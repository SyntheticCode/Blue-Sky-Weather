/**
 * 
 */
package synthetic.code.weather.BlueSky;

import java.util.ArrayList;

/**
 * @author David
 * 
 */
public class StationList implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6221839575480780054L;
	
	private ArrayList<WeatherStation> list;
	//public int currentIndex;
	
	public StationList() {
		//this.currentIndex = 0;
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
		ArrayList<String> nameList = new ArrayList<String>(list.size());
		
		for(int i = 0; i < this.list.size(); i++) {
			nameList.add(this.list.get(i).getStationTitle());
		}
		
		return nameList;
	}
	
	public ArrayList<WeatherStation.StationType> getStationTypesList() {
		ArrayList<WeatherStation.StationType> typeList = new ArrayList<WeatherStation.StationType>(list.size());
		
		for(int i = 0; i < this.list.size(); i++) {
			typeList.add(this.list.get(i).getStationType());
		}
		
		return typeList;
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
