package edu.kit.ifv.mobitopp.util.panel;

class PersonInfo {

	public int person_number;
	public int sex;
	public int graduation;
	public int birth_year;
	public int employment_type;
	public int pole_distance;
	public int income = -1;
	public boolean commutation_ticket;
	public boolean licence;
	
	//modetypeweights
	public boolean fahrrad;
	public boolean apkwverf;
	public boolean ppkwverf;

	public float pref_cardriver;
	public float pref_carpassenger;
	public float pref_walking;
	public float pref_cycling;
	public float pref_publictransport;


	public String toString() {

		return getClass().getName()
					+ ": "
					+ person_number + ", "
					+ sex + ", "
					+ graduation + ", "
					+ birth_year + ", "
					+ employment_type + ", "
					+ pole_distance + ", "
					+ commutation_ticket + ", "
					+ fahrrad + ", "
					+ apkwverf + ", "
					+ ppkwverf + ", "
				;
	}
}
