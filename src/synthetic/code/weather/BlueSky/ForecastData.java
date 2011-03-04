/**
 * 
 */
package synthetic.code.weather.BlueSky;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

/**
 * @author David
 *
 */
public class ForecastData {
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
	
	public class ForecastDayShort {
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
			return ForecastData.convertIconToId(this.icon);
		}
		public void setForecast(String forecast) {
			this.forecast = forecast.trim();
		}
		public String getForecast() {
			return forecast;
		}
	}
	
	public class ForecastDayExtended {
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
			return ForecastData.convertIconToId(this.icon);
		}
		public void setCondition(String condition) {
			this.condition = condition;
		}
		public String getCondition() {
			return condition;
		}
	}
}
