package com.nhn.platform.qa.cwmtest.chrome;

import com.nhn.platform.qa.cwmtest.Utils.*;

import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.*;

import junit.framework.Test; 
import junit.framework.TestCase; 
import junit.framework.TestSuite; 
 
public class SampleTest  extends TestCase{
	//private WebDriverUtil.driver DriverUtil.driver = DriverUtil.driverUtil.DriverUtil.driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	public SampleTest(String name){
		 super(name);
	}
	@Before
	public void  setUp() throws Exception {
		DriverUtil.ChromeStart();
		baseUrl = "http://smilejay.com/";
	}
	//@Test
	public void testSearchMyBlog0() throws Exception {
		DriverUtil.driver.get(baseUrl + "/");
		DriverUtil.driver.findElement(By.name("s")).clear();
		DriverUtil.driver.findElement(By.name("s")).sendKeys("Linux");
		DriverUtil.driver.findElement(By.cssSelector("input.button")).click();
		int check1 = DriverUtil.driver.findElement(By.className("title")).getText().indexOf("搜索结果");
		int check2 = DriverUtil.driver.findElement(By.className("info")).getText().indexOf("关键字");
		int check3 = DriverUtil.driver.findElement(By.className("info")).getText().indexOf("Linux");
		boolean flag = false;
		if ((check1 != -1) && (check2 != -1) && (check3 != -1) ) {
			flag = true;
		}
		System.out.println(DriverUtil.driver.findElement(By.className("info")).getText());
		//Assert.assertTrue(flag);
		DriverUtil.html.InsertHtml("function-name", "In order to verify the function is OK!", EtcIO.PassTag, "none", "none", "1. open browser.<br /> 2. testing.", "success", "success", "none");
	}
    //@Test
	public void testSearchMyBlog1() throws Exception {
		DriverUtil.driver.get(baseUrl + "/");
		DriverUtil.driver.findElement(By.name("s")).clear();
		DriverUtil.driver.findElement(By.name("s")).sendKeys("Linux");
		DriverUtil.driver.findElement(By.cssSelector("input.button")).click();
		int check1 = DriverUtil.driver.findElement(By.className("title")).getText().indexOf("搜索结果");
		int check2 = DriverUtil.driver.findElement(By.className("info")).getText().indexOf("关键字");
		int check3 = DriverUtil.driver.findElement(By.className("info")).getText().indexOf("Linux");
		boolean flag = false;
		if ((check1 != -1) && (check2 != -1) && (check3 != -1) ) {
			flag = true;
		}
		System.out.println(DriverUtil.driver.findElement(By.className("info")).getText());
		//Assert.assertTrue(flag);
		DriverUtil.html.InsertHtml("function-name", "In order to verify the function is OK!", EtcIO.FailTag, "none", "none", "1. open browser.<br /> 2. testing.", "success", "fail", "none");
		DriverUtil.html.ScreenCapture();
	}
    @After
	public void tearDown() throws Exception {		
		DriverUtil.driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			//fail(verificationErrorString);
		}
	}
	public static Test suite() {  
	         TestSuite suite = new TestSuite("Test for SampleTest");  
	         //$JUnit-BEGIN$ 
	         suite.addTest( new SampleTest("testSearchMyBlog0" ));
	         suite.addTest( new SampleTest("testSearchMyBlog1" ));
	         //$JUnit-END$  
	         return suite;  
	}  
}
