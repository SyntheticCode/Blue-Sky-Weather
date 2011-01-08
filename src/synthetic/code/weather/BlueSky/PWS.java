/**
 * 
 */
package synthetic.code.weather.BlueSky;

/**
 * Object that holds all useful information about a Personal Weather Station (PWS).
 * @author David
 *
 */
public class PWS extends BaseStation {
	private String	url;
	private String	stationID;
	private String	name; // <neighborhood> in XML
	private short	distanceKm;
	private short	distanceMi;
	
	public PWS() {
		super();
		this.url = "";
		this.stationID = "";
		this.name = "";
		this.distanceKm = 0;
		this.distanceMi = 0;
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