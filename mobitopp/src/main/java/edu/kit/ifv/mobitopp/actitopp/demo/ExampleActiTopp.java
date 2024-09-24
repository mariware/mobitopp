package edu.kit.ifv.mobitopp.actitopp.demo;

import java.util.HashMap;

import edu.kit.ifv.mobitopp.actitopp.*;

public class ExampleActiTopp {
	
	private static ModelFileBase fileBase = new ModelFileBase();
	private static RNGHelper randomgenerator = new RNGHelper(1234);
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
		// createAndModelOnePerson_Example1();

		// createAndModelOnePerson_Example2();
		
		createAndModelMultiplePersons_Example();
		
	}
	
	
	/**
	 * 
	 * Erzeugung einer Person inkl. Aktivit�tenplan - Beispiel 1
	 * 
	 */
	public static void createAndModelOnePerson_Example1()
	{
		ActitoppPerson testperson = new ActitoppPerson(
				10, 	// PersIndex
				0, 		// Kinder 0-10
				1, 		// Kinder unter 18
				55, 	// Alter
				1, 		// Beruf
				1, 		// Geschlecht
				2, 		// Raumtyp
				2			// Pkw im HH
				);		
		System.out.println(testperson);
					
	 /* 
	  * Erzeuge Schedules f�r die Person bis der Schedule keine Fehler mehr hat.
	  *  
		* In einigen F�llen kommt es aufgrund ung�nstiger Zufallszahlen zu �berlappungen
		* in den Aktivit�tenpl�nen (bspw. nicht genug Zeit f�r alle Aktivit�ten).
		* In diesen seltenen F�llen wird die Planerstellung mit einer neuen Zufallszahl wiederholt.
		*/
		boolean scheduleOK = false;
    while (!scheduleOK)
    {
      try
      {
    		// Erzeuge Wochenaktivit�tenplan
     	 testperson.generateSchedule(fileBase, randomgenerator);
    		
        scheduleOK = true;                
      }
      catch (InvalidPatternException e)
      {
        System.err.println(e.getReason());
        System.err.println("person involved: " + testperson.getPersIndex());
      }
	  }
		
		//testperson.getweekPattern().printOutofHomeActivitiesList();
		testperson.getWeekPattern().printAllActivitiesList();
	}
	
	
	/**
	 * 
	 * Erzeugung einer Person inkl. Aktivit�tenplan - Beispiel 2
	 * 
	 */
	public static void createAndModelOnePerson_Example2()
	{
		ActitoppPerson testperson = new ActitoppPerson(
				20, 	// PersIndex
				0, 		// Kinder 0-10
				1, 		// Kinder unter 18
				55, 	// Alter
				1, 		// Beruf
				1, 		// Geschlecht
				2, 		// Raumtyp
				2,		// Pkw im HH
				3.0,	// Pendeldistanz zur Arbeit in Kilometern	(0 falls kein Pendeln)
				0.0		// Pendeldistanz zu Bildungszwecken in Kilometern (0 falls kein Pendeln)
				);		
		System.out.println(testperson);
		
	 /* 
	  * Erzeuge Schedules f�r die Person bis der Schedule keine Fehler mehr hat.
	  *  
		* In einigen F�llen kommt es aufgrund ung�nstiger Zufallszahlen zu �berlappungen
		* in den Aktivit�tenpl�nen (bspw. nicht genug Zeit f�r alle Aktivit�ten).
		* In diesen seltenen F�llen wird die Planerstellung mit einer neuen Zufallszahl wiederholt.
		*/
		boolean scheduleOK = false;
		while (!scheduleOK)
		{
			 try
			 {
				// Erzeuge Wochenaktivit�tenplan
				 testperson.generateSchedule(fileBase, randomgenerator);
				
			   scheduleOK = true;                
			 }
			 catch (InvalidPatternException e)
			 {
			   System.err.println(e.getReason());
			   System.err.println("person involved: " + testperson.getPersIndex());
			 }
		}
		
		//testperson.getweekPattern().printOutofHomeActivitiesList();
		testperson.getWeekPattern().printAllActivitiesList();
	}
	
	/**
	 * 
	 * Erzeugung mehrerer Personen inkl. Aktivit�tenplan
	 * 
	 */
	public static void createAndModelMultiplePersons_Example()
	{
		try
		{			
			CSVPersonInputReader loader = new CSVPersonInputReader(ModelFileBase.class.getResourceAsStream("demo/Demopersonen.csv"));
			HashMap<Number, ActitoppPerson> personmap = loader.loadInput();
			
			for (Number key : personmap.keySet())
			{
				ActitoppPerson actperson = personmap.get(key);
				// System.out.println(actperson);
				// System.out.println(actperson.getPersIndex());
				
				
			 /* 
			  * Erzeuge Schedules f�r die Person bis der Schedule keine Fehler mehr hat.
			  *  
				* In einigen F�llen kommt es aufgrund ung�nstiger Zufallszahlen zu �berlappungen
				* in den Aktivit�tenpl�nen (bspw. nicht genug Zeit f�r alle Aktivit�ten).
				* In diesen seltenen F�llen wird die Planerstellung mit einer neuen Zufallszahl wiederholt.
				*/
				boolean scheduleOK = false;
		    while (!scheduleOK)
		    {
	        try
	        {
	      		// Erzeuge Wochenaktivit�tenplan
	        	actperson.generateSchedule(fileBase, randomgenerator);
	      		
	          scheduleOK = true;                
	        }
	        catch (InvalidPatternException e)
	        {
	          System.err.println(e.getReason());
	          System.err.println("person involved: " + actperson.getPersIndex());
	        }
		    }
				
				//actperson.getWeekPattern().printAllActivitiesList();
				 
			}
				
			// Output als CSV-Datei
			CSVExportWriter exportwriter = new CSVExportWriter("D:/DemoTripList.csv");
			exportwriter.exportTripData(personmap);
			
			System.out.println("all persons processed!");	
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
