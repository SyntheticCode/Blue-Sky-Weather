/**
 * 
 */
package synthetic.code.weather.BlueSky;

import java.util.ArrayList;
import java.util.List;

//import synthetic.code.weather.BlueSky.BlueSkyActivity.StationAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author David
 *
 */
public class UiObjects {
	// Objects parent (object most on be saved on state changes)
	private BlueSkyActivity parent;
	
	// UI objects
	public TabHost tabHost;
	public TextView location;
	// Station Tab
	public ListView stationListView;
	public TextView selectedStation;
	// Weather Tab
	public TextView updateTime;
	public TextView temperature;
	public TextView windSpeed;
	public TextView weatherCondtion;
	public TextView windGust;
	public TextView humidity;
	public TextView rainfall;
	public TextView pressure;
	public TextView visibility;
	public TextView dewPoint;
	public TextView uvIndex;
	// Forecast Tab
	public ArrayList<ForecastShortView> forecastShort;
	public ArrayList<ForecastExtendedView> forecastExtended;
	
	// Key value for store and load
	private static final String KEY_LOCATION 		= "location";
	private static final String KEY_SELECTEDSTATION	= "selectedStation";
	private static final String KEY_UPDATETIME		= "updateTime";
	private static final String KEY_TEMPERATURE		= "temperature";
	private static final String KEY_WINDSPEED		= "windSpeed";
	private static final String KEY_WEATHERCONDTION	= "weatherCondtion";
	private static final String KEY_WINDGUST		= "windGust";
	private static final String KEY_HUMIDITY		= "humidity";
	private static final String KEY_RAINFALL		= "rainfall";
	private static final String KEY_PRESSURE		= "pressure";
	private static final String KEY_VISIBILITY		= "visibility";
	private static final String KEY_DEWPOINT		= "dewPoint";
	private static final String KEY_UVINDEX			= "uvIndex";
	//private static final String KEY_STATIONLISTVIEW	= "stationListView";
	
	private static final String KEY_FORECASTSHORT_ICON		= "forecastShortIcon";
	private static final String KEY_FORECASTSHORT_TITLE		= "forecastShortTitle";
	private static final String KEY_FORECASTSHORT_CONDITION	= "forecastShortCondition";
	
	private static final String KEY_FORECASTEXTENDED_ICON				= "forecastExtendedIcon";
	private static final String KEY_FORECASTEXTENDED_TITLE				= "forecastExtendedTitle";
	private static final String KEY_FORECASTEXTENDED_CONDITION			= "forecastExtendedCondition";
	private static final String KEY_FORECASTEXTENDED_TEMPERATUREHIGH	= "forecastExtendedTemperatureHigh";
	private static final String KEY_FORECASTEXTENDED_TEMPERATURELOW		= "forecastExtendedTemperatureLow";
	private static final String KEY_FORECASTEXTENDED_TEMPERATUREUNIT	= "forecastExtendedTemperatureUnit";
	
//	public UiObjects() {
//		
//	}
	
