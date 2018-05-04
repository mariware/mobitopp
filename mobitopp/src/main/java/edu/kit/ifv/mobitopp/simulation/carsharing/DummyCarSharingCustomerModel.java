package edu.kit.ifv.mobitopp.simulation.carsharing;


import java.util.Random;

import edu.kit.ifv.mobitopp.simulation.Person;

public class DummyCarSharingCustomerModel 
	implements CarSharingCustomerModel {

	private Random random;

	private double probability;

	public DummyCarSharingCustomerModel(
		Random random,
		double probability
	) {
		this.random = random;
		this.probability = probability;
	}


	public boolean estimateCustomership(
  	Person person
  ) {

		double random = this.random.nextDouble();

		return random <= probability;
	}

}
