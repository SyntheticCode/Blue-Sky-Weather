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