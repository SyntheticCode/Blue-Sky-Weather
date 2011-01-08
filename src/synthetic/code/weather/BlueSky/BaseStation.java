package synthetic.code.weather.BlueSky;

public class BaseStation {
	protected boolean 		empty;
	protected String 		city;
	protected String 		state;
	protected String 		country;
	protected float			lat;
	protected float			lon;
	protected int			elevation;
	protected WeatherData	weather;
	
	protected BaseStation() {
		this.empty = true;
		this.city = "";
		this.state = "";
		this.country = "";
		this.lat = 0;
		this.lon = 0;
		this.elevation = 0;
		this.weather = new WeatherData();
	}
	
	// Check if object is empty (null)
	public boolean empty() {
		return this.empty;
	}
	
	/* Getters and Setters */
	public void setCity(String city) {
		this.empty = false;
		this.city = city;
	}
	public String getCity() {
		return city;
	}
	public void setState(String state) {
		this.empty = false;
		this.state = state;
	}
	public String getState() {
		return state;
	}
	public void setCountry(String country) {
		this.empty = false;
		this.country = country;
	}
	public String getCountry() {
		return country;
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
		return lat;
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
		return lon;
	}
	public void setElevation(int elevation) {
		this.empty = false;
		this.elevation = elevation;
	}
	public void setElevation(String elevation) {
		this.empty = false;
		this.elevation = Integer.parseInt(elevation);
	}
	public int getElevation() {
		return elevation;
	}
	public void setWeather(WeatherData weather) {
		this.empty = false;
		this.weather = weather;
	}
	public WeatherData getWeather() {
		return weather;
	}
}
