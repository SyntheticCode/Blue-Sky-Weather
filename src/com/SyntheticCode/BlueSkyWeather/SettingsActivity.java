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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

/**
 * @author David
 *
 * This is the Settings menu activity.
 */
public class SettingsActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		// Add a listener for the About item clicked
		Preference aboutPref = (Preference) findPreference("about_preference");
		aboutPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// Start the AboutActivity
				Context context = getApplicationContext();
				Intent i = new Intent(context, AboutActivity.class);
				startActivityForResult(i, 0);
				
				return false;
			}
		});
	}

}
