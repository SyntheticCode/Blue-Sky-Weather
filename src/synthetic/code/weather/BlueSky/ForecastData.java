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
package synthetic.code.weather.BlueSky;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

/**
 * @author David
 *
 * Note: Weather Underground repackages NWS data. So getting data from NWS may be faster.
 */
public class ForecastData {
	
	public final static int forecastShortCount = 5;
	public final static int forecastExtendedCount = 6;
	
	public ArrayList<ForecastDayShort> forecastShort;
	public ArrayList<ForecastDayExtended> forecastExtended;
	
	public ForecastData() {
		forecastShort = new ArrayList<ForecastDayShort>(forecastShortCount);
		forecastExtended = new ArrayList<ForecastDayExtended>(forecastExtendedCount);
		
	}
	
	private static int convertIconToId(String icon) {
		int id;
//		// If the icon has "nt_" (night) prefix then remove it
//		if(icon.contains("nt_")) {
//			icon = icon.substring(3);
//		}
		
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
	
	private static int convertIconToId(String condition, boolean night, String icon) {
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
			id = convertIconToId(icon);
		}
		
		return id;
	}
	
	private static boolean isNight(String s) {
		// All the different strings that represent night
		String night[] = {"Tonight", "Night"};
		
		if(s.contains(night[0])) return true;
		if(s.contains(night[1])) return true;
		return false;
	}
	
	public class ForecastDayShort implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2560286931395495389L;
		
		private String title;
		private String icon;
		private String forecast;
		public void setTitle(String title) {
			this.title = title.trim();
		}
		public String getTitle() {
			return title;
		}
		public void setIcon(String icon) {
			this.icon = icon.trim();
		}
		public String getIcon() {
			return icon;
		}
		public int getIconId() {
			return ForecastData.convertIconToId(this.forecast, ForecastData.isNight(this.title), this.icon);
		}
		public void setForecast(String forecast) {
			this.forecast = forecast.trim();
		}
		public String getForecast() {
			return forecast;
		}
	}
	
	public class ForecastDayExtended implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4761682674571058997L;
		
		private String dateDay;
		private String dateMonth;
		private String dateWeekday;
		private String high_F;
		private String high_C;
		private String low_F;
		private String low_C;
		private String icon;
		private String condition;
		public void setDateDay(String dateDay) {
			this.dateDay = dateDay.trim();
		}
		public String getDateDay() {
			return dateDay;
		}
		public void setDateMonth(String dateMonth) {
			this.dateMonth = dateMonth.trim();
		}
		public String getDateMonth() {
			return dateMonth;
		}
		public void setDateWeekday(String dateWeekday) {
			this.dateWeekday = dateWeekday.trim();
		}
		public String getDateWeekday() {
			return dateWeekday;
		}
		public void setHigh_F(String high_F) {
			this.high_F = high_F.trim();
		}
		public String getHigh_F() {
			return high_F;
		}
		public void setHigh_C(String high_C) {
			this.high_C = high_C.trim();
		}
		public String getHigh_C() {
			return high_C;
		}
		public void setLow_F(String low_F) {
			this.low_F = low_F.trim();
		}
		public String getLow_F() {
			return low_F;
		}
		public void setLow_C(String low_C) {
			this.low_C = low_C.trim();
		}
		public String getLow_C() {
			return low_C;
		}
		public void setIcon(String icon) {
			this.icon = icon.trim();
		}
		public String getIcon() {
			return icon;
		}
		public int getIconId() {
			return ForecastData.convertIconToId(this.condition, false, this.icon);
		}
		public void setCondition(String condition) {
			this.condition = condition;
		}
		public String getCondition() {
			return condition;
		}
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
}
