package edu.kit.ifv.mobitopp.actitopp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Tim Hilgert
 *
 */
public class ActitoppPerson
{
	
	// Enthlt alle Attribute, die nicht direkt ber Variablen ausgelesen werden knnen
	private Map<String, Double> attributes;
	
	private HWeekPattern weekPattern;

	private int PersIndex;
	
	private int age;
	private int gender;
	private int children0_10;
	private int children_u18;
	private int employment;
	private int areatype;
	private int numberofcarsinhousehold;	
	
	// Pendeldistanzen sind als Default = 0, das heit nicht verfgbar oder Person pendelt nicht
	private double commutingdistance_work = 0.0;
	private double commutingdistance_education = 0.0;
	
	
	/**
	 * Konstruktor zum Erstellen einer Person
	 * 
	 * @param PersIndex
	 * @param children0_10
	 * @param children_u18
	 * @param age
	 * @param employment
	 * @param gender
	 * @param areatype
	 * @param numberofcarsinhousehold
	 */
	public ActitoppPerson(
			int PersIndex,
			int children0_10,
			int children_u18,
			int age,
			int employment,
			int gender,
			int areatype,
			int numberofcarsinhousehold
	) {
		this.PersIndex = PersIndex;
		this.children0_10 = children0_10;
		this.children_u18 = children_u18;
		this.age = age;
		this.employment = employment;
		this.gender = gender;
		this.areatype = areatype;
		this.numberofcarsinhousehold = numberofcarsinhousehold;  
		
		this.attributes = new HashMap<String, Double>();
		
		}
	
	
	/**
	 * Konstruktor zum Erstellen einer Person
	 * 
	 * @param PersIndex
	 * @param children0_10
	 * @param children_u18
	 * @param age
	 * @param employment
	 * @param gender
	 * @param areatype
	 * @param numberofcarsinhousehold
	 * @param commutingdistance_work
	 * @param commutingdistance_education
	 */
	public ActitoppPerson(
			int PersIndex,
			int children0_10,
			int children_u18,
			int age,
			int employment,
			int gender,
			int areatype,
			int numberofcarsinhousehold,
			double commutingdistance_work,
			double commutingdistance_education
	) {
		this.PersIndex = PersIndex;
		this.children0_10 = children0_10;
		this.children_u18 = children_u18;
		this.age = age;
		this.employment = employment;
		this.gender = gender;
		this.areatype = areatype;
		this.numberofcarsinhousehold = numberofcarsinhousehold;  
		this.setCommutingdistance_work(commutingdistance_work);
		this.setCommutingdistance_education(commutingdistance_education);
		
		this.attributes = new HashMap<String, Double>();
		
		}	

	
  /**
	 * @return the weekPattern
	 */
	public HWeekPattern getWeekPattern() {
		return weekPattern;
	}


	/**
	 * @return the persIndex
	 */
	public int getPersIndex() {
		return PersIndex;
	}


