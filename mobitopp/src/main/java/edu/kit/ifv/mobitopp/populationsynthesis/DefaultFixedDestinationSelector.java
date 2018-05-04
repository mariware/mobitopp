package edu.kit.ifv.mobitopp.populationsynthesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.kit.ifv.mobitopp.data.FixedDistributionMatrix;
import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.data.ZoneRepository;
import edu.kit.ifv.mobitopp.simulation.ActivityType;
import edu.kit.ifv.mobitopp.simulation.Employment;
import edu.kit.ifv.mobitopp.simulation.ImpedanceIfc;
import edu.kit.ifv.mobitopp.simulation.Location;
import edu.kit.ifv.mobitopp.simulation.Person;
import edu.kit.ifv.mobitopp.simulation.emobility.EmobilityPerson;
import edu.kit.ifv.mobitopp.util.panel.PersonOfPanelData;


class DefaultFixedDestinationSelector
implements FixedDestinationSelector
{	

	private Map<ActivityType,FixedDistributionMatrix> _fixedDistributionMatrices = null;

	private final Random random;

	private ActivityType[] activityTypes = new ActivityType[] {
																									ActivityType.WORK,
																									ActivityType.EDUCATION_PRIMARY,
																									ActivityType.EDUCATION_SECONDARY,
																									ActivityType.EDUCATION_TERTIARY,
																									ActivityType.EDUCATION_OCCUP,
																									ActivityType.EDUCATION
																								};

	protected final ZoneRepository zoneRepository;

	protected final	ImpedanceIfc impedance;

	public DefaultFixedDestinationSelector(
		ZoneRepository zoneRepository,
		Map<ActivityType,FixedDistributionMatrix> fixedDistributionMatrices_,
		ImpedanceIfc impedance,
		long randomSeed	
	)
	{
		this.zoneRepository = zoneRepository;
		this._fixedDistributionMatrices = fixedDistributionMatrices_;

		this.impedance = impedance;
		this.random = new Random(randomSeed);
	}

	public void setFixedDestinations(
		Zone zone, 
		Map<Integer, Person> persons,
		Map<Integer, PersonOfPanelData> panelPersons
	)
	{

			// Für alle Poltypen (Arbeit/Ausbildung ggf. differenziert)
			// 1. Personen zählen
			// 2. nach Poldistanz sortieren
			// 3. Verteilung der Zielzellen nach Distanz
			// 4. Distanzverteilung der Zielzellen skalieren (entsprechend Personenzahl)
			// 5. Pole zuordnen

		for (ActivityType activityType : this.activityTypes) {
			SortedMap<Integer,List<Person>> personDistances 
				= calculatePersonDistanceDistribution(activityType, persons, panelPersons);

			// 3. Verteilung der Zielzellen nach Distanz
			// 4. Distanzverteilung der Zielzellen skalieren (entsprechend Personenzahl)
			// 5. Pole zuordnen

			SortedMap<Integer,Map<Integer,Float>> zoneDistances 
				= calculateZoneDistanceDistribution(zone, activityType);

			reweightZoneDistanceDistribution(zoneDistances, activityType);

			rescaleZoneDistanceDistributions(personDistances, zoneDistances);	

			ActivityType fixedActivityType = (activityType == ActivityType.WORK)
																				? ActivityType.WORK
																				: ActivityType.EDUCATION;

			setPersonsPoleZones(fixedActivityType, personDistances, zoneDistances);	
		}
	}

	private SortedMap<Integer,Map<Integer,Float>>	calculateZoneDistanceDistribution(
		Zone zone,
		ActivityType activityType
	) {

			FixedDistributionMatrix distMatrix = this._fixedDistributionMatrices.get(activityType);

			SortedMap<Integer,Map<Integer,Float>> distances = new TreeMap<Integer,Map<Integer,Float>>();

			Integer homezoneOid = zone.getOid();

			for (Integer targetzoneOid : distMatrix.oids()) {
				Float dist_float = this.impedance.getDistance(homezoneOid,targetzoneOid);
				Integer dist = dist_float.intValue();
				Float count = distMatrix.get(homezoneOid,targetzoneOid);
				if (count > 0.0f || targetzoneOid == zone.getOid()) {
					if (distances.get(dist) == null) {
								distances.put(dist, new HashMap<Integer,Float>());
					}

					if (count == 0.0f) { // work-around in case that no target zone exists
						count = 0.000001f;
					}
					
					distances.get(dist).put(targetzoneOid, count);
				}

			}

		return distances;
	}

	private void reweightZoneDistanceDistribution(
		SortedMap<Integer,Map<Integer,Float>> distances,
		ActivityType activityType
	) {

		float	beta;
		float	alpha;


		if (activityType == ActivityType.EDUCATION_PRIMARY) {
			beta = -0.1f;
			alpha = 1e-3f;
		} else if (activityType == ActivityType.EDUCATION_SECONDARY) {
			beta = 0.9f;
			alpha = 4e-4f;
		} else if (activityType == ActivityType.EDUCATION_TERTIARY) {
			beta = 0.5f;
			alpha = 1e-4f;
		} else if (activityType == ActivityType.EDUCATION_OCCUP) {
			beta = 0.5f;
			alpha = 1e-4f;
		} else if (activityType == ActivityType.EDUCATION) {
			beta = 0.7f;
			alpha = 1e-4f;
		} else if (activityType == ActivityType.WORK) {
			beta = 0.45f;
			alpha = 1e-3f;
		} else {
			beta = 0.0f;
			alpha = 0.0f;
		}

		

		for (Integer distance : distances.keySet()) {

			Map<Integer,Float> zones = distances.get(distance);

			float damping = 1.0f - beta*(float)Math.exp(-alpha*distance);
			for (Integer zone : zones.keySet()) {

				float value = zones.get(zone);

				zones.put(zone, damping*value);
			}
		}
	}

	private SortedMap<Integer,List<Person>> calculatePersonDistanceDistribution(
		ActivityType activityType,
		Map<Integer, Person> demandPersons,
		Map<Integer, PersonOfPanelData> panelPersons
	) {
			SortedMap<Integer,List<Person>> distances = new TreeMap<Integer,List<Person>>();

			for (Integer personId : demandPersons.keySet()) {

				PersonOfPanelData personOfPanelData = panelPersons.get(personId);
				Person p = demandPersons.get(personId);

				PersonForSetup person = null;

				if (p instanceof PersonForSetup) { 
					person = (PersonForSetup)p; 
				} else {
					assert p instanceof EmobilityPerson;

					person = ((EmobilityPerson) p).personForSetup();
				}


				assert person != null;

				assert personOfPanelData != null;
				

				// Think twice before changing the following if.
				//~10% of all persons did not report a pole distance, so no zone will be assigned. 
				// This matches the percentage of holiday weeks in a year vs. all weeks of a year (5/50)
				if (
						activityTypeMatchesEmploymentType(activityType, person.employment())
						|| (activityType == ActivityType.WORK || activityType == ActivityType.EDUCATION)
								&& person.getPatternActivityWeek().existsPatternActivity(activityType)
								&& !person.hasFixedZoneFor(activityType)
				) {
					int dist = (int) personOfPanelData.getPoleDistance();

					if (distances.get(dist) == null) {
							distances.put(dist, new ArrayList<Person>());
					}

					distances.get(dist).add(person);
				}
			}

		return distances;
	}

	private boolean activityTypeMatchesEmploymentType(
		ActivityType activityType, 
		Employment employmentType
	) {

		return activityType == ActivityType.EDUCATION_PRIMARY 
						&& employmentType == Employment.STUDENT_PRIMARY
				|| activityType == ActivityType.EDUCATION_SECONDARY 
						&& employmentType == Employment.STUDENT_SECONDARY
				|| activityType == ActivityType.EDUCATION_TERTIARY 
						&& employmentType == Employment.STUDENT_TERTIARY
				|| activityType == ActivityType.EDUCATION_OCCUP
						&& employmentType == Employment.EDUCATION
		;
	}


	private void rescaleZoneDistanceDistributions(
		SortedMap<Integer,List<Person>> personDistances, 
		SortedMap<Integer,Map<Integer,Float>> zoneDistances
	) {

		int pers_cnt = 0;
		float zone_cnt = 0.0f;

		for (Integer dist : personDistances.keySet()) {
			pers_cnt +=  personDistances.get(dist).size();
		}

		for (Integer dist : zoneDistances.keySet()) {
			for (Integer target : zoneDistances.get(dist).keySet()) {
				zone_cnt +=  zoneDistances.get(dist).get(target);
			}
		}

		float scaling = pers_cnt / zone_cnt;

		double curr_cnt = 0.0f;
		double culum_remainder = 0.0;

		for (Integer dist : zoneDistances.keySet()) {
			for (Integer target : zoneDistances.get(dist).keySet()) {
				float val =  zoneDistances.get(dist).get(target);
				double scaled =  val*scaling;
				double rounded = Math.floor(scaled);
				double remainder = scaled - rounded;

				culum_remainder += remainder;

				if (culum_remainder > 1.0) {
					rounded += 1.0;
					culum_remainder -= 1.0;
				}	

				zoneDistances.get(dist).put(target, (float) rounded);

				curr_cnt += rounded;
			}
		}

		int diff = pers_cnt - (int) curr_cnt;

		Integer first = zoneDistances.firstKey();
		Map<Integer, Float> firstMap = zoneDistances.get(first);
		Integer zone = firstMap.keySet().iterator().next();
		firstMap.put(zone, firstMap.get(zone)+diff);


	}

	private void setPersonsPoleZones(
		ActivityType activityType,
		SortedMap<Integer,List<Person>> personDistances, 
		SortedMap<Integer,Map<Integer,Float>> zoneDistances
	) {

			LinkedList<Integer> zoneOids = new LinkedList<Integer>();

			for (Integer dist : zoneDistances.keySet()) {
				for (Integer zone : zoneDistances.get(dist).keySet()) {
					for (int i=0; i <zoneDistances.get(dist).get(zone); i++) {
						zoneOids.add(zone);
					}
				}
			}


			for (Integer dist : personDistances.keySet()) {
				for (Person p : personDistances.get(dist)) {

					int zoneId = zoneOids.remove();

					Zone zone = this.zoneRepository.getZoneByOid(zoneId);

					assert zone != null;


				PersonForSetup person = null;

				if (p instanceof PersonForSetup) { 
					person = (PersonForSetup)p; 
				} else {
					assert p instanceof EmobilityPerson;

					person = ((EmobilityPerson) p).personForSetup();
				}

					Location location;

					if(zone.opportunities().locationsAvailable(activityType)) {
						location = zone.opportunities().selectRandomLocation(activityType, this.random.nextDouble());
					} else {
						location = zone.getZonePolygon().centroidLocation();
					}

					person.setFixedDestination(activityType, zone, location);
				}
			}

	}

}

