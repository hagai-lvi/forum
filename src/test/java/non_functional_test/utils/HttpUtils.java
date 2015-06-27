package non_functional_test.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hagai_lvi on 6/27/15.
 */
public class HttpUtils {
	public final static String HTTP_PREFIX = "http://";
	public final static String HOST = "localhost";
	public final static String PORT = "8080";
	public final static String POSTFIX = "/forum-system";

	public final static String FULL_URL = HTTP_PREFIX + HOST + ":" + PORT + POSTFIX;

	public final static String FACADE_URL = FULL_URL + "/facade";

	/**
	 * Return the http response code of the request
	 */
	public static  int connectToFacade() throws IOException {

			URL url = new URL(FACADE_URL);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			int responseCode = urlConnection.getResponseCode();
			return responseCode;
	}

}
