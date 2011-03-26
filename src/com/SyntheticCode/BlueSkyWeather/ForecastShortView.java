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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author David
 *
 */
public class ForecastShortView extends RelativeLayout {
	public ImageView icon;
	public TextView title;
	public TextView condition;

	public ForecastShortView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.forecast_row_short, this, true);
		
		icon = (ImageView) findViewById(R.id.forecast_short_icon);
		title = (TextView) findViewById(R.id.forecast_short_title);
		condition = (TextView) findViewById(R.id.forecast_short_condition);
	}

}
