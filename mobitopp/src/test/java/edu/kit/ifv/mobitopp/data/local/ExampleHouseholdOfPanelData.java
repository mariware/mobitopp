package edu.kit.ifv.mobitopp.data.local;

import edu.kit.ifv.mobitopp.util.panel.HouseholdOfPanelData;
import edu.kit.ifv.mobitopp.util.panel.HouseholdOfPanelDataId;

public class ExampleHouseholdOfPanelData {

	public static final short year = 2000;
	public static final int aNumber = 3;
	public static final int otherNumber = 4;
	public static final HouseholdOfPanelDataId anId = new HouseholdOfPanelDataId(year, aNumber);
	public static final HouseholdOfPanelDataId otherId = new HouseholdOfPanelDataId(year, otherNumber);
	public static final int areaType = 4;
	public static final int size = 5;
	public static final int aDomCode = 6;
	public static final int otherDomCode = 7;
	public static final int reportingPersons = 8;
	public static final int minors = 9;
	public static final int notReportingChildren = 10;
	public static final int cars = 11;
	public static final int income = 12;
  public static final int incomeClass = 1;
	public static final HouseholdOfPanelData household = ExampleHouseholdOfPanelData.household(aDomCode, anId);

	public static HouseholdOfPanelData household(int domCode, HouseholdOfPanelDataId id) {
		return new HouseholdOfPanelData(id, areaType, size, domCode, reportingPersons, minors,
				notReportingChildren, cars, income, incomeClass);
	}

}
