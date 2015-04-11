package main_test;

import org.junit.*;
import main.interfaces.*;
import main.exceptions.*;
import main.forum_contents.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public class MainTest {
	Facade _facade;
	Collection<ForumI> _forumCollection;
	HashMap<ForumI,UserI> _forumUsers;

	public static Logger logger = Logger.getLogger(MainTest.class.getName());


	@BeforeClass
	public static void beforeClass(){
		//logger.setLevel(Level.FINE);

		System.out.println("Before class");
	}

	@Before
	public void beforeTest(){
		logger.fine("123");
	}

	@Test
	public void test1(){
		logger.warning("hello");
		System.out.println("\nTest\n");
		Assert.assertTrue(true);
	}

	@After
	public void afterTest(){
		System.out.println("After test");

	}

	@AfterClass
	public static void afterClass(){
		System.out.println("After class");
	}

}
