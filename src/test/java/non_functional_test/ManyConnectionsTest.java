package non_functional_test;

import org.junit.Assert;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hagai_lvi on 6/27/15.
 *
 * This test requires a running server
 */
public class ManyConnectionsTest {
	final static int NUM_OF_CONNECTIONS = 30;
	public static final String URL = "http://localhost:8080/forum-system/facade";

	@Test
	public void test() throws Throwable {
		Thread[] threads = new Thread[NUM_OF_CONNECTIONS];
		final Throwable[] exceptions = new Throwable[NUM_OF_CONNECTIONS];

		for (int i = 0 ; i<threads.length ; i++){
			ConnectionInitiator c = new ConnectionInitiator(URL, exceptions, i);
			threads[i] = new Thread(c);
			threads[i].run();
		}

		for (Thread t: threads){
			t.join();
		}

		for (Throwable t: exceptions){
			if (t != null){
				throw t;
			}
		}

	}

	private static class ConnectionInitiator implements Runnable{
		private final String url;
		private final Throwable[] exceptions;
		private final int threadNumber;

		public ConnectionInitiator(String url, Throwable[] exceptions, int threadNumber){
			this.url = url;
			this.exceptions = exceptions;
			this.threadNumber = threadNumber;
		}

		@Override
		public void run() {
			URL url = null;
			try {
				url = new URL(this.url);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				int responseCode = urlConnection.getResponseCode();
				Assert.assertEquals("Thread " + threadNumber + " got response code " + responseCode,
						200, responseCode);

			} catch (Exception e) {
				e.printStackTrace();
				exceptions[threadNumber] = e;
			}
		}
	}
}
