package edu.kit.ifv.mobitopp.actitopp;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Tim Hilgert
 *
 */
public class Configuration {

	
/*
 * 		Steuerung der Input-Parametersets	
 * 
 * 		erlaubte Konfigurationen:
 * 
 * 		mopv10					=	Nutzung der Parametersch�tzungen des MOP 2004-2013 (actiTopp Version 1.0)
 * 		mopv11					= Nutzung der Parametersch�tzungen des MOP 2004-2013 (actiTopp Version 1.1)
 * 		stuttgart				=	Nutzung der Parametersch�tzungen auf Basis des MOP 2004-2013 kalibriert auf die Stuttgart-Erhebung	
 */
	
	public static final String parameterset = "mopv10";
	
	//Angabe der Stufen, die Flowlisten f�r Logit-Modellierungen verwenden
  public static final String[] flowlist_initials =
  { 		
  		"1A", "1B", "1C", "1D", "1E", "1F", 
  		"2A", 
  		"3A", "3B", 
  		"4A", 
  		"5A", "5B",
  		"6A", 
  		"7A", "7B", "7C", "7D", "7E", 
  		"8A", "8B", "8D", "8J", 
  		"9A", 
  		"10A",
  		"10B", "10D", "10G", "10J",
  		"10M", "10O", "10Q", "10S",
  		"98A", "98B", "98C"
  };
	
  
  //Angaben der Stufen, die Listen mit H�ufigkeitsverteilungen verwenden
  public static final String[] timedistributions_initials =
  { 
  		"7K", "7L", "7M", "7N", "7O", 
  		"8C", "8E", "8K", 
  		"10C", "10E", "10H", "10K", "10N", "10P", "10R", "10T"
  };

  
  //Angabe der maximalen Kategorie-Indizes bei H�ufigskeitsverteilungen
  public static final int[] timeDistributions_MaxIndizes =
  { 
  		4, 3, 5, 4, 3, 
  		14, 14, 14, 
  		15, 15, 10, 10, 15, 10, 10, 9
  };
	
  
	
/*
 * 		Aktit�tenzwecke
 */	
	
	// genutzte Aktivit�tstypen im Modell
	public static final ArrayList<Character> ACTIVITY_TYPES = new ArrayList<Character>(Arrays.asList('W', 'E', 'L', 'S', 'T'));
	// genutzte Aktivit�tstypen (mobiTopp Aktivit�ten)
	public static final ArrayList<Byte> ACTIVITY_TYPES_mobiTopp = new ArrayList<Byte>(Arrays.asList((byte)1,(byte)2,(byte)3,(byte)6,(byte)7,(byte)11,(byte)12,(byte)41,(byte)42,(byte)51,(byte)52,(byte)53,(byte)77));
	// Anzahl m�glicher Aktivit�ten
	public static final int NUMBER_OF_ACTIVITY_TYPES = ACTIVITY_TYPES.size();

	
/*
 * 		Aktivit�tendauern - Zeitklassen	
 */
		
	// Zeitklassen f�r Aktivit�tendauern - Lower Bounds
	public static final int[] ACT_TIME_TIMECLASSES_LB = { 1, 15, 30, 60, 120, 180, 240, 300, 360, 420, 480, 540, 600, 660, 720 };
	// Zeitklassen f�r Aktivit�tendauern - Upper Bounds
	public static final int[] ACT_TIME_TIMECLASSES_UB = { 14, 29, 59, 119, 179, 239, 299, 359, 419, 479, 539, 599, 659, 719, 1440 };
	// Anzahl an Zeitklassen f�r Aktivit�tendauer
	public static final int NUMBER_OF_ACT_DURATION_CLASSES = ACT_TIME_TIMECLASSES_LB.length;
	
	
/*
 * 		Heimaktivit�tendauern - Zeitklassen	
 */
				
	// Zeitklassen f�r HomeTime - Lower Bounds
	public static final int[] HOME_TIME_TIMECLASSES_LB = { 0, 15, 30, 60, 120, 180, 240, 300, 360, 420 };
	// Zeitklassen f�r HomeTime - Upper Bounds
	public static final int[] HOME_TIME_TIMECLASSES_UB = { 14, 29, 59, 119, 179, 239, 299, 359, 419, 1440 };
	// Anzahl an Zeitklassen f�r HomeTime
	public static final int NUMBER_OF_HOME_DURATION_CLASSES = HOME_TIME_TIMECLASSES_LB.length;

	
/*
 * 		Startzeiten Haupttouren - Zeitklassen	
 */	
	
