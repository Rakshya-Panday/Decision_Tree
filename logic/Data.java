package logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Data {
	static String [][] WEATHER = {
			{"outlook",  "temperature",  "humidity",    "windy",     "play"},
			{"sunny",      "hot",        "high",       "FALSE",      "no"},
			{"sunny",      "hot",        "high",        "TRUE",      "no"},
			{"overcast",   "hot",        "high",       "FALSE",      "yes"},
			{"rainy",      "mild",       "high",       "FALSE",      "yes"},
			{"rainy",      "cool",       "normal",    "FALSE",       "yes"},
			{"rainy",      "cool",       "normal",    "TRUE",         "no"},
			{"overcast",   "cool",       "normal",    "TRUE",         "yes"},
			{"sunny",      "mild",       "high",      "FALSE",        "no"},
			{"sunny",      "cool",       "normal",    "FALSE",       "yes"},
			{"rainy",      "mild",       "normal",    "FALSE",       "yes"},
			{"sunny",      "mild",       "normal",    "TRUE",        "yes"},
			{"overcast",   "mild",       "high",      "TRUE",        "yes"},
			{"overcast",   "hot",        "normal",    "FALSE",       "yes"},
			{"rainy",      "mild",       "high",      "TRUE",         "no"}}; 
	
	static String[][] CONTACT_LENSES = {
			{"N","P","K","temperature","humidity","ph","rainfall","label"},
			{"90","42","43","20.87974371","82.00274423","6.502985292000001","202.9355362","rice"},
			{"85","58","41","21.77046169","80.31964408","7.038096361","226.6555374","rice"},
			{"60","55","44","23.00445915","82.3207629","7.840207144","263.9642476","rice"},
			{"57","73","85","18.49311205","14.72115044","7.358099622","91.94595352","chickpea"},
			{"22","64","82","19.48974337","17.17260319","6.4740245000000005","87.51312796","chickpea"},
			{"52","73","79","17.25769499","18.74943955","7.840339388999999","94.00287214","chickpea"}


			};
				
			
	
	public static Map<String,String[][]> datas = Collections.unmodifiableMap(new HashMap< String, String[][]>() {
		private static final long serialVersionUID = 1L;
	{
		put("WEATHER",WEATHER);
		put("CONTACT_LENSES",CONTACT_LENSES);
		}
	});
}
