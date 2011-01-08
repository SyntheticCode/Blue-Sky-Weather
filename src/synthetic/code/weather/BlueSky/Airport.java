package synthetic.code.weather.BlueSky;

/**
 * Object that holds all useful information about an Airport.
 * @author David
 *
 */
public class Airport extends BaseStation {
	private String 	airportCode;
	
	public Airport() {
		super();
		this.airportCode = "";
	}
	
	public void setAirportCode(String code) {
		this.empty = false;
		this.airportCode = code.trim();
	}
	
	public String getAirportCode() {
		return this.airportCode;
	}	

}
