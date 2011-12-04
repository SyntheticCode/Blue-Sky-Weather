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

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author David
 *
 * This is the About Page Activity.
 */
public class AboutActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		// Get the version from the Manifest and update the version TextView
		TextView versionLabel = (TextView)this.findViewById(R.id.about_version);
		String version;
		try {
			version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			version = "error";
		}
		
		versionLabel.setText(getString(R.string.about_label_version, version));
	}
}
