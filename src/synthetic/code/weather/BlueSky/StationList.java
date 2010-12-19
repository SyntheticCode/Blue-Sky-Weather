/**
 * 
 */
package synthetic.code.weather.BlueSky;

import java.util.ArrayList;

/**
 * @author David
 * 
 */
public class StationList {
	private ArrayList<PWS> pwsList;
	private ArrayList<Airport> airportList;

	public StationList() {
		this.pwsList = new ArrayList<PWS>();
		this.airportList = new ArrayList<Airport>();
	}
	
	public void addPws(PWS pws) {
		this.pwsList.add(pws);
	}
	
	public void addAirport(Airport airport) {
		this.airportList.add(airport);
	}
	
	public void clearAirport() {
		this.airportList.clear();
	}
	
	public void clearPws() {
		this.pwsList.clear();
	}
	
	public ArrayList<String> getAirportNameList() {
		ArrayList<String> names = new ArrayList<String>();
		String name;
		for(int i = 0; i < this.airportList.size(); i++) {
			// Format name : "City, State (Code)"
			name = this.airportList.get(i).getCity() + ", ";
			name += this.airportList.get(i).getState() + " (";
			name += this.airportList.get(i).getAirportCode() + ")";
			names.add(name);
		}
		return names;
	}
	
	public ArrayList<String> getPwsNameList() {
		ArrayList<String> names = new ArrayList<String>();
		for(int i = 0; i < this.pwsList.size(); i++) {
			// Neighborhood is the name of the station
			names.add(this.pwsList.get(i).getName());
		}
		return names;
	}
	
	public ArrayList<String> getPwsNameDistanceList(boolean metric) {
		ArrayList<String> names = new ArrayList<String>();
		String name;
		for(int i = 0; i < this.pwsList.size(); i++) {
			// Format name : "Neighborhood (Distance)"
			// Neighborhood is the name of the station
			name = this.pwsList.get(i).getName() + " (";
			name += this.pwsList.get(i).getDistance(metric);
			// Add unit to end of distance
			if(metric) name += "km)";
			else name += "mi)";
			names.add(name);
		}
		return names;
	}
	
	public Airport getAirport(int index) {
		if((index < 0) || (index >= this.airportList.size())) {
			return null;
		}
		else {
			return this.airportList.get(index);
		}
	}
	
	public PWS getPws(int index) {
		if((index < 0) || (index >= this.pwsList.size())) {
			return null;
		}
		else {
			return this.pwsList.get(index);
		}
	}
}
