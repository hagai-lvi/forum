package non_functional_test.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;
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
	public final static String LOGIN_PAGE_URL = FULL_URL + "/login_page";
	public final static String FORUM_HOME_PAGE_URL = FULL_URL + "/forum_homepage";

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

	/**
	 *
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 */
	public static int login(String forumName,String username, String password) throws IOException {
		URL url = new URL(LOGIN_PAGE_URL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write("forum=A");
		wr.close();
		if (conn.getResponseCode() != 200){
			return conn.getResponseCode();
		}

		url = new URL(FORUM_HOME_PAGE_URL);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write("forumName=" + forumName + "&username=" + username + "&password=" + password);
		wr.close();
		return conn.getResponseCode();

	}
}
