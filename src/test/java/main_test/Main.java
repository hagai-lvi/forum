package main_test;

import org.junit.*;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public class Main {

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
