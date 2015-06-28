package non_functional_test;

import non_functional_test.utils.HttpUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hagai_lvi on 6/27/15.
 */

//TODO continue implementation
public class ManyLoginsTest {
	final static int NUM_OF_CONNECTIONS = 30;

	@Test
	public void testManyLogins() throws Throwable {

		Thread[] threads = new Thread[NUM_OF_CONNECTIONS];
		final Throwable[] exceptions = new Throwable[NUM_OF_CONNECTIONS];

		for (int i = 0 ; i<threads.length ; i++){
			LoginConnector l = new LoginConnector("A", "ADMIN", "ADMIN", i, exceptions);
			threads[i] = new Thread(l);
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

	private static class LoginConnector implements Runnable{
		private final String FORUM_NAME;
		private final String USERNAME;
		private final String PASSWORD;
		private final int threadNumber;
		private final Throwable[] exceptions;

		public LoginConnector(String forumName, String username, String password, int threadNumber, Throwable[] exceptions){
			FORUM_NAME = forumName;
			USERNAME = username;
			PASSWORD = password;
			this.threadNumber = threadNumber;
			this.exceptions = exceptions;
		}

		@Override
		public void run() {
			int responseCode = 0;
			try {
				long time = System.currentTimeMillis();
				responseCode = HttpUtils.login(FORUM_NAME,USERNAME, PASSWORD);
				Assert.assertTrue((System.currentTimeMillis()-time) < 2000);
				Assert.assertEquals("thread " + threadNumber + " got response code " + responseCode,
						200, responseCode);
			}

			catch (Exception e) {
				e.printStackTrace();
				exceptions[threadNumber] = e;
			}
		}
	}
}
