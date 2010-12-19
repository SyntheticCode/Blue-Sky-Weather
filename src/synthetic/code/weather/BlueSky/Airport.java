package synthetic.code.weather.BlueSky;

/**
 * Object that holds all useful information about an Airport.
 * @author David
 *
 */
public class Airport {
	private boolean empty;
	private String 	airportCode;
	private String 	city;
	private String 	state;
	private String 	country;
	private float	lat;
	private float	lon;
	private WeatherData weather;
	
	public Airport() {
		this.empty = true;
		this.airportCode = "";
		this.city = "";
		this.state = "";
		this.country = "";
		this.lat = 0;
		this.lon = 0;
		this.setWeather(null);
	}
	
	public boolean empty() {
		return this.empty;
	}
	
	public void setAirportCode(String code) {
		this.empty = false;
		this.airportCode = code.trim();
	}
	
	public String getAirportCode() {
		return this.airportCode;
	}
	
	public void setCity(String city) {
		this.empty = false;
		this.city = city.trim();
	}
	
	public String getCity() {
		return this.city;
	}
	
	public void setState(String state) {
		this.empty = false;
		this.state = state.trim();
	}
	
	public String getState() {
		return this.state;
	}
	
	public void setCountry(String country) {
		this.empty = false;
		this.country = country.trim();
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public void setLatitude(float lat) {
		this.empty = false;
		this.lat = lat;
	}
	
	public void setLatitude(String lat) {
		this.empty = false;
		this.lat = Float.parseFloat(lat);
	}
	
	public float getLatitude() {
		return this.lat;
	}
	
	public void setLongitude(float lon) {
		this.empty = false;
		this.lon = lon;
	}
	
	public void setLongitude(String lon) {
		this.empty = false;
		this.lon = Float.parseFloat(lon);
	}
	
	public float getLongitude() {
		return this.lon;
	}

	public void setWeather(WeatherData weather) {
		this.empty = false;
		this.weather = weather;
	}

	public WeatherData getWeather() {
		return weather;
	}
}
