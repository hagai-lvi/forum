package main_test;

import org.junit.*;

import java.util.logging.Logger;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public class Main {

	public static Logger logger = Logger.getLogger(Main.class.getName());


	@BeforeClass
	public static void beforeClass(){
		System.out.println("Before class");
	}

	@Before
	public void beforeTest(){
		System.out.println("Before test");
	}

	@Test
	public void test1(){
		logger.warning("hello");
		System.out.println("Test");
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