	/**
	 * @param persIndex the persIndex to set
	 */
	public void setPersIndex(int persIndex) {
		PersIndex = persIndex;
	}


	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}


	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}


	/**
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}


	/**
	 * @param gender the gender to set
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}


	/**
	 * @return the children0_10
	 */
	public int getChildren0_10() {
		return children0_10;
	}


	/**
	 * @param children0_10 the children0_10 to set
	 */
	public void setChildren0_10(int children0_10) {
		this.children0_10 = children0_10;
	}


	/**
	 * @return the children_u18
	 */
	public int getChildren_u18() {
		return children_u18;
	}


	/**
	 * @param children_u18 the children_u18 to set
	 */
	public void setChildren_u18(int children_u18) {
		this.children_u18 = children_u18;
	}


	/**
	 * @return the employment
	 */
	public int getEmployment() {
		return employment;
	}


	/**
	 * @param employment the employment to set
	 */
	public void setEmployment(int employment) {
		this.employment = employment;
	}


	/**
	 * @return the areatype
	 */
	public int getAreatype() {
		return areatype;
	}


	/**
	 * @param areatype the areatype to set
	 */
	public void setAreatype(int areatype) {
		this.areatype = areatype;
	}


	/**
	 * @return the numberofcarsinhousehold
	 */
	public int getNumberofcarsinhousehold() {
		return numberofcarsinhousehold;
	}


	/**
	 * @param numberofcarsinhousehold the numberofcarsinhousehold to set
	 */
	public void setNumberofcarsinhousehold(int numberofcarsinhousehold) {
		this.numberofcarsinhousehold = numberofcarsinhousehold;
	}
	
	
	/**
	 * @return the commutingdistance_work
	 */
	public double getCommutingdistance_work() {
		return commutingdistance_work;
	}


	/**
	 * @param commutingdistance_work the commutingdistance_work to set
	 */
	public void setCommutingdistance_work(double commutingdistance_work) {
		this.commutingdistance_work = commutingdistance_work;
	}


	/**
	 * @return the commutingdistance_education
	 */
	public double getCommutingdistance_education() {
		return commutingdistance_education;
	}


	/**
	 * @param commutingdistance_education the commutingdistance_education to set
	 */
	public void setCommutingdistance_education(double commutingdistance_education) {
		this.commutingdistance_education = commutingdistance_education;
	}
	
	/**
	 * 
	 * @return the commutingduration_work [min]
	 */
	public int getCommutingDuration_work()
	{
		// mittlere Pendelgeschwindigkeit in km/h wird je gruppierter Pendelentfernung festgelegt
		// die mittleren Geschwindigkeiten wurden ber alle Pendelwege des MOP 2004-2013 ermittelt
		double commutingspeed_work;
		if 			(commutingdistance_work>0  && commutingdistance_work <= 5)  commutingspeed_work = 16;
		else if (commutingdistance_work>5  && commutingdistance_work <= 10) commutingspeed_work = 29;
		else if (commutingdistance_work>10 && commutingdistance_work <= 20) commutingspeed_work = 38;
		else if (commutingdistance_work>20 && commutingdistance_work <= 50) commutingspeed_work = 51;
		else if (commutingdistance_work>50) 																commutingspeed_work = 67;
		else																																commutingspeed_work = 32;
		
		// Mindestdauer jedes Wegs: 1 Minute
		return (int) Math.max(1, Math.round((commutingdistance_work/commutingspeed_work)*60));
	}
	
	
	/**
	 * 
	 * @return the commutingduration_education [min]
	 */
	public int getCommutingDuration_education()
	{
		// mittlere Pendelgeschwindigkeit in km/h wird je gruppierter Pendelentfernung festgelegt
		// die mittleren Geschwindigkeiten wurden ber alle Pendelwege des MOP 2004-2013 ermittelt
		double commutingspeed_education;
		if 			(commutingdistance_education>0  && commutingdistance_education <= 5)  commutingspeed_education = 12;
		else if (commutingdistance_education>5  && commutingdistance_education <= 10) commutingspeed_education = 21;
		else if (commutingdistance_education>10 && commutingdistance_education <= 20) commutingspeed_education = 28;
		else if (commutingdistance_education>20 && commutingdistance_education <= 50) commutingspeed_education = 40;
		else if (commutingdistance_education>50) 																			commutingspeed_education = 55;
		else																																					commutingspeed_education = 21;
		
		// Mindestdauer jedes Wegs: 1 Minute
		return (int) Math.max(1, Math.round((commutingdistance_education/commutingspeed_education)*60));
	}
	

	/**
	 * @param attributes spezifischesAttribut fr Map
	 */
	public void addAttributetoMap(String name, Double value) {
		this.attributes.put(name, value);
	}
	
	/**
	 * 
	 * @param name spezifisches Attribut aus Map
	 * @return
	 */
	public double getAttributefromMap(String name) {
		return this.attributes.get(name);
	}


	/**
	 * @return the attributes
	 */
	public Map<String, Double> getAttributesMap() {
		return attributes;
	}


	@Override
	public String toString()	{
  	StringBuffer message = new StringBuffer();

  	message.append("\n Personeninformationen");
  	
		message.append("\n - Nummer : ");
		message.append(PersIndex);
		
		message.append("\n - Anzahl Kinder 0-10 : ");
		message.append(getChildren0_10());
		
		message.append("\n - Anzahl Kinder unter 18 : ");
		message.append(getChildren_u18());
		
		message.append("\n - Alter : ");
		message.append(getAge());

		message.append("\n - Beruf : ");
		message.append(getEmployment());
		
		message.append("\n - Geschlecht : ");
		message.append(getGender());
		
		message.append("\n - Raumtyp : ");
		message.append(getAreatype());
		
		message.append("\n - Pkw im HH : ");
		message.append(getNumberofcarsinhousehold());		

		message.append("\n - Pendeldistanz Arbeiten : ");
		message.append(getCommutingdistance_work());		
		
		message.append("\n - Pendeldistanz Bildung : ");
		message.append(getCommutingdistance_education());		
		
		return message.toString();
	}
	
	/**
	 * Methode erzeugt Wochenaktivittenplan fr Person
	 * 
	 * @param modelbase
	 * @throws InvalidPatternException
	 * @throws PrerequisiteNotMetException
	 */
	public void generateSchedule(ModelFileBase modelbase, RNGHelper randomgenerator)	throws InvalidPatternException 
	{
		// Erzeuge ein leeres Default-Pattern
		weekPattern = new HWeekPattern(this);
		
		// Erzeuge einen Coordinator zum Modellablauf
		Coordinator modelCoordinator = new Coordinator(this, modelbase, randomgenerator);
		
		// Erzeuge den Schedule
		try 
		{
			modelCoordinator.executeModel();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Methode zum berprfen der Vorbedingung von Schritt 9
	 * 
	 * @return
	 */
	public boolean isPersonWorkerAndWorkMainToursAreScheduled()
	{   
		int employmenttype = this.getEmployment();
    if (employmenttype == 1 || employmenttype == 2 || employmenttype == 40 || employmenttype == 41 || employmenttype == 42 || employmenttype == 5)
    {
      for (HDay day : getWeekPattern().getDays())
      {
        for (HTour tour : day.getTours())
        {
          if (tour.getActivity(0).getType() == 'W' || tour.getActivity(0).getType() == 'E')
        	{
          	return true;
          }
        }
      }
    }
    else
    {
        return false;
    }
    return false;
	}



	
}