	// Zeitklassen f�r Startzeiten von Haupttouren - Lower Bounds 
	public static final int[] MAIN_TOUR_START_TIMECLASSES_LB = { 0, 120, 240, 360, 420, 480, 540, 600, 660, 780, 900, 960, 1020, 1080, 1200, 1320 };
	// Zeitklassen f�r Startzeiten von Haupttouren - Upper Bounds 
	public static final int[] MAIN_TOUR_START_TIMECLASSES_UB = { 119, 239, 359, 419, 479, 539, 599, 659, 779, 899, 959, 1019, 1079, 1199, 1319, 1439 };
	// Anzahl an Zeitklassen f�r Haupttouren
	public static final int NUMBER_OF_MAIN_START_TIME_CLASSES = MAIN_TOUR_START_TIMECLASSES_LB.length;

	
/*
 * 		Startzeiten VOR-Touren - Zeitklassen	
 */		
	
	// Zeitklassen f�r Startzeiten von Vortouren - Lower Bounds 
	public static final int[] PRE_TOUR_START_TIMECLASSES_LB = { 0, 360, 420, 480, 540, 600, 660, 780, 900, 960, 1020 };
	// Zeitklassen f�r Startzeiten von Vortouren - Upper Bounds 
	public static final int[] PRE_TOUR_START_TIMECLASSES_UB = { 359, 419, 479, 539, 599, 659, 779, 899, 959, 1019, 1439 };
	// Anzahl an Zeitklassen f�r Vortouren
	public static final int NUMBER_OF_PRE_START_TIME_CLASSES = PRE_TOUR_START_TIMECLASSES_LB.length;

	
/*
 * 		Startzeiten NACH-Touren - Zeitklassen	
 */			
	
	// Zeitklassen f�r Startzeiten von Nachtouren - Lower Bounds 
	public static final int[] POST_TOUR_START_TIMECLASSES_LB = { 0, 600, 660, 780, 900, 960, 1020, 1080, 1140, 1200, 1320 };
	// Zeitklassen f�r Startzeiten von Nachtouren - Upper Bounds 
	public static final int[] POST_TOUR_START_TIMECLASSES_UB = { 599, 659, 779, 899, 959, 1019, 1079, 1139, 1199, 1319, 1439 };
	// Anzahl an Zeitklassen f�r Nachtouren
	public static final int NUMBER_OF_POST_START_TIME_CLASSES = POST_TOUR_START_TIMECLASSES_LB.length;

	
/*
 * 		Startzeiten 2.,3. Tour - Zeitklassen	
 */		
	
	// Zeitklassen f�r Startzeiten von zweiter und dritte Tour des Tages - Lower Bounds 
	public static final int[] SECTHR_TOUR_START_TIMECLASSES_LB = { 0, 540, 660, 780, 840, 900, 960, 1020, 1080, 1140, 1200};
	// Zeitklassen f�r Startzeiten von zweiter und dritte Tour des Tages - Upper Bounds 
	public static final int[] SECTHR_TOUR_START_TIMECLASSES_UB = { 539, 659, 779, 839, 899, 959, 1019, 1079, 1139, 1199, 1439};
	// Anzahl an Zeitklassen f�r zweiter und dritte Tour des Tages
	public static final int NUMBER_OF_SECTHR_START_TIME_CLASSES = SECTHR_TOUR_START_TIMECLASSES_LB.length;

/*
 * 		Wegzeitkonstante	
 */		
	
	// Konstante f�r die durchschnittliche Zeitbelegung eines Weges
	public static final int FIXED_TRIP_TIME_ESTIMATOR = 20;


/*
 * 
 * 		Folgende Einstellungen sind nur relevant, falls die Ausf�hrung direkt durch die mobiTopp Bevsynthese erfolgt
 * 
 */
  
	//ganzzahlige Prozentangabe, um die Bev�lkerunssynthese nur f�r einen bestimmten Anteil der Bev�lkerung durchzuf�hren
	public static final int percent_bevsynthese = 1;	
	
	// steuert, alle wieviele Datens�tze das Logging f�r die actiTopp Log-Dateien aktiviert wird
	public static final int anzexportds = 20000;
		
/*
 * 
 * 		Folgende Einstellungen sind nur zu Debugzwecken relevant
 * 
 */
	
	// steuert die Ausgabe von Log in Modellschritten
	public static final boolean debugenabled = false;




}
