
/********************
* @title FrameTest.java
* @package com.nhn.platform.qa.cwmtest.firefox
* @description 
* @author cn15291(Frank Wu)
* @update 2013-1-30 下午01:46:42
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
 * @update 2013-1-30 下午01:46:42
 ********************/

public class FrameTest extends TestCase {
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();

	public FrameTest(String name) {
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
		TestSuite suite = new TestSuite("Test for FrameTest");
		// $JUnit-BEGIN$
		suite.addTest(new FrameTest("FrameTest001_005"));
		suite.addTest(new FrameTest("MonitorsTest001_008"));
		// $JUnit-END$
		return suite;
	}

	public void FrameTest001_005() throws Exception
    {
        //测试数据
        String[] expect = new String[] { "//div[@class='x-panel x-column x-panel-default']//img[@src='resources/images/s_db.png']", 
                                        "//div[@class='x-panel x-column x-panel-default']//img[@src='resources/images/s_vars.png']", 
                                        "//div[@class='x-panel x-column x-panel-default']//img[@src='resources/images/broker.png']" ,
                                        "//div[@class='x-panel x-column x-panel-default']//img[@src='resources/images/status_group.png']",
                                        "//button[@data-qtip='Report bug']"};

        //登录
        Start2End.Login(this.baseUrl);

        for (int i = 0; i < expect.length; i++)
        {
        	try{
            DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsHome)).click();
            Start2End.Sleep(1000);
            boolean flag=Start2End.IsElementPresent(By.xpath(expect[i]));
            Start2End.Sleep(1000);
            if(flag){
            	Start2End.InsertHtml("FrameDisplay", "Login and enter into home page.", expect[i], "Have found successfully!", EtcIO.PassTag);
            }
            else{
            	Start2End.InsertHtml("FrameDisplay", "Login and enter into home page.", expect[i], "Have not found!", EtcIO.FailTag);
            	DriverUtil.html.ScreenCapture(DriverUtil.driver);
            }
        	}catch(Exception e){
        		Start2End.InsertHtml("FrameDisplay", "Login and enter into home page.", expect[i], e.getMessage(), EtcIO.FailTag);
            	DriverUtil.html.ScreenCapture(DriverUtil.driver);
        	}
        }

        //退出
        Start2End.Logout(baseUrl);
    }
    public void MonitorsTest001_008()
    {
    	String[] clicks= new String[] {"//div[@id='chartsBox']//following::button[1]",
    			"//div[@id='chartsBox']//following::button[2]",
    			"//div[@id='chartsBox']//following::button[3]",
    			"//div[@id='chartsBox']//following::button[4]",
    			"//div[@id='chartsBox']//following::button[5]",
    			"//div[@id='cpuGaugeInView']",
    			"//div[@id='memGaugeInView']",
    			"//div[@id='spaceGaugeInView']"};
        //测试数据
        String[] expect = new String[] {"//span[text()='CPU Detail']",
        		"//span[text()='Memory Detail']",
        		"//span[text()='Disk Space Detail']",
        		"//label[text()='Disk Space']",
        		"//label[text()='Database Status']",
        		"//span[text()='CPU Detail']",
        		"//span[text()='Memory Detail']",
        		"//span[text()='Disk Space Detail']"};
        
        Start2End.Login(baseUrl);
       
        DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsMonitor)).click();
        Start2End.Sleep(5000);

        for(int i=0;i<clicks.length;i++){
        	try{
        	DriverUtil.driver.findElement(By.xpath(clicks[i])).click();
        	Start2End.Sleep(1000);
        	boolean flag=Start2End.IsElementPresent(By.xpath(expect[i]));
        	if(flag){
        		Start2End.InsertHtml("MonitorsTest", "Login and enter into home page. Click the Monitors-button. Then click the button: "+clicks[i], expect[i], "Have found successfully!", EtcIO.PassTag);
            }
            else{
            	Start2End.InsertHtml("MonitorsTest", "Login and enter into home page. Click the Monitors-button. Then click the button: "+clicks[i], expect[i], "Have not found!", EtcIO.FailTag);
            	DriverUtil.html.ScreenCapture(DriverUtil.driver);
            }
        	}catch(Exception e){
        		Start2End.InsertHtml("MonitorsTest", "Login and enter into home page. Click the Monitors-button. Then click the button: "+clicks[i], expect[i], e.getMessage(), EtcIO.FailTag);
            	DriverUtil.html.ScreenCapture(DriverUtil.driver);
        	}
        	try{
        		DriverUtil.driver.findElement(By.xpath(ICwmXpath.xcmsClose)).click();
        		Start2End.Sleep(1000);
        	}catch(Exception e){        		
        	}
        }
        Start2End.Logout(baseUrl);
    }
}
