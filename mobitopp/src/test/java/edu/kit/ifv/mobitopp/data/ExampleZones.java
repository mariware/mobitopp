package edu.kit.ifv.mobitopp.data;

import static java.util.Collections.emptyList;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import edu.kit.ifv.mobitopp.data.areatype.AreaType;
import edu.kit.ifv.mobitopp.data.areatype.ZoneAreaType;
import edu.kit.ifv.mobitopp.dataimport.DefaultPower;
import edu.kit.ifv.mobitopp.dataimport.DefaultRegionType;
import edu.kit.ifv.mobitopp.dataimport.RegionType;
import edu.kit.ifv.mobitopp.simulation.Location;
import edu.kit.ifv.mobitopp.simulation.emobility.ChargingDataForZone;
import edu.kit.ifv.mobitopp.simulation.emobility.LimitedChargingDataForZone;

public class ExampleZones {

	private static final Double dummyPoint = new Point2D.Double(0.0d, 0.0d);
	private static final int dummyAccessEdge = -1;

	private final Zone someZone;
	private final Zone otherZone;
	
	public ExampleZones(Zone someZone, Zone otherZone) {
		super();
		this.someZone = someZone;
		this.otherZone = otherZone;
	}

	public static ExampleZones create() {
		Zone someZone = zoneWithId("1");
		Zone otherZone = zoneWithId("2");
		return new ExampleZones(someZone, otherZone);
	}

	private static Zone zoneWithId(String id) {
		String name = "zone " + id;
		AreaType areaType = ZoneAreaType.METROPOLITAN;
		RegionType regionType = new DefaultRegionType(1);
		ZoneClassificationType classification = ZoneClassificationType.areaOfInvestigation;
		Attractivities attractivities = new Attractivities();
		ChargingDataForZone charging = createChargingData();
		Location centroid = new Location(dummyPoint, dummyAccessEdge, 0.0d);
    return new Zone(id, name, areaType, regionType, classification, centroid, attractivities, charging);
	}

	private static LimitedChargingDataForZone createChargingData() {
		return new LimitedChargingDataForZone(emptyList(), DefaultPower.zero);
	}

	public Zone someZone() {
		return someZone;
	}

	public Zone otherZone() {
		return otherZone;
	}

}
