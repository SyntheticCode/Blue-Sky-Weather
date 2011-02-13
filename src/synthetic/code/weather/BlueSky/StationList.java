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
	private ArrayList<WeatherStation> list;
	
	public StationList() {
		this.list = new ArrayList<WeatherStation>();
	}
	
	public void add(WeatherStation station) {
		this.list.add(station);
	}
	
	public WeatherStation get(int index) {
		return this.list.get(index);
	}
	
	public int size() {
		return this.list.size();
	}
	
	public void clear() {
		this.list.clear();
	}
	
	public ArrayList<String> getStationNamesList() {
		ArrayList<String> nameList = new ArrayList<String>();
		
		for(int i = 0; i < this.list.size(); i++) {
			nameList.add(this.list.get(i).getStationTitle());
		}
		
		return nameList;
	}
	
	public WeatherStation.StationType getStationType(int index) {
		return list.get(index).getStationType();
	}
	
	public int getFirstAirport() {
		for(int i = 0; i < this.size(); i++) {
			if(this.get(i).getStationType() == WeatherStation.StationType.AIRPORT)
				return i;
		}
		return -1;
	}
	
	public int getFirstPws() {
		for(int i = 0; i < this.size(); i++) {
			if(this.get(i).getStationType() == WeatherStation.StationType.PWS)
				return i;
		}
		return -1;
	}
}
