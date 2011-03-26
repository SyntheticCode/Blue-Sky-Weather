/**
 * Copyright (C) 2011 David Schonert
 *
 * This file is part of BlueSky.
 *
 * BlueSky is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * BlueSky is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BlueSky.  If not, see <http://www.gnu.org/licenses/>.
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
