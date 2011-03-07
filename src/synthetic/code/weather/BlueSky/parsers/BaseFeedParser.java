package synthetic.code.weather.BlueSky.parsers;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public abstract class BaseFeedParser {
	
	private final URL feedUrl;
	protected boolean abort;

	protected BaseFeedParser(String feedUrl){
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		
		abort = false;
	}

	protected InputStream getInputStream() {
		try {
			URLConnection conn = feedUrl.openConnection();
			conn.setConnectTimeout(20000); // 10s
			conn.setReadTimeout(20000); // 10s
			return conn.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public abstract Object parse(); 
	
	public void stopParse() {
		abort = true;
	}
}