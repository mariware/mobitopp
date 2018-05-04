package edu.kit.ifv.mobitopp.populationsynthesis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.kit.ifv.mobitopp.simulation.Household;

public class PopulationDataForZone implements Serializable {
	
	private static final long serialVersionUID = 1L;

  private final List<Household> _households = new ArrayList<Household>();
  public PopulationDataForZone() {
		super();
	}

	public void addHousehold(Household household_) {
		assert household_ != null;

		boolean addFlag = this._households.add(household_);

		assert addFlag;
	}

	public List<Household> getHouseholds() {
		return this._households;
	}

}
