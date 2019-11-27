package edu.kit.ifv.mobitopp.populationsynthesis.community;

import java.util.function.Consumer;

import edu.kit.ifv.mobitopp.data.DemandZone;
import edu.kit.ifv.mobitopp.data.PopulationForSetup;
import edu.kit.ifv.mobitopp.populationsynthesis.HouseholdForSetup;
import edu.kit.ifv.mobitopp.populationsynthesis.PersonBuilder;

public class PersonBasedStep implements PopulationSynthesisStep {

	private final Consumer<PersonBuilder> consumer;

	public PersonBasedStep(final Consumer<PersonBuilder> consumer) {
		super();
		this.consumer = consumer;
	}

	@Override
	public void process(final Community community) {
		community
				.zones()
				.map(DemandZone::getPopulation)
				.flatMap(PopulationForSetup::households)
				.flatMap(HouseholdForSetup::persons)
				.forEach(consumer);
	}

}
