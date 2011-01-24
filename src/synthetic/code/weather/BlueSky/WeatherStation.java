package synthetic.code.weather.BlueSky;

public class WeatherStation {
	public enum StationType {
		AIRPORT, PWS, GENERIC
	}
	
	// All station data
	private StationType	type;
	private boolean 	empty;
	private String		stationID; // same as airport code
	private String 		city;
	private String 		state;
	private String 		country;
	private float		lat;
	private float		lon;
	private int			elevation;
	private WeatherData	weather;
	
	// PWS station data
	private String	url;
	
	private String	name; // <neighborhood> in XML
	private short	distanceKm;
	private short	distanceMi;
	
	public WeatherStation(StationType stationType) {
		this.type = stationType;
		this.empty = true;
		this.city = "";
		this.state = "";
		this.country = "";
		this.lat = 0;
		this.lon = 0;
		this.elevation = 0;
		this.weather = new WeatherData();
		//this.airportCode = "";
		this.url = "";
		this.stationID = "";
		this.name = "";
		this.distanceKm = 0;
		this.distanceMi = 0;
	}
	
	public WeatherStation() {
		this(StationType.GENERIC);
	}
	
	// Check if object is empty (null)
	public boolean empty() {
		return this.empty;
	}
	
	/* Getters and Setters */
	public void setStationType(StationType type) {
		// Changing type does not make an object not empty
		this.type = type;
	}
	public StationType getStationType() {
		return type;
	}
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
	public void setUrl(String url) {
		this.empty = false;
		this.url = url.trim();
	}
	public String getUrl() {
		return this.url;
	}
	public void setId(String id) {
		this.empty = false;
		this.stationID = id.trim();
	}
	public String getId() {
		return this.stationID;
	}
	public void setName(String name) {
		this.empty = false;
		this.name = name.trim();
	}
	public String getName() {
		return this.name;
	}
	public void setDistance(short distance, boolean metric) {
		this.empty = false;
		if(metric) {
			this.distanceKm = distance;
		}
		else {
			this.distanceMi = distance;
		}
	}
	public void setDistance(String distance, boolean metric) {
		this.empty = false;
		if(metric) {
			this.distanceKm = Short.parseShort(distance);
		}
		else {
			this.distanceMi = Short.parseShort(distance);
		}
	}
	public short getDistance(boolean metric) {
		if(metric) return this.distanceKm;
		else return this.distanceMi;
	}
}
