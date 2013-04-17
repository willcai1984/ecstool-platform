/********************
 * @title DriverUtil.java
 * @package com.nhn.platform.qa.cwmtest.Utils
 * @description 
 * @author cn15291(Frank Wu)
 * @update 2013-1-25 上午10:21:08
 * @version V1.0
 ********************/

package com.nhn.platform.qa.cwmtest.Utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/********************
 * @description
 * @version 1.0
 * @author cn15291(Frank Wu)
 * @update 2013-1-25 上午10:21:08
 ********************/

public class DriverUtil {

	private static ChromeDriverService chservice;
	private static InternetExplorerDriverService ieservice;
	private static Dimension screenDims = Toolkit.getDefaultToolkit()
			.getScreenSize();

	public static HtmlDoc html = new HtmlDoc();
	public static WebDriver driver;

	// public static void StartService(int browserType) {
	// switch(browserType){
	// case BrowserType.ChromeType:
	// case BrowserType.ChromeFirefox:
	// StartChromeService();
	// break;
	// case BrowserType.IEType:
	// case BrowserType.IEFirefox:
	// StartIEService();
	// break;
	// case BrowserType.ChromeIE:
	// case BrowserType.ChromeIEFirefox:
	// StartChromeService();
	// StartIEService();
	// break;
	// default:
	// break;
	// }
	// }
	private static void StartChromeService() {
		if (EtcIO.chromeDriverPath == null || EtcIO.chromeDriverPath.equals("")) {
			EtcIO.chromeDriverPath = "conf/chromedriver";
		}
		chservice = new ChromeDriverService.Builder()
		 		.usingDriverExecutable(new File(EtcIO.chromeDriverPath))
				.usingAnyFreePort().build();
		try {
			System.setProperty("webdriver.chrome.driver",
					EtcIO.chromeDriverPath);
			chservice.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void StartIEService() {
		if (EtcIO.IEDriverPath == null || EtcIO.IEDriverPath.equals("")) {
			EtcIO.IEDriverPath = "conf/IEDriverServer.exe";
		}
		ieservice = new InternetExplorerDriverService.Builder()
				.usingDriverExecutable(new File(EtcIO.IEDriverPath))
				.usingAnyFreePort().build();
		try {
			System.setProperty("webdriver.ie.driver", EtcIO.IEDriverPath);
			ieservice.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void StopService() {
		if (chservice != null && chservice.isRunning()) {
			chservice.stop();
		}
		if (ieservice != null && ieservice.isRunning()) {
			ieservice.stop();
		}
	}

	public static void ChromeStart() {
		QuitDriver();
		if (chservice == null) {
			StartChromeService();
		}
		driver = new RemoteWebDriver(chservice.getUrl(),
				DesiredCapabilities.chrome());
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		MaxiMise(driver);
	}

	public static void IEStart() {
		QuitDriver();
		if (ieservice == null) {
			StartIEService();
		}
		DesiredCapabilities ieCapabilities = DesiredCapabilities
				.internetExplorer();
		ieCapabilities
				.setCapability(
						InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
		driver = new RemoteWebDriver(ieservice.getUrl(), ieCapabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		MaxiMise(driver);
	}

	public static void FirefoxStart() {
		QuitDriver();
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		MaxiMise(driver);
	}

	public static void QuitDriver() {
		if (driver != null) {
			driver.quit();
		}
	}

	// 使窗口最大化函数
	public static void MaxiMise(WebDriver driver) {
		final JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.open('','testwindow','width=400,height=200')");
		driver.close();
		driver.switchTo().window("testwindow");
		js.executeScript("window.moveTo(0,0);");
		int width = (int) screenDims.getWidth();
		int height = (int) screenDims.getHeight();
		/*
		 * 1280和1024分别为窗口的宽和高，可以用下面的代码得到 screenDims =
		 * Toolkit.getDefaultToolkit().getScreenSize(); width = (int)
		 * screenDims.getWidth(); height = (int) screenDims.getHeight();
		 */
		js.executeScript("window.resizeTo(" + width + "," + height + ");");
	}
}
