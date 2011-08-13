/**
 * Copyright (C) 2011 David Schonert
 *
 * This file is part of BlueSky.
 *
 * BlueSky is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * BlueSky is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BlueSky.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.SyntheticCode.BlueSkyWeather;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

/**
 * @author David
 *
 * Note: Weather Underground repackages NWS data. So getting data from NWS may be faster.
 * NWS link : http://forecast.weather.gov/MapClick.php?lat=37.76834106&lon=-122.39418793&FcstType=dwml
 */
public class ForecastData {
	
	public final static int forecastShortCount = 5;
	public final static int forecastExtendedCount = 6;
	
	public static class ForecastDataObject {
		public String timeLayout;
		public ArrayList<String> data;
		
		public ForecastDataObject() {
			timeLayout = "";
			data = new ArrayList<String>();
		}
		
		public int count() {
			return data.size();
		}
		
		public void print(String title) {
			for(int i = 0; i < data.size(); i++) {
				Log.v("BlueSky", title + " [" + i + "] " + data.get(i));
			}
		}
	};
	
	public ArrayList<ForecastDataObject> periods;
	public ForecastDataObject temperatureMin;
	public ForecastDataObject temperatureMax;
	public ForecastDataObject weatherCondition;
	public ForecastDataObject conditionIcon;
	public ForecastDataObject forecastText;
	
	public ForecastData() {
		periods = new ArrayList<ForecastDataObject>();
		temperatureMin = new ForecastDataObject();
		temperatureMax = new ForecastDataObject();
		weatherCondition = new ForecastDataObject();
		conditionIcon = new ForecastDataObject();
		forecastText = new ForecastDataObject();
	}
	
	public String mapTemperatureToPeriod(int periodIndex) {
		String temperature = "";
		int minPeriod = 0;
		int maxPeriod = 0;
		// Get the name of the period we are looking for
		String periodTitle = periods.get(0).data.get(periodIndex);
		
		// Match the periods to the min and max temperatures
		for(int i = 1; i < periods.size(); i++) {
			if(periods.get(i).timeLayout.equalsIgnoreCase(temperatureMin.timeLayout)) {
				minPeriod = i;
			}
			if(periods.get(i).timeLayout.equalsIgnoreCase(temperatureMax.timeLayout)) {
				maxPeriod = i;
			}
		}
		
		// Make sure the index that is to be checked exists and a minPeriod was found
		if(((periodIndex/2) < periods.get(minPeriod).data.size()) && minPeriod != 0) {
			// Check if the periodTitle is in the minTemperature period
			if(periods.get(minPeriod).data.get(periodIndex/2).equalsIgnoreCase(periodTitle)) {
				// Title found so temperate is min
				temperature = temperatureMin.data.get(periodIndex/2);
			}
		}
		// Make sure the index that is to be checked exists and a maxPeriod was found
		if(((periodIndex/2) < periods.get(maxPeriod).data.size()) && maxPeriod != 0) {
			// Check if the periodTitle is in the maxTemperature period
			if(periods.get(maxPeriod).data.get(periodIndex/2).equalsIgnoreCase(periodTitle)) {
				// Title found so temperate is max
				temperature = temperatureMax.data.get(periodIndex/2);
			}
		}
		
		return temperature;
	}
	
	public static int convertLinkToId(String link) {
		int id;
		String icon = parseIconFromLink(link);
		
		if(nwsIconHash.containsKey(icon)) {
			id = nwsIconHash.get(icon);
		}
		else {
			id = R.drawable.ic_forecast_unknown;
		}
		
		return id;
	}
	
	private static String parseIconFromLink(String link) {
		String temp[];
		temp = link.split("/");
		
		String icon = temp[temp.length - 1];
		
		// Remove 4 characters (".jpg") from end of string
		icon = icon.substring(0, icon.length() - 4);
		
		return icon;
	}
	
	private static int convertIconToId(String icon) {
		int id;
		
		Log.v("BlueSky", "Icon = " + icon);
		
		// need to check for the key before getting the value
		if(iconHash.containsKey(icon)) {
			id = iconHash.get(icon);
		}
		else {
			Log.v("BlueSky", "Could not match " + icon + " to a forecast icon.");
			id = R.drawable.ic_forecast_unknown;
		}
		return id;
	}
	
	public static int convertIconToId(String condition, boolean night, String iconDefault) {
		int id;
		String temp[] = condition.split("\\.", 2); // split string in 2 parts on first "."
		String key = temp[0].toLowerCase();
		
		if(conditionHash.containsKey(key)) {
			String newIcon = conditionHash.get(key);
			if(night) {
				newIcon = "nt_" + newIcon;
			}
			id = convertIconToId(newIcon);
		}
		else {
			Log.v("BlueSky", "No Condition match : " + key);
			id = convertIconToId(iconDefault);
		}
		
		return id;
	}
	
	public static boolean isNight(String s) {
		// All the different strings that represent night
		String night[] = {"Tonight", "Night"};
		
		if(s.contains(night[0])) return true;
		if(s.contains(night[1])) return true;
		return false;
	}
	
