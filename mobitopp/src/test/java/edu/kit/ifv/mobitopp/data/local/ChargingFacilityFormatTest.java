package edu.kit.ifv.mobitopp.data.local;

import static com.github.npathai.hamcrestopt.OptionalMatchers.hasValue;
import static java.lang.String.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import edu.kit.ifv.mobitopp.populationsynthesis.Example;
import edu.kit.ifv.mobitopp.simulation.Location;
import edu.kit.ifv.mobitopp.simulation.emobility.ChargingFacility;
import edu.kit.ifv.mobitopp.simulation.emobility.ChargingFacility.Type;
import edu.kit.ifv.mobitopp.simulation.emobility.ChargingPower;

public class ChargingFacilityFormatTest {

	private static final int zoneId = 1;
	private static final int id = 2;
	private static final int stationId = 3;
	private static final Location location = Example.location;
	private static final Type type = Type.PUBLIC;
	private static final ChargingPower power = ChargingPower.fromKw(4);
	private static final ChargingFacility facility = new ChargingFacility(id, stationId, location,
			type, power);
	private static final ZoneChargingFacility zoneChargingFacility = new ZoneChargingFacility(zoneId,
			facility);

	@Test
	public void serialiseFacility() {
		ChargingFacilityFormat serialiser = new ChargingFacilityFormat();
		List<String> prepared = serialiser.prepare(zoneChargingFacility);

		assertThat(prepared, contains(valueOf(zoneId), valueOf(id), valueOf(stationId),
				valueOf(location), valueOf(type), valueOf(power.inKw())));
	}

	@Test
	public void deserialiseFacility() {
		ChargingFacilityFormat serialiser = new ChargingFacilityFormat();

		Optional<ZoneChargingFacility> parsed = serialiser
				.parse(serialiser.prepare(zoneChargingFacility));

		assertThat(parsed, hasValue(zoneChargingFacility));
	}
}
