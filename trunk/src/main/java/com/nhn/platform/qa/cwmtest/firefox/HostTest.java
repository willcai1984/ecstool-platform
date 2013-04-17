/********************
 * @title HostTest.java
 * @package com.nhn.platform.qa.cwmtest.firefox
 * @description 
 * @author cn15291(Frank Wu)
 * @update 2013-1-28 上午11:34:07
 * @version V1.0
 ********************/

package com.nhn.platform.qa.cwmtest.firefox;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.nhn.platform.qa.cwmtest.Utils.*;
import com.nhn.platform.qa.cwmtest.cwmautotest.ICwmXpath;
import com.nhn.platform.qa.cwmtest.cwmautotest.Start2End;

/********************
 * @description
 * @version 1.0
 * @author cn15291(Frank Wu)
 * @update 2013-1-28 上午11:34:07
 ********************/

public class HostTest extends TestCase {
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();

	public HostTest(String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		DriverUtil.FirefoxStart();
		baseUrl = EtcIO.baseUrl;
	}

	@After
	public void tearDown() throws Exception {
		DriverUtil.driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			// fail(verificationErrorString);
		}
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for HostTest");
		// $JUnit-BEGIN$
		suite.addTest(new HostTest("LoginTest001_007"));
		suite.addTest(new HostTest("LogoutTest001_002"));
		suite.addTest(new HostTest("ChangePwdTest001_016"));
		// $JUnit-END$
		return suite;
	}

	public void Login(String user, String password, String expect) {
		try {
			DriverUtil.driver.get(baseUrl);
			Start2End.Sleep(3000);
			String expectPath = "//span[@role='presentation']";
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameUser)).clear();
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameUser))
					.sendKeys(user);
			Start2End.Sleep(1000);
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnamePassword))
					.clear();
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnamePassword))
					.sendKeys(password);
			DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsLogin))
					.click();
			Start2End.Sleep(1000);
			String temp = DriverUtil.driver.findElement(By.xpath(expectPath))
					.getText();
			// DriverUtil.html.InsertHtml(TaskName, TestSummary, TestResult,
			// Comments, Precondition, Steps, Expects, Results, Remarks)
			boolean flag = temp.trim().contains(expect);
			DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsPopOk))
					.click();
			if (flag) {
				DriverUtil.html.InsertHtml("Login",
						"In order to check the Login-function is OK!",
						EtcIO.PassTag, "none", "none", "Input incorrect username:" + user
								+ " and password:" + password
								+ ", and then click login-button.", expect,
						temp, "none");
			} else {
				DriverUtil.html.InsertHtml("Login",
						"In order to check the Login-function is OK!",
						EtcIO.FailTag, "none", "none", "Input incorrect username:" + user
								+ " and password:" + password
								+ ", and then click login-button.", expect,
						temp, "none");
				DriverUtil.html.ScreenCapture(DriverUtil.driver);
			}
			// Assert.assertTrue(flag);
		} catch (Exception e) {
			DriverUtil.html.InsertHtml("Login",
					"In order to check the Login-function is OK!",
					EtcIO.ErrorTag, "exception error", "none", "Input incorrect username:"
							+ user + " and password:" + password
							+ ", and then click login-button.", expect,
					e.getMessage(), "none");
			DriverUtil.html.ScreenCapture(DriverUtil.driver);
		}
	}

	public void LoginTest001_007() throws Exception {
		// 测试数据和期望结果
		String[] username = new String[] { "admin", "admin", "  ", "",
				"admin1234", "admin" };
		String[] password = new String[] { "admin", "    ", "123456", "",
				"123456", "123" };
		String[] expect = new String[] { "Login was denied!",
				"Login was denied!", "Login was denied!",
				"User name is invalid!", "Login was denied!",
				"Login was denied!" };
		// Login001-006
		for (int i = 0; i < username.length; i++) {
			Login(username[i], password[i], expect[i]);
		}
		// Login007
		try {
			// 登录
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameUser)).clear();
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameUser))
					.sendKeys(EtcIO.userName);
			Start2End.Sleep(1000);
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnamePassword))
					.clear();
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnamePassword))
					.sendKeys(EtcIO.userPwd);
			// SendKeys.SendWait("~");
			DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsLogin))
					.click();
		} catch (Exception e) {
			DriverUtil.html.InsertHtml("Login",
					"In order to check the Login-function is OK!",
					EtcIO.ErrorTag, "none", "none", "Input correct user:"
							+ EtcIO.userName + " and password:"
							+ EtcIO.userPwd
							+ ", and then click login-button.", "none",
							e.getMessage(), "none");
			DriverUtil.html.ScreenCapture(DriverUtil.driver);
		}
		String expPath = "//span[text()='Information - CUBRID Database Server']";
		boolean flag = Start2End.IsElementPresent(By.xpath(expPath));
		if (flag) {
			DriverUtil.html.InsertHtml("Login",
					"In order to check the Login-function is OK!",
					EtcIO.PassTag, "none", "none", "Input correct user:"
							+ EtcIO.userName + " and password:"
							+ EtcIO.userPwd
							+ ", and then click login-button.", expPath,
					"Have found successfully!", "none");
		} else {
			DriverUtil.html.InsertHtml("Login",
					"In order to check the Login-function is OK!",
					EtcIO.FailTag, "none", "none", "Input user:"
							+ EtcIO.userName + " and password:"
							+ EtcIO.userPwd
							+ ", and then click login-button.", expPath,
					"Have not found!", "none");
			DriverUtil.html.ScreenCapture(DriverUtil.driver);
		}
		Assert.assertTrue(flag);
		Start2End.Logout(this.baseUrl);
	}

	public void LogoutTest001_002() throws Exception {
		Start2End.Login(this.baseUrl);
		// Logout001
		try {
			// 退出
			DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsLogout))
					.click();
			Start2End.Sleep(1000);
			DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsPopNo))
					.click();
			Start2End.Sleep(1000);
		} catch (Exception e) {
			DriverUtil.html.InsertHtml("Login",
					"In order to check the Login-function is OK!",
					EtcIO.ErrorTag, "none", "none",
					"Login and click Logout-button. Then click Cancel-button.", e.getMessage(),
					"Have not found!", "none");
			DriverUtil.html.ScreenCapture(DriverUtil.driver);
		}
		String expPath = "//span[text()='Information - CUBRID Database Server']";
		boolean flag = Start2End.IsElementPresent(By.xpath(expPath));
		if (flag) {
			DriverUtil.html.InsertHtml("Logout",
					"In order to check the Login-function is OK!",
					EtcIO.PassTag, "none", "none",
					"Login and click Logout-button. Then click Cancel-button.", expPath,
					"Have found successfully!", "none");
		} else {
			DriverUtil.html.InsertHtml("Logout",
					"In order to check the Logout-function is OK!",
					EtcIO.FailTag, "none", "none", 
					"Login and click Logout-button. Then click Cancel-button.", expPath,
					"Have not found!", "none");
			DriverUtil.html.ScreenCapture(DriverUtil.driver);
		}
		// Logout002
		try {
			DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsLogout))
					.click();
			Start2End.Sleep(1000);
			DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsPopYes))
					.click();
			Start2End.Sleep(1000);
		} catch (Exception e) {
			DriverUtil.html.InsertHtml("Logout",
					"In order to check the Logout-function is OK!",
					EtcIO.ErrorTag, "none", "none",
					"Login and click Logout-button. Then click OK-button.", e.getMessage(),
					"Have not found!", "none");
			DriverUtil.html.ScreenCapture(DriverUtil.driver);
		}
		expPath = ICwmXpath.xcmsLogin;
		flag = Start2End.IsElementPresent(By.xpath(expPath));
		if (flag) {
			DriverUtil.html.InsertHtml("Logout",
					"In order to check the Logout-function is OK!",
					EtcIO.PassTag, "none", "none",
					"Login and click Logout-button. Then click OK-button..", expPath,
					"Have found successfully!", "none");
		} else {
			DriverUtil.html.InsertHtml("Logout",
					"In order to check the Logout-function is OK!",
					EtcIO.FailTag, "none", "none",
					"Login and click Logout-button. Then click OK-button.", expPath,
					"Have not found!", "none");
			DriverUtil.html.ScreenCapture(DriverUtil.driver);
		}
		Assert.assertTrue(flag);
	}

	public void ChangePwdTest001_016() throws Exception {
		// 测试数据
		String[] oldPassword = new String[] { EtcIO.userPwd, "214598",
				"   ", EtcIO.userPwd, EtcIO.userPwd,
				EtcIO.userPwd, EtcIO.userPwd ,"abc"};
		String[] newPassword = new String[] { "214598", "123456", "123456",
				"    ", "", "214", EtcIO.userPwd ,EtcIO.userPwd};
		String[] confirmPassword = new String[] { "214598", "123456", "123456",
				"    ", "", "214", EtcIO.userPwd, EtcIO.userPwd };
		String[] expect = new String[] { "changed successfully!", "changed successfully!", "Please input the old password!", "Invalid password.",
				"Invalid password.", "Invalid password.", "changed successfully!","The old password is incorrect!"};
		String checkPath="//span[@role='presentation']";
		Start2End.Login(this.baseUrl);
		//ChangePwdTest001_014
		for(int i=0;i<oldPassword.length;i++){
			try{
			//Cancel
			if(Start2End.IsElementPresent(By.xpath(ICwmXpath.xcmsChange))){
				DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsChange)).click();
				Start2End.Sleep(1000);
			}
			else{
				continue;
			}
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameOldPwd)).clear();
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameOldPwd)).sendKeys(oldPassword[i]);
			Start2End.Sleep(1000);
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameNewPwd)).clear();
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameNewPwd)).sendKeys(newPassword[i]);
			Start2End.Sleep(1000);
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameConfirmPwd)).clear();
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameConfirmPwd)).sendKeys(confirmPassword[i]);
			Start2End.Sleep(1000);
			DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsChangeCancel)).click();
			Start2End.Sleep(1000);
			String temp="//span[text()='Information - CUBRID Database Server']";			
			boolean flag=Start2End.IsElementPresent(By.xpath(temp));
			if(flag){
				Start2End.InsertHtml("ChangePassword", "Login and start to change cms-user's password.(oldPassword:"+oldPassword[i]+"&newPassword:"+newPassword[i]+"&confirmPassword:"+confirmPassword[i]+"). Then click Cancel-button.", temp, "Have found successfully!", EtcIO.PassTag);
			}
			else{
				Start2End.InsertHtml("ChangePassword", "Login and start to change cms-user's password.(oldPassword:"+oldPassword[i]+"&newPassword:"+newPassword[i]+"&confirmPassword:"+confirmPassword[i]+"). Then click Cancel-button.", temp, "Have not found!", EtcIO.FailTag);
				DriverUtil.html.ScreenCapture(DriverUtil.driver);
			}
			
			//OK
			if(Start2End.IsElementPresent(By.xpath(ICwmXpath.xcmsChange))){
				DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsChange)).click();
				Start2End.Sleep(1000);
			}
			else{
				continue;
			}
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameOldPwd)).clear();
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameOldPwd)).sendKeys(oldPassword[i]);
			Start2End.Sleep(1000);
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameNewPwd)).clear();
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameNewPwd)).sendKeys(newPassword[i]);
			Start2End.Sleep(1000);
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameConfirmPwd)).clear();
			DriverUtil.driver.findElement(By.name(ICwmXpath.xnameConfirmPwd)).sendKeys(confirmPassword[i]);
			Start2End.Sleep(1000);
			//Start2End.ClickOK(DriverUtil.driver);
			DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsChangeOk)).click();
			Start2End.Sleep(1000);
			temp=DriverUtil.driver.findElement(By.xpath(checkPath)).getText();
			flag=temp.trim().contains(expect[i]);
			if(flag){
				Start2End.InsertHtml("ChangePassword", "Login and start to change cms-user's password.(oldPassword:"+oldPassword[i]+"&newPassword:"+newPassword[i]+"&confirmPassword:"+confirmPassword[i]+"). Then click OK-button.", expect[i], temp, EtcIO.PassTag);
			}
			else{
				Start2End.InsertHtml("ChangePassword", "Login and start to change cms-user's password.(oldPassword:"+oldPassword[i]+"&newPassword:"+newPassword[i]+"&confirmPassword:"+confirmPassword[i]+"). Then click OK-button.", expect[i], temp, EtcIO.FailTag);
				DriverUtil.html.ScreenCapture(DriverUtil.driver);
			}
			}
			catch(Exception e){
				Start2End.InsertHtml("ChangePassword", "Login and start to change cms-user's password.(oldPassword:"+oldPassword[i]+"&newPassword:"+newPassword[i]+"&confirmPassword:"+confirmPassword[i]+").", expect[i], e.getMessage(), EtcIO.ErrorTag);
				DriverUtil.html.ScreenCapture(DriverUtil.driver);
			}
			//DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsChangeOk)).click();
			Start2End.ClickOK(DriverUtil.driver);
			Start2End.Sleep(1000);
			try{
				DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsChangeCancel)).click();
				Start2End.Sleep(1000);
			}catch(Exception ex){				
			}
		}
		Start2End.Logout(this.baseUrl);
	}
	public void CmsUsersTest001_016() throws Exception {
		// 测试数据
		
	}
}
