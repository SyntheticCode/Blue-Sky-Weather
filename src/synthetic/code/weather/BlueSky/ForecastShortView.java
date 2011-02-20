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
