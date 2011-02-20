/**
 * 
 */
package synthetic.code.weather.BlueSky;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author David
 *
 */
public class ForecastData {
	private final static HashMap<String, Integer> iconHash = new HashMap<String, Integer>();
	static {
		// TODO Change from string id to icon id
		// All Weather Underground icon names mapped display strings
		iconHash.put("chanceflurries", R.string.forecast_chanceflurries);
		iconHash.put("chancerain", R.string.forecast_chancerain);
		iconHash.put("chancesleet", R.string.forecast_chancesleet);
		iconHash.put("chancesnow", R.string.forecast_chancesnow);
		iconHash.put("chancetstorms", R.string.forecast_chancetstorms);
		iconHash.put("clear", R.string.forecast_clear);
		iconHash.put("cloudy", R.string.forecast_cloudy);
		iconHash.put("flurries", R.string.forecast_flurries);
		iconHash.put("fog", R.string.forecast_fog);
		iconHash.put("hazy", R.string.forecast_hazy);
		iconHash.put("mostlycloudy", R.string.forecast_mostlycloudy);
		iconHash.put("mostlysunny", R.string.forecast_mostlysunny);
		iconHash.put("partlycloudy", R.string.forecast_partlycloudy);
		iconHash.put("partlysunny", R.string.forecast_partlysunny);
		iconHash.put("rain", R.string.forecast_rain);
		iconHash.put("sleet", R.string.forecast_sleet);
		iconHash.put("snow", R.string.forecast_snow);
		iconHash.put("sunny", R.string.forecast_sunny);
		iconHash.put("tstorms", R.string.forecast_tstorms);
		iconHash.put("tstorm", R.string.forecast_tstorm);
		iconHash.put("unknown", R.string.forecast_unknown);
	}
	
	public final static int forecastShortCount = 5;
	public final static int forecastExtendedCount = 6;
	
	public ArrayList<ForecastDayShort> forecastShort;
	public ArrayList<ForecastDayExtended> forecastExtended;
	
	public ForecastData() {
		forecastShort = new ArrayList<ForecastDayShort>(forecastShortCount);
		forecastExtended = new ArrayList<ForecastDayExtended>(forecastExtendedCount);
		
//		// Create all the forecast days so set will work
//		for(int i = 0; i < forecastShortCount; i++) {
//			forecastShort.add(new ForecastDayShort());
//		}
//		for(int i = 0; i < forecastExtendedCount; i++) {
//			forecastExtended.add(new ForecastDayExtended());
//		}
	}
	
	private static int convertIconToId(String icon) {
		int id;
		// need to check for the key before getting the value
		if(iconHash.containsKey(icon)) {
			id = iconHash.get(icon);
		}
		else {
			id = R.string.forecast_unknown;
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