	private final static HashMap<String, Integer> iconHash = new HashMap<String, Integer>();
	static {
		// All Weather Underground icon names mapped to icons
		iconHash.put("chanceflurries", R.drawable.ic_forecast_flurries);
		iconHash.put("chancerain", R.drawable.ic_forecast_rain);
		iconHash.put("chancesleet", R.drawable.ic_forecast_sleet);
		iconHash.put("chancesnow", R.drawable.ic_forecast_snow);
		iconHash.put("chancetstorms", R.drawable.ic_forecast_tstorm);
		iconHash.put("clear", R.drawable.ic_forecast_clear);
		iconHash.put("cloudy", R.drawable.ic_forecast_cloudy);
		iconHash.put("flurries", R.drawable.ic_forecast_flurries);
		iconHash.put("fog", R.drawable.ic_forecast_fog);
		iconHash.put("hail", R.drawable.ic_forecast_hail);
		iconHash.put("hazy", R.drawable.ic_forecast_hazy);
		iconHash.put("mostlycloudy", R.drawable.ic_forecast_mostlycloudy);
		iconHash.put("mostlysunny", R.drawable.ic_forecast_mostlysunny);
		iconHash.put("partlycloudy", R.drawable.ic_forecast_partlycloudy);
		iconHash.put("partlysunny", R.drawable.ic_forecast_partlysunny);
		iconHash.put("rain", R.drawable.ic_forecast_rain);
		iconHash.put("sleet", R.drawable.ic_forecast_sleet);
		iconHash.put("snow", R.drawable.ic_forecast_snow);
		iconHash.put("sunny", R.drawable.ic_forecast_clear);
		iconHash.put("tstorms", R.drawable.ic_forecast_tstorm);
		iconHash.put("tstorm", R.drawable.ic_forecast_tstorm);
		
		iconHash.put("nt_chanceflurries", R.drawable.ic_forecast_nt_flurries);
		iconHash.put("nt_chancerain", R.drawable.ic_forecast_nt_rain);
		iconHash.put("nt_chancesleet", R.drawable.ic_forecast_sleet);
		iconHash.put("nt_chancesnow", R.drawable.ic_forecast_nt_snow);
		iconHash.put("nt_chancetstorms", R.drawable.ic_forecast_nt_tstorm);
		iconHash.put("nt_clear", R.drawable.ic_forecast_nt_clear);
		iconHash.put("nt_cloudy", R.drawable.ic_forecast_nt_cloudy);
		iconHash.put("nt_flurries", R.drawable.ic_forecast_nt_flurries);
		iconHash.put("nt_fog", R.drawable.ic_forecast_nt_fog);
		iconHash.put("nt_hazy", R.drawable.ic_forecast_nt_hazy);
		iconHash.put("nt_hail", R.drawable.ic_forecast_hail);
		iconHash.put("nt_mostlycloudy", R.drawable.ic_forecast_nt_mostlycloudy);
		//iconHash.put("nt_mostlysunny", R.drawable.ic_forecast_nt_mostlysunny);
		iconHash.put("nt_partlycloudy", R.drawable.ic_forecast_nt_partlycloudy);
		//iconHash.put("nt_partlysunny", R.drawable.ic_forecast_nt_partlysunny);
		iconHash.put("nt_rain", R.drawable.ic_forecast_nt_rain);
		iconHash.put("nt_sleet", R.drawable.ic_forecast_sleet);
		iconHash.put("nt_snow", R.drawable.ic_forecast_nt_snow);
		iconHash.put("nt_sunny", R.drawable.ic_forecast_nt_clear);
		iconHash.put("nt_tstorms", R.drawable.ic_forecast_nt_tstorm);
		iconHash.put("nt_tstorm", R.drawable.ic_forecast_nt_tstorm);
		
		iconHash.put("unknown", R.drawable.ic_forecast_unknown);
	}
	
