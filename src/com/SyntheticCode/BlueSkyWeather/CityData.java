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
package com.SyntheticCode.BlueSkyWeather;

import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author David
 *
 */
public class CityData {
	private String 		city;
	private String 		state;
	private String 		country;
	private String		zip;
	private String		lat;
	private String		lon;
	
	final private String KEY_CITY = "CITY_CITY";
	final private String KEY_STATE = "CITY_STATE";
	final private String KEY_COUNTRY = "CITY_COUNTRY";
	final private String KEY_ZIP = "CITY_ZIP";
	final private String KEY_LAT = "CITY_LAT";
	final private String KEY_LON = "CITY_LON";
	
	public CityData() {
		this.city = "";
		this.state = "";
		this.country = "";
		this.zip = "";
		this.lat = "";
		this.lon = "";
	}
	
	public boolean infoComplete() {
		if(		(this.city != "") &&
				(this.state != "") &&
				(this.country != "") &&
				(this.zip != "") &&
				(this.lat != "") &&
				(this.lon != "")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String encodeObject() {
		String tmp = "";
		
		if(city != "") tmp += city + ",";
		else tmp += "null,";
		
		if(state != "") tmp += state + ",";
		else tmp += "null,";
		
		if(country != "") tmp += country + ",";
		else tmp += "null,";
		
		if(zip != "") tmp += zip + ",";
		else tmp += "null,";
		
		if(lat != "") tmp += lat + ",";
		else tmp += "null,";
		
		if(lon != "") tmp += lon + ",";
		else tmp += "null";
		
		return tmp;
	}
	
	public void decodeObject(String code) {
		String tmp[] = code.split(",");
		Log.v("BlueSky", "Decode Split = " + code);
		
		// Remove any nulls
		for(int i = 0; i < 6; i++) {
			if(tmp[i] == "null") tmp[i] = "";
		}
		
		this.city = tmp[0];
		this.state = tmp[1];
		this.country = tmp[2];
		this.zip = tmp[3];
		this.lat = tmp[4];
		this.lon = tmp[5];
	}
	
	public void saveData(SharedPreferences pref) {
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(KEY_CITY, this.city);
		editor.putString(KEY_STATE, this.state);
		editor.putString(KEY_COUNTRY, this.country);
		editor.putString(KEY_ZIP, this.zip);
		editor.putString(KEY_LAT, this.lat);
		editor.putString(KEY_LON, this.lon);
		
		editor.commit();
	}
	
	public void restoreData(SharedPreferences pref) {
		this.city = pref.getString(KEY_CITY, null);
		this.state = pref.getString(KEY_STATE, null);
		this.country = pref.getString(KEY_COUNTRY, null);
		this.zip = pref.getString(KEY_ZIP, null);
		this.lat = pref.getString(KEY_LAT, null);
		this.lon = pref.getString(KEY_LON, null);
	}
	
	public String getCityState() {
		return city + ", " + state;
	}
	
	public void setCityState(String cityState) {
		String tmp[] = cityState.split(",");
		this.city = tmp[0].trim();
		this.state = tmp[1].trim();
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city.trim();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state.trim();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country.trim();
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip.trim();
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat.trim();
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon.trim();
	}
}
