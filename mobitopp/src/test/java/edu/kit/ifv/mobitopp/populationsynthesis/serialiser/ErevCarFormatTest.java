package edu.kit.ifv.mobitopp.populationsynthesis.serialiser;

import static edu.kit.ifv.mobitopp.util.TestUtil.assertValue;
import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.data.ZoneRepository;
import edu.kit.ifv.mobitopp.populationsynthesis.Example;
import edu.kit.ifv.mobitopp.simulation.Car;
import edu.kit.ifv.mobitopp.simulation.car.AbstractElectricCar;
import edu.kit.ifv.mobitopp.simulation.car.CarPosition;
import edu.kit.ifv.mobitopp.simulation.car.ExtendedRangeElectricCar;

public class ErevCarFormatTest {
	private static final int zoneOid = 1;

	private Zone zone;
	private ErevCarFormat format;
	private ExtendedRangeElectricCar original;
	private ZoneRepository zoneRepository;

	@Before
	public void initialise() {
		zone = mock(Zone.class);
		zoneRepository = mock(ZoneRepository.class);
		ConventionalCarFormat conventionalCarFormat = new ConventionalCarFormat(zoneRepository);
		AbstractElectricCarFormat electricCarFormat = new AbstractElectricCarFormat(conventionalCarFormat);
		format = new ErevCarFormat(electricCarFormat);

		when(zone.getOid()).thenReturn(zoneOid);
		when(zoneRepository.getZoneByOid(zoneOid)).thenReturn(zone);

		original = Example.erevCar(zone);
	}

	@Test
	public void prepareBevCar() {
		List<String> prepared = format.prepare(original);

		assertThat(prepared, is(equalTo(erevCar())));
	}

	@Test
	public void parseBevCar() {
		ExtendedRangeElectricCar parsed = format.parse(erevCar());

		assertCars(parsed, original);
	}

	private void assertCars(ExtendedRangeElectricCar actual, ExtendedRangeElectricCar original) {
		assertValue(Car::id, actual, original);
		assertCarPosition(actual, original);
		assertValue(Car::carSegment, actual, original);
		assertValue(Car::capacity, actual, original);
		assertValue(Car::currentMileage, actual, original);
		assertValue(Car::currentFuelLevel, actual, original);
		assertValue(Car::maxRange, actual, original);
		assertValue(AbstractElectricCar::currentBatteryLevel, actual, original);
		assertValue(AbstractElectricCar::electricRange, actual, original);
		assertValue(AbstractElectricCar::currentBatteryCapacity, actual, original);
		assertValue(AbstractElectricCar::minimumChargingLevel, actual, original);
		assertValue(ExtendedRangeElectricCar::fullPowerRange, actual, original);
	}

	private void assertCarPosition(Car actual, Car original) {
		CarPosition actualPosition = actual.position();
		CarPosition originalPosition = original.position();
		assertValue(CarPosition::zone, actualPosition, originalPosition);
		assertValue(CarPosition::location, actualPosition, originalPosition);
	}

	private List<String> erevCar() {
		ArrayList<String> attributes = new ArrayList<>();
		attributes.add(valueOf(Example.carId));
		attributes.add(valueOf(zoneOid));
		attributes.add(valueOf(Example.serialisedLocation));
		attributes.add(valueOf(Example.segment));
		attributes.add(valueOf(Example.capacity));
		attributes.add(valueOf(Example.initialMileage));
		attributes.add(valueOf(Example.conventionalFuelLevel));
		attributes.add(valueOf(Example.conventionalRange + Example.electricRange));
		attributes.add(valueOf(Example.batteryLevel));
		attributes.add(valueOf(Example.electricRange));
		attributes.add(valueOf(Example.batteryCapacity));
		attributes.add(valueOf(Example.minimumChargingLevel));
		attributes.add(valueOf(Example.fullPowerRange));
		return attributes;
	}
}
