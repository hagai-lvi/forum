package non_functional_test;

import non_functional_test.utils.HttpUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hagai_lvi on 6/27/15.
 *
 * This test requires a running server
 */
public class ManyBasicConnectionsTest {
	final static int NUM_OF_CONNECTIONS = 30;
	public static final String URL = "http://localhost:8080/forum-system/facade";

	@Test
	public void testBasicConnection() throws Throwable {
		Thread[] threads = new Thread[NUM_OF_CONNECTIONS];
		final Throwable[] exceptions = new Throwable[NUM_OF_CONNECTIONS];

		for (int i = 0 ; i<threads.length ; i++){
			FacadeConnector c = new FacadeConnector(URL, exceptions, i);
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


	private static class FacadeConnector implements Runnable{
		private final String url;
		private final Throwable[] exceptions;
		private final int threadNumber;

		public FacadeConnector(String url, Throwable[] exceptions, int threadNumber){
			this.url = url;
			this.exceptions = exceptions;
			this.threadNumber = threadNumber;
		}

		@Override
		public void run() {
			int responseCode = 0;
			try {
				responseCode = HttpUtils.connectToFacade();
				Assert.assertEquals("thread " + threadNumber + " got response code " + responseCode,
						200, responseCode);

			} catch (Exception e) {
				e.printStackTrace();
				exceptions[threadNumber] = e;
			}
		}
	}
}
