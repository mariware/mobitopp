package edu.kit.ifv.mobitopp.simulation.destinationAndModeChoice;

import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.simulation.Mode;
import edu.kit.ifv.mobitopp.simulation.Person;
import edu.kit.ifv.mobitopp.simulation.activityschedule.ActivityIfc;

import java.util.Set;
import java.util.Map;


public interface DestinationAndModeChoiceUtility {

	public Map<Zone,Double> calculateUtilitiesForUpperModel(
		Set<Zone> choiceSet,
		Person person,
		ActivityIfc previousActivity,
		ActivityIfc nextActivity,
		Zone source,
		Map<Zone,Set<Mode>> availableModes
	);

	public Map<Mode,Double> calculateUtilitiesForLowerModel(
		Set<Mode> choiceSet,
		Person person,
		ActivityIfc previousActivity,
		ActivityIfc nextActivity,
		Zone source, 
		Zone destination
	);

}