	public UiObjects(BlueSkyActivity parentActivity) {
		parent = parentActivity;
		
		Resources res = parent.getResources();
		
		this.tabHost = parent.getTabHost();
		
		this.tabHost.addTab(this.tabHost
				.newTabSpec("Weather")
				.setIndicator("Weather", res.getDrawable(R.drawable.ic_airport))
				.setContent(R.id.tabWeatherLayout));
		
		this.tabHost.addTab(this.tabHost
				.newTabSpec("Forecast")
				.setIndicator("Forecast", res.getDrawable(R.drawable.ic_pws))
				.setContent(R.id.tabForecastLayout));
		
		this.tabHost.addTab(this.tabHost
				.newTabSpec("Stations")
				.setIndicator("Stations", res.getDrawable(R.drawable.ic_airport))
				.setContent(R.id.tabWeatherStationsLayout));
		
		this.tabHost.setCurrentTab(0);
		
		

//		// Set tabs Colors
//		tabHost.setBackgroundColor(Color.BLACK);
//		tabHost.getTabWidget().setBackgroundColor(Color.BLACK);
		
		// Create class objects
		
		this.location = (TextView) parent.findViewById(R.id.weather_location);
		// Station Tab objects
		this.stationListView = (ListView) parent.findViewById(R.id.weatherStations_list);
		this.selectedStation = (TextView) parent.findViewById(R.id.weatherStations_selected);
		// Weather Tab objects
		this.updateTime = (TextView) parent.findViewById(R.id.weather_updateTime);
		this.temperature = (TextView) parent.findViewById(R.id.weather_temperture);
		this.windSpeed = (TextView) parent.findViewById(R.id.weather_wind);
		this.weatherCondtion = (TextView) parent.findViewById(R.id.weather_condition);
		this.windGust = (TextView) parent.findViewById(R.id.weather_wind_gust);
		this.humidity = (TextView) parent.findViewById(R.id.weather_humidity);
		this.rainfall = (TextView) parent.findViewById(R.id.weather_rainfall);
		this.pressure = (TextView) parent.findViewById(R.id.weather_pressure);
		this.visibility = (TextView) parent.findViewById(R.id.weather_visibility);
		this.dewPoint = (TextView) parent.findViewById(R.id.weather_dew);
		this.uvIndex = (TextView) parent.findViewById(R.id.weather_uv);
		// Forecast Tab objects
		this.forecastShort = new ArrayList<ForecastShortView>();
		this.forecastShort.add((ForecastShortView) parent.findViewById(R.id.forecast_short_row1));
		this.forecastShort.add((ForecastShortView) parent.findViewById(R.id.forecast_short_row2));
		this.forecastExtended = new ArrayList<ForecastExtendedView>();
		this.forecastExtended.add((ForecastExtendedView) parent.findViewById(R.id.forecast_extended_row1));
		this.forecastExtended.add((ForecastExtendedView) parent.findViewById(R.id.forecast_extended_row2));
		this.forecastExtended.add((ForecastExtendedView) parent.findViewById(R.id.forecast_extended_row3));
		this.forecastExtended.add((ForecastExtendedView) parent.findViewById(R.id.forecast_extended_row4));
		this.forecastExtended.add((ForecastExtendedView) parent.findViewById(R.id.forecast_extended_row5));
	}
	
	public Bundle saveState(Bundle savedInstanceState) {
		savedInstanceState.putString(KEY_LOCATION, this.location.getText().toString());
		savedInstanceState.putString(KEY_SELECTEDSTATION, this.selectedStation.getText().toString());
		savedInstanceState.putString(KEY_UPDATETIME, this.updateTime.getText().toString());
		savedInstanceState.putString(KEY_TEMPERATURE, this.temperature.getText().toString());
		savedInstanceState.putString(KEY_WINDSPEED, this.windSpeed.getText().toString());
		savedInstanceState.putString(KEY_WEATHERCONDTION, this.weatherCondtion.getText().toString());
		savedInstanceState.putString(KEY_WINDGUST, this.windGust.getText().toString());
		savedInstanceState.putString(KEY_HUMIDITY, this.humidity.getText().toString());
		savedInstanceState.putString(KEY_RAINFALL, this.rainfall.getText().toString());
		savedInstanceState.putString(KEY_PRESSURE, this.pressure.getText().toString());
		savedInstanceState.putString(KEY_VISIBILITY, this.visibility.getText().toString());
		savedInstanceState.putString(KEY_DEWPOINT, this.dewPoint.getText().toString());
		savedInstanceState.putString(KEY_UVINDEX, this.uvIndex.getText().toString());
		
		// Get all forecastShort objects
		int size = this.forecastShort.size();
		int[] icon = new int[size];
		String[] title = new String[size];
		String[] condition = new String[size];
		for(int i = 0; i < size; i++) {
			icon[i] = this.forecastShort.get(i).icon.getId();
			title[i] = this.forecastShort.get(i).title.getText().toString();
			condition[i] = this.forecastShort.get(i).condition.getText().toString();
		}
		// Save all forecastShort as arrays
		savedInstanceState.putIntArray(KEY_FORECASTSHORT_ICON, icon);
		savedInstanceState.putStringArray(KEY_FORECASTSHORT_TITLE, title);
		savedInstanceState.putStringArray(KEY_FORECASTSHORT_CONDITION, condition);
		
		// Get all forecastExtended objects
		size = this.forecastExtended.size();
		icon = new int[size];
		title = new String[size];
		condition = new String[size];
		String[] temperatureHigh = new String[size];
		String[] temperatureLow = new String[size];
		String[] temperatureUnit = new String[size];
		for(int i = 0; i < size; i++) {
			icon[i] = this.forecastExtended.get(i).icon.getId();
			title[i] = this.forecastExtended.get(i).title.getText().toString();
			condition[i] = this.forecastExtended.get(i).condition.getText().toString();
			temperatureHigh[i] = this.forecastExtended.get(i).temperatureHigh.getText().toString();
			temperatureLow[i] = this.forecastExtended.get(i).temperatureLow.getText().toString();
			temperatureUnit[i] = this.forecastExtended.get(i).temperatureUnit.getText().toString();
		}
		
		// Save all forecastShort as arrays
		savedInstanceState.putIntArray(KEY_FORECASTEXTENDED_ICON, icon);
		savedInstanceState.putStringArray(KEY_FORECASTEXTENDED_TITLE, title);
		savedInstanceState.putStringArray(KEY_FORECASTEXTENDED_CONDITION, condition);
		savedInstanceState.putStringArray(KEY_FORECASTEXTENDED_TEMPERATUREHIGH, temperatureHigh);
		savedInstanceState.putStringArray(KEY_FORECASTEXTENDED_TEMPERATURELOW, temperatureLow);
		savedInstanceState.putStringArray(KEY_FORECASTEXTENDED_TEMPERATUREUNIT, temperatureUnit);
		
		return savedInstanceState;
	}
	
