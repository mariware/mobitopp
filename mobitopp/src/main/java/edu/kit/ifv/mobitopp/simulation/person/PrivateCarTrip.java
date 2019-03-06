package edu.kit.ifv.mobitopp.simulation.person;

import java.util.Objects;

import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.simulation.Car;
import edu.kit.ifv.mobitopp.simulation.ImpedanceIfc;
import edu.kit.ifv.mobitopp.simulation.Location;
import edu.kit.ifv.mobitopp.simulation.Mode;
import edu.kit.ifv.mobitopp.simulation.Person;
import edu.kit.ifv.mobitopp.simulation.PersonResults;
import edu.kit.ifv.mobitopp.simulation.TripData;
import edu.kit.ifv.mobitopp.simulation.BaseTrip;
import edu.kit.ifv.mobitopp.simulation.Trip;
import edu.kit.ifv.mobitopp.simulation.activityschedule.ActivityIfc;
import edu.kit.ifv.mobitopp.simulation.car.CarPosition;
import edu.kit.ifv.mobitopp.simulation.car.PrivateCar;
import edu.kit.ifv.mobitopp.time.Time;

public class PrivateCarTrip extends BaseTrip implements Trip {

  public PrivateCarTrip(TripData trip, SimulationPerson person) {
    super(trip, person);
  }

  @Override
  public void prepareTrip(ImpedanceIfc impedance, Time currentTime) {
    Objects.requireNonNull(impedance);
    Objects.requireNonNull(currentTime);
    if (person().currentActivity().activityType().isHomeActivity()) {
      useCarOfHousehold(impedance, currentTime);
    } else {
      useParkedCar();
    }
  }

  private void useParkedCar() {
    assert person().hasParkedCar();
    assert !person().isCarDriver();
    person().takeCarFromParking();
  }

  private void useCarOfHousehold(ImpedanceIfc impedance, Time currentTime) {
    int origin = origin().zone().getOid();
    int destination = destination().zone().getOid();
    float distance = impedance.getDistance(origin, destination);
    float distanceKm = distance / 1000.0f;
    PrivateCar car = person().household().takeAvailableCar(person(), distanceKm);
    person().useCar(car, currentTime);
  }

  @Override
  public FinishedTrip finish(Time currentDate, PersonResults results) {
    Person person = person();
    if (person.isCarDriver()) {
      return doFinish(currentDate, results);
    }
    throw new IllegalStateException(String.format("Cannot finish car trip without car: %s" , trip()));
  }

  private FinishedTrip doFinish(Time currentDate, PersonResults results) {
    FinishedTrip finishedTrip = super.finish(currentDate, results);
    int carId = stopCar(currentDate);
    notifyFinishCarTrip(results, finishedTrip);
    returnCar(currentDate);
    return new FinishedCarTrip(finishedTrip, carId);
  }

  private Integer stopCar(Time currentDate) {
    assert mode() == Mode.CAR : (trip() + "\n" 
                                 + "prev: " + previousActivity() + "\n"
                                 + "next : " + nextActivity()  + "\n --- \n"
                                 + person().activitySchedule() + "\n");
    Location location = nextActivity().location();
    Zone zone = nextActivity().zone();
    Car car = person().whichCar();
    car.stop(currentDate, new CarPosition(zone, location));
    return car.id();
  }

  private void notifyFinishCarTrip(PersonResults results, FinishedTrip finishedTrip) {
    ActivityIfc prevActivity = trip().previousActivity();
    assert prevActivity != null;
    results.notifyFinishCarTrip(person(), person().whichCar(), finishedTrip, nextActivity());
  }

  private void returnCar(Time currentDate) {
    ActivityIfc activity = nextActivity();
    Zone zone = activity.zone();
    Location location = activity.location();
    if (activity.activityType().isHomeActivity()) {
      Car car = person().releaseCar(currentDate);
      car.returnCar(zone);
    } else {
      Car car = person().parkCar(zone, location, currentDate);
      assert car instanceof PrivateCar;
    }
  }
}
