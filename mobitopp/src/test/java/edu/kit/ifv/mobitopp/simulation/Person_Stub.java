package edu.kit.ifv.mobitopp.simulation;

import java.util.Collection;
import java.util.List;

import edu.kit.ifv.mobitopp.data.ExampleZones;
import edu.kit.ifv.mobitopp.data.PatternActivityWeek;
import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.data.person.PersonId;
import edu.kit.ifv.mobitopp.simulation.activityschedule.ActivityIfc;
import edu.kit.ifv.mobitopp.simulation.activityschedule.randomizer.ActivityStartAndDurationRandomizer;
import edu.kit.ifv.mobitopp.simulation.car.PrivateCar;
import edu.kit.ifv.mobitopp.simulation.person.PersonState;
import edu.kit.ifv.mobitopp.time.Time;

public class Person_Stub
	implements Person
{

	private final int oid;
	private final Car car;
	private final Household household;
	private final ActivityIfc nextHomeActivity;
	private final boolean commuterTicket;

	private final Zone zone = ExampleZones.create().someZone();


	public Person_Stub(
		int oid, 
		Household household, 
		Car car, ActivityIfc 
		nextHomeActivity, 
		boolean commuterTicket
	) {
		this.oid = oid;
		this.household = household;
		this.car = car;
		this.nextHomeActivity = nextHomeActivity;
		this.commuterTicket = commuterTicket;
	}

	public Person_Stub(int oid, Household household, Car car, ActivityIfc nextHomeActivity) {
		this(oid, household, car, nextHomeActivity, false);
	}

	public Person_Stub(int oid, Household household, Car car) {
		this(oid, household, car, null);
	}

	public Person_Stub(int oid, Household household) {
		this(oid, household, null);
	}

	public int getOid() { return this.oid; }
	public Household household() { return this.household; }

	public boolean isCarDriver() { return this.car!=null; } 	// 'true' needed by 'RideSharingOffers'
	public boolean isCarPassenger() { return this.car!=null; } 	// 'true' needed by 'RideSharingOffers'
	public void useCar(Car car, Time time) {}
	public Car whichCar() { return this.car; }
	public Car releaseCar(Time time) { return null; }


	public boolean hasPersonalCar() { return false; }
	public boolean hasAccessToCar() { return false; }
	public boolean hasBike() { return false; }

	public boolean hasCommuterTicket() { return this.commuterTicket; }
	public boolean hasDrivingLicense() { return true; }

	public int getIncome() { return 0; }


	public PersonId getId() { return null; }

	public ModifiableActivityScheduleWithState activitySchedule() { return null; }
	public void initSchedule(ActivityStartAndDurationRandomizer activityDurationRandomizer, List<Time> days) {}

	public Gender gender() { return null; }
	public Employment employment() { return null; }
	public int age() { return -1; }

	public Zone homeZone() { return null; }
 	public boolean hasFixedZoneFor(ActivityType activityType) { return false; }
 	public Zone fixedZoneFor(ActivityType activityType) { return null; }
	public Zone fixedActivityZone() { return null; }
	public boolean hasFixedActivityZone() { return false; }
	public Zone nextFixedActivityZone(ActivityIfc activity) { return zone; }

	public void leaveCar() {}
	public void useCarAsPassenger(Car car) {}
	public PrivateCar personalCar() { return null;}
	public void assignPersonalCar(PrivateCar personalCar) {}
	public boolean hasPersonalCarAssigned() { return false; }

	public PatternActivityWeek getPatternActivityWeek() { return null; }
	public ActivityIfc currentActivity() { return null; }
	public ActivityIfc nextActivity() { return null; }
	public ActivityIfc nextHomeActivity() { return nextHomeActivity; }
	public TripIfc currentTrip() { return null; }
	public void currentTrip(TripIfc trip) { }

	public PersonState getState() { return null; }

	public boolean isFemale() { return false; }

	public boolean isMale() { return true; }

	public boolean isCarSharingMember() { return false; }

	public String forLogging(ImpedanceIfc impedance) { return ""; }

 	public Location fixedDestinationFor(ActivityType activityType) { return null; }
 	
 	@Override
	public PersonAttributes attributes() {
		return null;
	}
 	
  @Override
  public Collection<FixedDestination> getFixedDestinations() {
  	return null;
  }

	public void startActivity(Time currentDate, ActivityIfc activity, TripIfc precedingTrip,	ReschedulingStrategy rescheduling) {}

	@Override
	public Car parkCar(Zone zone, Location location, Time time) {
		return null;
	}

	@Override
	public boolean hasParkedCar() {
		return false;
	}

	@Override
	public void takeCarFromParking() {
		
	}

}