	// All UI objects need to already be created before this function is called
	public void loadState(Bundle savedInstanceState) {
		if(savedInstanceState != null) {
			String s = savedInstanceState.getString(KEY_LOCATION);
			if(s != null) {
				this.location.setText(s);
			}
			
			s = savedInstanceState.getString(KEY_SELECTEDSTATION);
			if(s != null) {
				this.selectedStation.setText(s);
			}

			s = savedInstanceState.getString(KEY_UPDATETIME);
			if(s != null) {
				this.updateTime.setText(s);
			}

			s = savedInstanceState.getString(KEY_TEMPERATURE);
			if(s != null) {
				this.temperature.setText(s);
			}

			s = savedInstanceState.getString(KEY_WINDSPEED);
			if(s != null) {
				this.windSpeed.setText(s);
			}

			s = savedInstanceState.getString(KEY_WEATHERCONDTION);
			if(s != null) {
				this.weatherCondtion.setText(s);
			}

			s = savedInstanceState.getString(KEY_WINDGUST);
			if(s != null) {
				this.windGust.setText(s);
			}

			s = savedInstanceState.getString(KEY_HUMIDITY);
			if(s != null) {
				this.humidity.setText(s);
			}

			s = savedInstanceState.getString(KEY_RAINFALL);
			if(s != null) {
				this.rainfall.setText(s);
			}

			s = savedInstanceState.getString(KEY_PRESSURE);
			if(s != null) {
				this.pressure.setText(s);
			}

			s = savedInstanceState.getString(KEY_VISIBILITY);
			if(s != null) {
				this.visibility.setText(s);
			}

			s = savedInstanceState.getString(KEY_DEWPOINT);
			if(s != null) {
				this.dewPoint.setText(s);
			}

			s = savedInstanceState.getString(KEY_UVINDEX);
			if(s != null) {
				this.uvIndex.setText(s);
			}
			
			
			// Get all forecastShort data
			int size = this.forecastShort.size();
			int[] icon = savedInstanceState.getIntArray(KEY_FORECASTSHORT_ICON);
			String[] title = savedInstanceState.getStringArray(KEY_FORECASTSHORT_TITLE);
			String[] condition = savedInstanceState.getStringArray(KEY_FORECASTSHORT_CONDITION);
			for(int i = 0; i < size; i++) {
				if(i > icon.length || i > title.length || i > condition.length) {
					break;
				}
				else if(icon[i] != 0 && title[i] != null && condition[i] != null) {
					this.forecastShort.get(i).icon.setImageResource(icon[i]);
					this.forecastShort.get(i).title.setText(title[i]);
					this.forecastShort.get(i).condition.setText(condition[i]);
				}
			}
			
			// Get all forecastExtended data
			size = this.forecastExtended.size();
			icon = savedInstanceState.getIntArray(KEY_FORECASTEXTENDED_ICON);
			title = savedInstanceState.getStringArray(KEY_FORECASTEXTENDED_TITLE);
			condition = savedInstanceState.getStringArray(KEY_FORECASTEXTENDED_CONDITION);
			String[] temperatureHigh = savedInstanceState.getStringArray(KEY_FORECASTEXTENDED_TEMPERATUREHIGH);
			String[] temperatureLow = savedInstanceState.getStringArray(KEY_FORECASTEXTENDED_TEMPERATURELOW);
			String[] temperatureUnit = savedInstanceState.getStringArray(KEY_FORECASTEXTENDED_TEMPERATUREUNIT);
			for(int i = 0; i < size; i++) {
				if(i > icon.length || i > title.length || i > condition.length || i > temperatureHigh.length || i > temperatureLow.length || i > temperatureUnit.length) {
					break;
				}
				else if(icon[i] != 0 && title[i] != null && condition[i] != null && temperatureHigh[i] != null && temperatureLow[i] != null && temperatureUnit[i] != null) {
					this.forecastExtended.get(i).icon.setImageResource(icon[i]);
					this.forecastExtended.get(i).title.setText(title[i]);
					this.forecastExtended.get(i).condition.setText(condition[i]);
					this.forecastExtended.get(i).temperatureHigh.setText(temperatureHigh[i]);
					this.forecastExtended.get(i).temperatureLow.setText(temperatureLow[i]);
					this.forecastExtended.get(i).temperatureUnit.setText(temperatureUnit[i]);
				}
			}
		}
	}
	
