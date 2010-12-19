/**
 * 
 */
package synthetic.code.weather.BlueSky;

/**
 * Object that holds all useful information about a Personal Weather Station (PWS).
 * @author David
 *
 */
public class PWS {
	private boolean empty;
	private String	url;
	private String	stationID;
	private String	name; // <neighborhood> in XML
	private String	city;
	private String	state;
	private String	country;
	private short	distanceKm;
	private short	distanceMi;
	private WeatherData weather;
	
	public PWS() {
		this.empty = true;
		this.url = "";
		this.stationID = "";
		this.name = "";
		this.city = "";
		this.state = "";
		this.country = "";
		this.distanceKm = 0;
		this.distanceMi = 0;
		weather = null;
	}
	
	public boolean empty() {
		return this.empty;
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

	public void setWeather(WeatherData weather) {
		this.empty = false;
		this.weather = weather;
	}

	public WeatherData getWeather() {
		return weather;
	}
}