	// Convert Condition string to icon string ("Condition to Icon.xlsx")
	private final static HashMap<String, String> conditionHash = new HashMap<String, String>();
	static {
		// All NWS conditions from http://www.weather.gov/xml/current_obs/weather.php
		conditionHash.put("Mostly Cloudy".toLowerCase(), "mostlycloudy");
		conditionHash.put("Mostly Cloudy with Haze".toLowerCase(), "mostlycloudy");
		conditionHash.put("Mostly Cloudy and Breezy".toLowerCase(), "mostlycloudy");
			
		conditionHash.put("Fair".toLowerCase(), "mostlysunny");
		conditionHash.put("Clear".toLowerCase(), "clear");
		conditionHash.put("Fair with Haze".toLowerCase(), "mostlysunny");
		conditionHash.put("Clear with Haze".toLowerCase(), "clear");
		conditionHash.put("Fair and Breezy".toLowerCase(), "mostlysunny");
		conditionHash.put("Clear and Breezy".toLowerCase(), "clear");
			
		conditionHash.put("A Few Clouds".toLowerCase(), "partlysunny");
		conditionHash.put("A Few Clouds with Haze".toLowerCase(), "partlysunny");
		conditionHash.put("A Few Clouds and Breezy".toLowerCase(), "partlysunny");

		conditionHash.put("Partly Cloudy".toLowerCase(), "partlycloudy");
		conditionHash.put("Partly Cloudy with Haze".toLowerCase(), "partlycloudy");
		conditionHash.put("Partly Cloudy and Breezy".toLowerCase(), "partlycloudy");

		conditionHash.put("Overcast".toLowerCase(), "cloudy");
		conditionHash.put("Overcast with Haze".toLowerCase(), "cloudy");
		conditionHash.put("Overcast and Breezy".toLowerCase(), "cloudy");

		conditionHash.put("Fog/Mist".toLowerCase(), "fog");
		conditionHash.put("Fog".toLowerCase(), "fog");
		conditionHash.put("Freezing Fog".toLowerCase(), "fog");
		conditionHash.put("Shallow Fog".toLowerCase(), "fog");
		conditionHash.put("Partial Fog".toLowerCase(), "fog");
		conditionHash.put("Patches of Fog".toLowerCase(), "fog");
		conditionHash.put("Fog in Vicinity".toLowerCase(), "fog");
		conditionHash.put("Freezing Fog in Vicinity".toLowerCase(), "fog");
		conditionHash.put("Shallow Fog in Vicinity".toLowerCase(), "fog");
		conditionHash.put("Partial Fog in Vicinity".toLowerCase(), "fog");
		conditionHash.put("Patches of Fog in Vicinity".toLowerCase(), "fog");
		conditionHash.put("Showers in Vicinity Fog".toLowerCase(), "fog");
		conditionHash.put("Light Freezing Fog".toLowerCase(), "fog");
		conditionHash.put("Heavy Freezing Fog".toLowerCase(), "fog");

		conditionHash.put("Smoke".toLowerCase(), "hazy");

		conditionHash.put("Freezing Rain".toLowerCase(), "sleet");
		conditionHash.put("Freezing Drizzle".toLowerCase(), "sleet");
		conditionHash.put("Light Freezing Rain".toLowerCase(), "sleet");
		conditionHash.put("Light Freezing Drizzle".toLowerCase(), "sleet");
		conditionHash.put("Heavy Freezing Rain".toLowerCase(), "sleet");
		conditionHash.put("Heavy Freezing Drizzle".toLowerCase(), "sleet");
		conditionHash.put("Freezing Rain in Vicinity".toLowerCase(), "sleet");
		conditionHash.put("Freezing Drizzle in Vicinity".toLowerCase(), "sleet");

		conditionHash.put("Ice Pellets".toLowerCase(), "hail");
		conditionHash.put("Light Ice Pellets".toLowerCase(), "hail");
		conditionHash.put("Heavy Ice Pellets".toLowerCase(), "hail");
		conditionHash.put("Ice Pellets in Vicinity".toLowerCase(), "hail");
		conditionHash.put("Showers Ice Pellets".toLowerCase(), "hail");
		conditionHash.put("Thunderstorm Ice Pellets".toLowerCase(), "hail");
		conditionHash.put("Ice Crystals".toLowerCase(), "hail");
		conditionHash.put("Hail".toLowerCase(), "hail");
		conditionHash.put("Small Hail/Snow Pellets".toLowerCase(), "hail");
		conditionHash.put("Light Small Hail/Snow Pellets".toLowerCase(), "hail");
		conditionHash.put("Heavy small Hail/Snow Pellets".toLowerCase(), "hail");
		conditionHash.put("Showers Hail".toLowerCase(), "hail");
		conditionHash.put("Hail Showers".toLowerCase(), "hail");

		conditionHash.put("Freezing Rain Snow".toLowerCase(), "sleet");
		conditionHash.put("Light Freezing Rain Snow".toLowerCase(), "sleet");
		conditionHash.put("Heavy Freezing Rain Snow".toLowerCase(), "sleet");
		conditionHash.put("Freezing Drizzle Snow".toLowerCase(), "sleet");
		conditionHash.put("Light Freezing Drizzle Snow".toLowerCase(), "sleet");
		conditionHash.put("Heavy Freezing Drizzle Snow".toLowerCase(), "sleet");
		conditionHash.put("Snow Freezing Rain".toLowerCase(), "sleet");
		conditionHash.put("Light Snow Freezing Rain".toLowerCase(), "sleet");
		conditionHash.put("Heavy Snow Freezing Rain".toLowerCase(), "sleet");
		conditionHash.put("Snow Freezing Drizzle".toLowerCase(), "sleet");
		conditionHash.put("Light Snow Freezing Drizzle".toLowerCase(), "sleet");
		conditionHash.put("Heavy Snow Freezing Drizzle".toLowerCase(), "sleet");

		conditionHash.put("Rain Ice Pellets".toLowerCase(), "hail");
		conditionHash.put("Light Rain Ice Pellets".toLowerCase(), "hail");
		conditionHash.put("Heavy Rain Ice Pellets".toLowerCase(), "hail");
		conditionHash.put("Drizzle Ice Pellets".toLowerCase(), "hail");
		conditionHash.put("Light Drizzle Ice Pellets".toLowerCase(), "hail");
		conditionHash.put("Heavy Drizzle Ice Pellets".toLowerCase(), "hail");
		conditionHash.put("Ice Pellets Rain".toLowerCase(), "hail");
		conditionHash.put("Light Ice Pellets Rain".toLowerCase(), "hail");
		conditionHash.put("Heavy Ice Pellets Rain".toLowerCase(), "hail");
		conditionHash.put("Ice Pellets Drizzle".toLowerCase(), "hail");
		conditionHash.put("Light Ice Pellets Drizzle".toLowerCase(), "hail");
		conditionHash.put("Heavy Ice Pellets Drizzle".toLowerCase(), "hail");

		conditionHash.put("Rain Snow".toLowerCase(), "sleet");
		conditionHash.put("Light Rain Snow".toLowerCase(), "sleet");
		conditionHash.put("Heavy Rain Snow".toLowerCase(), "sleet");
		conditionHash.put("Snow Rain".toLowerCase(), "sleet");
		conditionHash.put("Light Snow Rain".toLowerCase(), "sleet");
		conditionHash.put("Heavy Snow Rain".toLowerCase(), "sleet");
		conditionHash.put("Drizzle Snow".toLowerCase(), "sleet");
		conditionHash.put("Light Drizzle Snow".toLowerCase(), "sleet");
		conditionHash.put("Heavy Drizzle Snow".toLowerCase(), "sleet");
		conditionHash.put("Snow Drizzle".toLowerCase(), "sleet");
		conditionHash.put("Light Snow Drizzle".toLowerCase(), "sleet");
		conditionHash.put("Heavy Drizzle Snow".toLowerCase(), "sleet");

		conditionHash.put("Rain Showers".toLowerCase(), "rain");
		conditionHash.put("Light Rain Showers".toLowerCase(), "rain");
		conditionHash.put("Light Rain and Breezy".toLowerCase(), "rain");
		conditionHash.put("Heavy Rain Showers".toLowerCase(), "rain");
		conditionHash.put("Rain Showers in Vicinity".toLowerCase(), "rain");
		conditionHash.put("Light Showers Rain".toLowerCase(), "rain");
		conditionHash.put("Heavy Showers Rain".toLowerCase(), "rain");
		conditionHash.put("Showers Rain".toLowerCase(), "rain");
		conditionHash.put("Showers Rain in Vicinity".toLowerCase(), "rain");
		conditionHash.put("Rain Showers Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Light Rain Showers Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Heavy Rain Showers Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Rain Showers in Vicinity Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Light Showers Rain Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Heavy Showers Rain Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Showers Rain Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Showers Rain in Vicinity Fog/Mist".toLowerCase(), "rain");

		conditionHash.put("Thunderstorm".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Rain".toLowerCase(), "tstorm");
		conditionHash.put("Light Thunderstorm Rain".toLowerCase(), "tstorm");
		conditionHash.put("Heavy Thunderstorm Rain".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Rain Fog/Mist".toLowerCase(), "tstorm");
		conditionHash.put("Light Thunderstorm Rain Fog/Mist".toLowerCase(), "tstorm");
		conditionHash.put("Heavy Thunderstorm Rain Fog and Windy".toLowerCase(), "tstorm");
		conditionHash.put("Heavy Thunderstorm Rain Fog/Mist".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Showers in Vicinity".toLowerCase(), "tstorm");
		conditionHash.put("Light Thunderstorm Rain Haze".toLowerCase(), "tstorm");
		conditionHash.put("Heavy Thunderstorm Rain Haze".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Fog".toLowerCase(), "tstorm");
		conditionHash.put("Light Thunderstorm Rain Fog".toLowerCase(), "tstorm");
		conditionHash.put("Heavy Thunderstorm Rain Fog".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Light Rain".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Heavy Rain".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Rain Fog/Mist".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Light Rain Fog/Mist".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Heavy Rain Fog/Mist".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm in Vicinity Fog/Mist".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Showers in Vicinity".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm in Vicinity Haze".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Haze in Vicinity".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Light Rain Haze".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Heavy Rain Haze".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Fog".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Light Rain Fog".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Heavy Rain Fog".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Hail".toLowerCase(), "tstorm");
		conditionHash.put("Light Thunderstorm Rain Hail".toLowerCase(), "tstorm");
		conditionHash.put("Heavy Thunderstorm Rain Hail".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Rain Hail Fog/Mist".toLowerCase(), "tstorm");
		conditionHash.put("Light Thunderstorm Rain Hail Fog/Mist".toLowerCase(), "tstorm");
		conditionHash.put("Heavy Thunderstorm Rain Hail Fog/Hail".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Showers in Vicinity Hail".toLowerCase(), "tstorm");
		conditionHash.put("Light Thunderstorm Rain Hail Haze".toLowerCase(), "tstorm");
		conditionHash.put("Heavy Thunderstorm Rain Hail Haze".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Hail Fog".toLowerCase(), "tstorm");
		conditionHash.put("Light Thunderstorm Rain Hail Fog".toLowerCase(), "tstorm");
		conditionHash.put("Heavy Thunderstorm Rain Hail Fog".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Light Rain Hail".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Heavy Rain Hail".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Rain Hail Fog/Mist".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Light Rain Hail Fog/Mist".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Heavy Rain Hail Fog/Mist".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm in Vicinity Hail".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm in Vicinity Hail Haze".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Haze in Vicinity Hail".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Light Rain Hail Haze".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Heavy Rain Hail Haze".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Hail Fog".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Light Rain Hail Fog".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Heavy Rain Hail Fog".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Small Hail/Snow Pellets".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm Rain Small Hail/Snow Pellets".toLowerCase(), "tstorm");
		conditionHash.put("Light Thunderstorm Rain Small Hail/Snow Pellets".toLowerCase(), "tstorm");
		conditionHash.put("Heavy Thunderstorm Rain Small Hail/Snow Pellets".toLowerCase(), "tstorm");

		conditionHash.put("Snow".toLowerCase(), "snow");
		conditionHash.put("Light Snow".toLowerCase(), "snow");
		conditionHash.put("Heavy Snow".toLowerCase(), "snow");
		conditionHash.put("Snow Showers".toLowerCase(), "snow");
		conditionHash.put("Light Snow Showers".toLowerCase(), "snow");
		conditionHash.put("Heavy Snow Showers".toLowerCase(), "snow");
		conditionHash.put("Showers Snow".toLowerCase(), "snow");
		conditionHash.put("Light Showers Snow".toLowerCase(), "snow");
		conditionHash.put("Heavy Showers Snow".toLowerCase(), "snow");
		conditionHash.put("Snow Fog/Mist".toLowerCase(), "snow");
		conditionHash.put("Light Snow Fog/Mist".toLowerCase(), "snow");
		conditionHash.put("Heavy Snow Fog/Mist".toLowerCase(), "snow");
		conditionHash.put("Snow Showers Fog/Mist".toLowerCase(), "snow");
		conditionHash.put("Light Snow Showers Fog/Mist".toLowerCase(), "snow");
		conditionHash.put("Heavy Snow Showers Fog/Mist".toLowerCase(), "snow");
		conditionHash.put("Showers Snow Fog/Mist".toLowerCase(), "snow");
		conditionHash.put("Light Showers Snow Fog/Mist".toLowerCase(), "snow");
		conditionHash.put("Heavy Showers Snow Fog/Mist".toLowerCase(), "snow");
		conditionHash.put("Snow Fog".toLowerCase(), "snow");
		conditionHash.put("Light Snow Fog".toLowerCase(), "snow");
		conditionHash.put("Heavy Snow Fog".toLowerCase(), "snow");
		conditionHash.put("Snow Showers Fog".toLowerCase(), "snow");
		conditionHash.put("Light Snow Showers Fog".toLowerCase(), "snow");
		conditionHash.put("Heavy Snow Showers Fog".toLowerCase(), "snow");
		conditionHash.put("Showers Snow Fog".toLowerCase(), "snow");
		conditionHash.put("Light Showers Snow Fog".toLowerCase(), "snow");
		conditionHash.put("Heavy Showers Snow Fog".toLowerCase(), "snow");
		conditionHash.put("Showers in Vicinity Snow".toLowerCase(), "snow");
		conditionHash.put("Snow Showers in Vicinity".toLowerCase(), "snow");
		conditionHash.put("Snow Showers in Vicinity Fog/Mist".toLowerCase(), "snow");
		conditionHash.put("Snow Showers in Vicinity Fog".toLowerCase(), "snow");
		conditionHash.put("Low Drifting Snow".toLowerCase(), "snow");
		conditionHash.put("Blowing Snow".toLowerCase(), "snow");
		conditionHash.put("Snow Low Drifting Snow".toLowerCase(), "snow");
		conditionHash.put("Snow Blowing Snow".toLowerCase(), "snow");
		conditionHash.put("Light Snow Low Drifting Snow".toLowerCase(), "snow");
		conditionHash.put("Light Snow Blowing Snow".toLowerCase(), "snow");
		conditionHash.put("Light Snow Blowing Snow Fog/Mist".toLowerCase(), "snow");
		conditionHash.put("Heavy Snow Low Drifting Snow".toLowerCase(), "snow");
		conditionHash.put("Heavy Snow Blowing Snow".toLowerCase(), "snow");
		conditionHash.put("Thunderstorm Snow".toLowerCase(), "snow");
		conditionHash.put("Light Thunderstorm Snow".toLowerCase(), "snow");
		conditionHash.put("Heavy Thunderstorm Snow".toLowerCase(), "snow");
		conditionHash.put("Snow Grains".toLowerCase(), "snow");
		conditionHash.put("Light Snow Grains".toLowerCase(), "snow");
		conditionHash.put("Heavy Snow Grains".toLowerCase(), "snow");
		conditionHash.put("Heavy Blowing Snow".toLowerCase(), "snow");
		conditionHash.put("Blowing Snow in Vicinity".toLowerCase(), "snow");

		conditionHash.put("Windy".toLowerCase(), "clear");
		conditionHash.put("Breezy".toLowerCase(), "clear");
		conditionHash.put("Fair and Windy".toLowerCase(), "mostlysunny");
		conditionHash.put("A Few Clouds and Windy".toLowerCase(), "partlysunny");
		conditionHash.put("Partly Cloudy and Windy".toLowerCase(), "partlycloudy");
		conditionHash.put("Mostly Cloudy and Windy".toLowerCase(), "mostlycloudy");
		conditionHash.put("Overcast and Windy".toLowerCase(), "cloudy");

		conditionHash.put("Showers in Vicinity".toLowerCase(), "rain");
		conditionHash.put("Showers in Vicinity Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Showers in Vicinity Fog".toLowerCase(), "rain");
		conditionHash.put("Showers in Vicinity Haze".toLowerCase(), "rain");

		conditionHash.put("Freezing Rain Rain".toLowerCase(), "sleet");
		conditionHash.put("Light Freezing Rain Rain".toLowerCase(), "sleet");
		conditionHash.put("Heavy Freezing Rain Rain".toLowerCase(), "sleet");
		conditionHash.put("Rain Freezing Rain".toLowerCase(), "sleet");
		conditionHash.put("Light Rain Freezing Rain".toLowerCase(), "sleet");
		conditionHash.put("Heavy Rain Freezing Rain".toLowerCase(), "sleet");
		conditionHash.put("Freezing Drizzle Rain".toLowerCase(), "sleet");
		conditionHash.put("Light Freezing Drizzle Rain".toLowerCase(), "sleet");
		conditionHash.put("Heavy Freezing Drizzle Rain".toLowerCase(), "sleet");
		conditionHash.put("Rain Freezing Drizzle".toLowerCase(), "sleet");
		conditionHash.put("Light Rain Freezing Drizzle".toLowerCase(), "sleet");
		conditionHash.put("Heavy Rain Freezing Drizzle".toLowerCase(), "sleet");

		conditionHash.put("Thunderstorm in Vicinity".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm in Vicinity Fog".toLowerCase(), "tstorm");
		conditionHash.put("Thunderstorm in Vicinity Haze".toLowerCase(), "tstorm");

		conditionHash.put("Light Rain".toLowerCase(), "rain");
		conditionHash.put("Drizzle".toLowerCase(), "rain");
		conditionHash.put("Light Drizzle".toLowerCase(), "rain");
		conditionHash.put("Heavy Drizzle".toLowerCase(), "rain");
		conditionHash.put("Light Rain Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Drizzle Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Light Drizzle Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Heavy Drizzle Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Light Rain Fog".toLowerCase(), "rain");
		conditionHash.put("Drizzle Fog".toLowerCase(), "rain");
		conditionHash.put("Light Drizzle Fog".toLowerCase(), "rain");
		conditionHash.put("Heavy Drizzle Fog".toLowerCase(), "rain");

		conditionHash.put("Rain".toLowerCase(), "rain");
		conditionHash.put("Heavy Rain".toLowerCase(), "rain");
		conditionHash.put("Rain Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Heavy Rain Fog/Mist".toLowerCase(), "rain");
		conditionHash.put("Rain Fog".toLowerCase(), "rain");
		conditionHash.put("Heavy Rain Fog".toLowerCase(), "rain");

		conditionHash.put("Funnel Cloud".toLowerCase(), "tstorm");
		conditionHash.put("Funnel Cloud in Vicinity".toLowerCase(), "tstorm");
		conditionHash.put("Tornado/Water Spout".toLowerCase(), "tstorm");

		conditionHash.put("Dust".toLowerCase(), "hazy");
		conditionHash.put("Low Drifting Dust".toLowerCase(), "hazy");
		conditionHash.put("Blowing Dust".toLowerCase(), "hazy");
		conditionHash.put("Sand".toLowerCase(), "hazy");
		conditionHash.put("Blowing Sand".toLowerCase(), "hazy");
		conditionHash.put("Low Drifting Sand".toLowerCase(), "hazy");
		conditionHash.put("Dust/Sand Whirls".toLowerCase(), "hazy");
		conditionHash.put("Dust/Sand Whirls in Vicinity".toLowerCase(), "hazy");
		conditionHash.put("Dust Storm".toLowerCase(), "hazy");
		conditionHash.put("Heavy Dust Storm".toLowerCase(), "hazy");
		conditionHash.put("Dust Storm in Vicinity".toLowerCase(), "hazy");
		conditionHash.put("Sand Storm".toLowerCase(), "hazy");
		conditionHash.put("Heavy Sand Storm".toLowerCase(), "hazy");
		conditionHash.put("Sand Storm in Vicinity".toLowerCase(), "hazy");

		conditionHash.put("Haze".toLowerCase(), "hazy");
		
		conditionHash.put("Mostly Sunny".toLowerCase(), "mostlysunny"); // not in list on NWS
	}
	
	// Convert NWS icon name to icon string
	private final static HashMap<String, Integer> nwsIconHash = new HashMap<String, Integer>();
	static {
		// All NWS conditions from http://www.weather.gov/xml/current_obs/weather.php
		nwsIconHash.put("bkn", R.drawable.ic_forecast_mostlycloudy);
		
		nwsIconHash.put("blizzard", R.drawable.ic_forecast_snow);
		nwsIconHash.put("blizzard10", R.drawable.ic_forecast_snow);
		nwsIconHash.put("blizzard100", R.drawable.ic_forecast_snow);
		nwsIconHash.put("blizzard20", R.drawable.ic_forecast_snow);
		nwsIconHash.put("blizzard30", R.drawable.ic_forecast_snow);
		nwsIconHash.put("blizzard40", R.drawable.ic_forecast_snow);
		nwsIconHash.put("blizzard50", R.drawable.ic_forecast_snow);
		nwsIconHash.put("blizzard60", R.drawable.ic_forecast_snow);
		nwsIconHash.put("blizzard70", R.drawable.ic_forecast_snow);
		nwsIconHash.put("blizzard80", R.drawable.ic_forecast_snow);
		nwsIconHash.put("blizzard90", R.drawable.ic_forecast_snow);
		
		//nwsIconHash.put("br", R.drawable.);
		
		//nwsIconHash.put("cold", R.drawable.);
		
		nwsIconHash.put("du", R.drawable.ic_forecast_nt_hazy);
		nwsIconHash.put("dust", R.drawable.ic_forecast_nt_hazy);
		
		nwsIconHash.put("few", R.drawable.ic_forecast_mostlysunny);
		
		nwsIconHash.put("fg", R.drawable.ic_forecast_fog);
		
		nwsIconHash.put("fu", R.drawable.ic_forecast_nt_hazy);
		
		nwsIconHash.put("fzra", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("fzra10", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("fzra100", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("fzra20", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("fzra30", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("fzra40", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("fzra50", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("fzra60", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("fzra70", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("fzra80", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("fzra90", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("fzrara", R.drawable.ic_forecast_sleet);
		
		nwsIconHash.put("hazy", R.drawable.ic_forecast_hazy);
		
		nwsIconHash.put("hi_bkn", R.drawable.ic_forecast_mostlycloudy);
		
		nwsIconHash.put("hi_few", R.drawable.ic_forecast_partlycloudy);
		
		nwsIconHash.put("hi_nbkn", R.drawable.ic_forecast_nt_mostlycloudy);
		
		nwsIconHash.put("hi_nfew", R.drawable.ic_forecast_nt_partlycloudy);
		nwsIconHash.put("hi_nsct", R.drawable.ic_forecast_nt_partlycloudy);
		
		nwsIconHash.put("hi_nshwrs", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("hi_nshwrs10", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("hi_nshwrs100", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("hi_nshwrs20", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("hi_nshwrs30", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("hi_nshwrs40", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("hi_nshwrs50", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("hi_nshwrs60", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("hi_nshwrs70", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("hi_nshwrs80", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("hi_nshwrs90", R.drawable.ic_forecast_nt_rain);
		
		nwsIconHash.put("hi_nskc", R.drawable.ic_forecast_nt_clear);
		
		nwsIconHash.put("hi_ntsra", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("hi_ntsra10", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("hi_ntsra100", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("hi_ntsra20", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("hi_ntsra30", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("hi_ntsra40", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("hi_ntsra50", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("hi_ntsra60", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("hi_ntsra70", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("hi_ntsra80", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("hi_ntsra90", R.drawable.ic_forecast_nt_tstorm);
		
		nwsIconHash.put("hi_sct", R.drawable.ic_forecast_partlycloudy);
		
		nwsIconHash.put("hi_shwrs", R.drawable.ic_forecast_rain);
		nwsIconHash.put("hi_shwrs10", R.drawable.ic_forecast_rain);
		nwsIconHash.put("hi_shwrs100", R.drawable.ic_forecast_rain);
		nwsIconHash.put("hi_shwrs20", R.drawable.ic_forecast_rain);
		nwsIconHash.put("hi_shwrs30", R.drawable.ic_forecast_rain);
		nwsIconHash.put("hi_shwrs40", R.drawable.ic_forecast_rain);
		nwsIconHash.put("hi_shwrs50", R.drawable.ic_forecast_rain);
		nwsIconHash.put("hi_shwrs60", R.drawable.ic_forecast_rain);
		nwsIconHash.put("hi_shwrs70", R.drawable.ic_forecast_rain);
		nwsIconHash.put("hi_shwrs80", R.drawable.ic_forecast_rain);
		nwsIconHash.put("hi_shwrs90", R.drawable.ic_forecast_rain);
		
		nwsIconHash.put("hi_skc", R.drawable.ic_forecast_clear);
		
		nwsIconHash.put("hi_tsra", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("hi_tsra10", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("hi_tsra100", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("hi_tsra20", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("hi_tsra30", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("hi_tsra40", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("hi_tsra50", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("hi_tsra60", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("hi_tsra70", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("hi_tsra80", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("hi_tsra90", R.drawable.ic_forecast_tstorm);
		
		nwsIconHash.put("hot", R.drawable.ic_forecast_clear);
		
		nwsIconHash.put("ip", R.drawable.ic_forecast_hail);
		nwsIconHash.put("ip10", R.drawable.ic_forecast_hail);
		nwsIconHash.put("ip100", R.drawable.ic_forecast_hail);
		nwsIconHash.put("ip20", R.drawable.ic_forecast_hail);
		nwsIconHash.put("ip30", R.drawable.ic_forecast_hail);
		nwsIconHash.put("ip40", R.drawable.ic_forecast_hail);
		nwsIconHash.put("ip50", R.drawable.ic_forecast_hail);
		nwsIconHash.put("ip60", R.drawable.ic_forecast_hail);
		nwsIconHash.put("ip70", R.drawable.ic_forecast_hail);
		nwsIconHash.put("ip80", R.drawable.ic_forecast_hail);
		nwsIconHash.put("ip90", R.drawable.ic_forecast_hail);
		
		nwsIconHash.put("mist", R.drawable.ic_forecast_fog);
		
		nwsIconHash.put("mix", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("mix10", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("mix100", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("mix20", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("mix30", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("mix40", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("mix50", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("mix60", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("mix70", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("mix80", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("mix90", R.drawable.ic_forecast_sleet);
		
		nwsIconHash.put("nbkn", R.drawable.ic_forecast_nt_mostlycloudy);
		
		nwsIconHash.put("nfew", R.drawable.ic_forecast_nt_partlycloudy);
		
		nwsIconHash.put("nfg", R.drawable.ic_forecast_nt_fog);
		
		nwsIconHash.put("nmix", R.drawable.ic_forecast_nt_snow);
		
		nwsIconHash.put("novc", R.drawable.ic_forecast_nt_mostlycloudy);
		
		nwsIconHash.put("nra", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nra10", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nra100", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nra20", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nra30", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nra40", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nra50", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nra60", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nra70", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nra80", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nra90", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nraip", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nraip10", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nraip100", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nraip20", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nraip30", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nraip40", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nraip50", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nraip60", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nraip70", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nraip80", R.drawable.ic_forecast_nt_rain);
		nwsIconHash.put("nraip90", R.drawable.ic_forecast_nt_rain);
		
		nwsIconHash.put("nrasn", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nrasn10", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nrasn100", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nrasn20", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nrasn30", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nrasn40", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nrasn50", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nrasn60", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nrasn70", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nrasn80", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nrasn90", R.drawable.ic_forecast_nt_snow);
		
		nwsIconHash.put("nsct", R.drawable.ic_forecast_nt_partlycloudy);
		
		nwsIconHash.put("nscttsra", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("nscttsra10", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("nscttsra100", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("nscttsra20", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("nscttsra30", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("nscttsra40", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("nscttsra50", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("nscttsra60", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("nscttsra70", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("nscttsra80", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("nscttsra90", R.drawable.ic_forecast_nt_tstorm);
		
		nwsIconHash.put("nskc", R.drawable.ic_forecast_nt_clear);
		
		nwsIconHash.put("nsn", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nsn10", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nsn100", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nsn20", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nsn30", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nsn40", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nsn50", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nsn60", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nsn70", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nsn80", R.drawable.ic_forecast_nt_snow);
		nwsIconHash.put("nsn90", R.drawable.ic_forecast_nt_snow);
		
		nwsIconHash.put("nsvrtsra", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("ntsra", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("ntsra10", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("ntsra100", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("ntsra20", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("ntsra30", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("ntsra40", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("ntsra50", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("ntsra60", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("ntsra70", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("ntsra80", R.drawable.ic_forecast_nt_tstorm);
		nwsIconHash.put("ntsra90", R.drawable.ic_forecast_nt_tstorm);
		
		//nwsIconHash.put("nwind", R.drawable.);
		
		nwsIconHash.put("ovc", R.drawable.ic_forecast_cloudy);
		
		nwsIconHash.put("pcloudy", R.drawable.ic_forecast_partlycloudy);
		
		nwsIconHash.put("ra", R.drawable.ic_forecast_rain);
		nwsIconHash.put("ra1", R.drawable.ic_forecast_rain);
		nwsIconHash.put("ra10", R.drawable.ic_forecast_rain);
		nwsIconHash.put("ra100", R.drawable.ic_forecast_rain);
		nwsIconHash.put("ra20", R.drawable.ic_forecast_rain);
		nwsIconHash.put("ra30", R.drawable.ic_forecast_rain);
		nwsIconHash.put("ra40", R.drawable.ic_forecast_rain);
		nwsIconHash.put("ra50", R.drawable.ic_forecast_rain);
		nwsIconHash.put("ra60", R.drawable.ic_forecast_rain);
		nwsIconHash.put("ra70", R.drawable.ic_forecast_rain);
		nwsIconHash.put("ra80", R.drawable.ic_forecast_rain);
		nwsIconHash.put("ra90", R.drawable.ic_forecast_rain);
		
		nwsIconHash.put("raip", R.drawable.ic_forecast_hail);
		nwsIconHash.put("raip10", R.drawable.ic_forecast_hail);
		nwsIconHash.put("raip100", R.drawable.ic_forecast_hail);
		nwsIconHash.put("raip20", R.drawable.ic_forecast_hail);
		nwsIconHash.put("raip30", R.drawable.ic_forecast_hail);
		nwsIconHash.put("raip40", R.drawable.ic_forecast_hail);
		nwsIconHash.put("raip50", R.drawable.ic_forecast_hail);
		nwsIconHash.put("raip60", R.drawable.ic_forecast_hail);
		nwsIconHash.put("raip70", R.drawable.ic_forecast_hail);
		nwsIconHash.put("raip80", R.drawable.ic_forecast_hail);
		nwsIconHash.put("raip90", R.drawable.ic_forecast_hail);
		
		nwsIconHash.put("rasn", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("rasn10", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("rasn100", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("rasn20", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("rasn30", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("rasn40", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("rasn50", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("rasn60", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("rasn70", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("rasn80", R.drawable.ic_forecast_sleet);
		nwsIconHash.put("rasn90", R.drawable.ic_forecast_sleet);
		
		nwsIconHash.put("sct", R.drawable.ic_forecast_nt_partlycloudy);
		
		nwsIconHash.put("scttsra", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("scttsra10", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("scttsra100", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("scttsra20", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("scttsra30", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("scttsra40", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("scttsra50", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("scttsra60", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("scttsra70", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("scttsra80", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("scttsra90", R.drawable.ic_forecast_tstorm);
		
		nwsIconHash.put("shra", R.drawable.ic_forecast_rain);
		nwsIconHash.put("shra10", R.drawable.ic_forecast_rain);
		nwsIconHash.put("shra100", R.drawable.ic_forecast_rain);
		nwsIconHash.put("shra2", R.drawable.ic_forecast_rain);
		nwsIconHash.put("shra20", R.drawable.ic_forecast_rain);
		nwsIconHash.put("shra30", R.drawable.ic_forecast_rain);
		nwsIconHash.put("shra40", R.drawable.ic_forecast_rain);
		nwsIconHash.put("shra50", R.drawable.ic_forecast_rain);
		nwsIconHash.put("shra60", R.drawable.ic_forecast_rain);
		nwsIconHash.put("shra70", R.drawable.ic_forecast_rain);
		nwsIconHash.put("shra80", R.drawable.ic_forecast_rain);
		nwsIconHash.put("shra90", R.drawable.ic_forecast_rain);
		
		nwsIconHash.put("skc", R.drawable.ic_forecast_clear);
		
		nwsIconHash.put("smoke", R.drawable.ic_forecast_hazy);
		
		nwsIconHash.put("sn", R.drawable.ic_forecast_snow);
		nwsIconHash.put("sn10", R.drawable.ic_forecast_snow);
		nwsIconHash.put("sn100", R.drawable.ic_forecast_snow);
		nwsIconHash.put("sn20", R.drawable.ic_forecast_snow);
		nwsIconHash.put("sn30", R.drawable.ic_forecast_snow);
		nwsIconHash.put("sn40", R.drawable.ic_forecast_snow);
		nwsIconHash.put("sn50", R.drawable.ic_forecast_snow);
		nwsIconHash.put("sn60", R.drawable.ic_forecast_snow);
		nwsIconHash.put("sn70", R.drawable.ic_forecast_snow);
		nwsIconHash.put("sn80", R.drawable.ic_forecast_snow);
		nwsIconHash.put("sn90", R.drawable.ic_forecast_snow);
		
		nwsIconHash.put("tcu", R.drawable.ic_forecast_cloudy);
		
		nwsIconHash.put("tsra", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("tsra10", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("tsra100", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("tsra20", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("tsra30", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("tsra40", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("tsra50", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("tsra60", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("tsra70", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("tsra80", R.drawable.ic_forecast_tstorm);
		nwsIconHash.put("tsra90", R.drawable.ic_forecast_tstorm);
		
		//nwsIconHash.put("wind", R.drawable.);
	}
}