	public void updateWeatherTab(WeatherData weather) {
		this.updateTime.setText(weather.getTime());
		this.temperature.setText(weather.getTempFString());
		this.weatherCondtion.setText(weather.getWeatherCondition());
		this.windSpeed.setText(weather.getWindDirSpeedString());
		this.weatherCondtion.setText(weather.getWeatherCondition());
		this.windGust.setText(weather.getWindGustMphString());
		this.humidity.setText(weather.getHumidityString());
		this.rainfall.setText(weather.getRainfallInchString());
		this.pressure.setText(weather.getPressureInchString());
		this.visibility.setText(weather.getVisibilityMileString());
		this.dewPoint.setText(weather.getDewFString());
		this.uvIndex.setText(weather.getUvString());
	}
	
	public void updateForecastTab(ForecastData forecast) {
		if(forecast.forecastShort.size() > 1 && forecast.forecastExtended.size() > 4) {
			// Short Forecast display data
			for(int i = 0; i < BlueSkyActivity.FORECAST_SHORT_COUNT; i++) {
				this.forecastShort.get(i).icon.setImageResource(forecast.forecastShort.get(i).getIconId());
				this.forecastShort.get(i).title.setText(forecast.forecastShort.get(i).getTitle());
				this.forecastShort.get(i).condition.setText(forecast.forecastShort.get(i).getForecast());
			}
			
			// Extended Forecast display data
			for(int i = 0; i < BlueSkyActivity.FORECAST_EXTENDED_COUNT; i++) {
				this.forecastExtended.get(i).icon.setImageResource(forecast.forecastExtended.get(i).getIconId());
				this.forecastExtended.get(i).title.setText(forecast.forecastExtended.get(i).getDateWeekday());
				this.forecastExtended.get(i).condition.setText(forecast.forecastExtended.get(i).getCondition());
				this.forecastExtended.get(i).temperatureHigh.setText(forecast.forecastExtended.get(i).getHigh_F());
				this.forecastExtended.get(i).temperatureLow.setText(forecast.forecastExtended.get(i).getLow_F());
				this.forecastExtended.get(i).temperatureUnit.setText(parent.getString(R.string.unit_english_temperature));
			}
			
			showForecast();
		}
		else {
			// Warn user
			Toast.makeText(parent, "Forecast Data Error", Toast.LENGTH_LONG).show();
			hideForecast("");
		}
	}
	
