/**
 * 
 */
package synthetic.code.weather.BlueSky;

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
public class ForecastExtendedView extends RelativeLayout {
	public ImageView icon;
	public TextView title;
	public TextView condition;
	public TextView temperatureHigh;
	public TextView temperatureLow;
	public TextView temperatureUnit;

	public ForecastExtendedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.forecast_row_extended, this, true);
		
		icon = (ImageView) findViewById(R.id.forecast_extended_icon);
		title = (TextView) findViewById(R.id.forecast_extended_title);
		condition = (TextView) findViewById(R.id.forecast_extended_condition);
		temperatureHigh = (TextView) findViewById(R.id.forecast_extended_temperature_high);
		temperatureLow = (TextView) findViewById(R.id.forecast_extended_temperature_low);
		temperatureUnit = (TextView) findViewById(R.id.forecast_extended_temperature_unit);
	}

}
