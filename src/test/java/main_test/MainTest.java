package main_test;

import org.junit.*;

import java.util.logging.Logger;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public class MainTest {

	private static Logger logger = Logger.getLogger(MainTest.class.getName());


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
