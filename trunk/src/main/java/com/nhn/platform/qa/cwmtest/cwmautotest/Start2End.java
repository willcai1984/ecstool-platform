package com.nhn.platform.qa.cwmtest.cwmautotest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.nhn.platform.qa.cwmtest.Utils.DriverUtil;
import com.nhn.platform.qa.cwmtest.Utils.EtcIO;

public class Start2End extends TestCase {
	public Start2End(String name) {
		super(name);
	}

	// @Test
	public void EndJUnitTest() throws Exception {
		DriverUtil.html.CompleteCount();
		DriverUtil.StopService();
		System.out.println("Complete to clear ENV!");
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Start2End");
		// $JUnit-BEGIN$
		suite.addTest(new Start2End("EndJUnitTest"));
		// $JUnit-END$
		return suite;
	}
    public static void ClickYes(WebDriver driver){
    	try {
    		driver.findElement(By.xpath(ICwmXpath.xcmsPopYes))
					.click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static void ClickOK(WebDriver driver){
    	try {
    		driver.findElement(By.xpath(ICwmXpath.xcmsPopOk))
					.click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	public static void Login(String baseUrl) {
		DriverUtil.driver.get(baseUrl);
		Sleep(3000);
		try {
			// 登录
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameUser)).clear();
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameUser))
					.sendKeys(EtcIO.userName);
			Sleep(1000);
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnamePassword))
					.clear();
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnamePassword))
					.sendKeys(EtcIO.userPwd);
			// SendKeys.SendWait("~");
			DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsLogin))
					.click();
			Sleep(1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void Logout(String baseUrl) {
		DriverUtil.driver.get(baseUrl);
		Sleep(3000);
		try {
			// 退出
			DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsLogout))
					.click();
			Sleep(1000);
			DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsPopYes))
					.click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean IsElementPresent(By by) {
		try {
			DriverUtil.driver.findElement(by);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static void Sleep(long milsecond) {
		try {
			Thread.sleep(milsecond);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void InsertHtml(String taskname,String steps,String expects,String results,String resultTag){
		DriverUtil.html.InsertHtml(taskname,
				"In order to check the "+taskname+"-function is OK!",
				resultTag, "none", "none", steps, expects,
				results, "none");
	}
}