	public void updateStationListView(ArrayList<String> names, ArrayList<WeatherStation.StationType> types) {
		StationAdapter adapter = new StationAdapter(parent, R.layout.stations_item, names, types);
		this.stationListView.setAdapter(adapter);
		
		this.showWeatherStation();
	}
	
	public void updateSelectedStation(String title, String elevation) {
		this.selectedStation.setText(title + " (" + elevation + "ft)");
	}
	
	public void hideForecast(String text) {
		// Create all the forecast View objects
		TextView headerShort = (TextView)parent.findViewById(R.id.forecast_short_title);
		TableLayout tableShort = (TableLayout)parent.findViewById(R.id.forecast_table_short);
		TextView headerExtended = (TextView)parent.findViewById(R.id.forecast_extended_title);
		TableLayout tableExtended = (TableLayout)parent.findViewById(R.id.forecast_table_extended);
		TextView forecastEmpty = (TextView)parent.findViewById(R.id.forecast_empty_title);
		
		// Hide the forecast objects
		headerShort.setVisibility(View.GONE);
		tableShort.setVisibility(View.GONE);
		headerExtended.setVisibility(View.GONE);
		tableExtended.setVisibility(View.GONE);
		
		// Show the empty text
		forecastEmpty.setText(text);
		forecastEmpty.setVisibility(View.VISIBLE);
	}
	
	public void showForecast() {
		// Create all the forecast View objects
		TextView headerShort = (TextView)parent.findViewById(R.id.forecast_short_title);
		TableLayout tableShort = (TableLayout)parent.findViewById(R.id.forecast_table_short);
		TextView headerExtended = (TextView)parent.findViewById(R.id.forecast_extended_title);
		TableLayout tableExtended = (TableLayout)parent.findViewById(R.id.forecast_table_extended);
		TextView forecastEmpty = (TextView)parent.findViewById(R.id.forecast_empty_title);
		
		// Show the forecast objects
		headerShort.setVisibility(View.VISIBLE);
		tableShort.setVisibility(View.VISIBLE);
		headerExtended.setVisibility(View.VISIBLE);
		tableExtended.setVisibility(View.VISIBLE);
		
		// Hide the empty text
		forecastEmpty.setVisibility(View.GONE);
	}
	
	public void hideWeatherStation(String text) {
		TextView stationEmpty = (TextView)parent.findViewById(R.id.weatherStations_empty_title);
		
		this.selectedStation.setVisibility(View.GONE);
		this.stationListView.setVisibility(View.GONE);
		
		stationEmpty.setText(text);
		stationEmpty.setVisibility(View.VISIBLE);
	}
	
	public void showWeatherStation() {
		TextView stationEmpty = (TextView)parent.findViewById(R.id.weatherStations_empty_title);
		
		this.selectedStation.setVisibility(View.VISIBLE);
		this.stationListView.setVisibility(View.VISIBLE);
		
		stationEmpty.setVisibility(View.GONE);
	}
	
	/**
	 * Custom Adapter class for stationListView.
	 * Has a custom layout with an icon and text for each row.
	 * @author David
	 *
	 */
	public class StationAdapter extends ArrayAdapter<String> {
		ArrayList<String> names;
		ArrayList<WeatherStation.StationType> types;

		public StationAdapter(Context context, int resource,
				ArrayList<String> objects, ArrayList<WeatherStation.StationType> types) {
			super(context, resource, objects);
			this.names = objects;
			this.types = types;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			View row = inflater.inflate(R.layout.stations_item, parent, false);
			TextView label = (TextView) row.findViewById(R.id.station_text);
			label.setText(this.names.get(position));

			ImageView icon = (ImageView) row.findViewById(R.id.station_icon);

			// Pick which icon to use
			if(this.types.get(position) == WeatherStation.StationType.AIRPORT) {
				icon.setImageResource(R.drawable.ic_airport);
			}
			else if(this.types.get(position) == WeatherStation.StationType.PWS) {
				icon.setImageResource(R.drawable.ic_pws);
			}

			return row;
		}
	}
}
