
/********************
* @title ICwmXpath.java
* @package com.nhn.platform.qa.cwmtest.cwmautotest
* @description 
* @author cn15291(Frank Wu)
* @update 2013-1-28 上午09:30:53
* @version V1.0
********************/
	
package com.nhn.platform.qa.cwmtest.cwmautotest;


/********************
 * @description 
 * @version 1.0
 * @author cn15291(Frank Wu)
 * @update 2013-1-28 上午09:30:53
 ********************/

public interface ICwmXpath {
    //Server host admin
	public static String xcmsUser="//input[@name='username']";	
	public static String xcmsPassword="//input[@name='password']";	
	public static String xcmsLaguage="//li[text()='English']";
	public static String xcmsLogin="//span[@class='x-btn-inner'][text()='Login']";
	
	//change password & logout
	public static String xcmsHome="//button[@data-qtip='Home']";
	public static String xcmsLogout="//button[@data-qtip='Logout']";
	public static String xcmsChange="//button[@data-qtip=\"Change administrator's password\"]";
	public static String xcmsPopOk="//span[@class='x-btn-inner'][text()='OK']";
	public static String xcmsChangeOk="//span[text()='Cancel']//following::button[@class='x-btn-center'][1]";
	public static String xcmsPopCancel="//span[@class='x-btn-inner'][text()='Cancel']";
	public static String xcmsChangeCancel="//span[text()='Cancel']//following::button[@class='x-btn-center'][2]";
	public static String xcmsPopYes="//span[@class='x-btn-inner'][text()='Yes']";
	public static String xcmsPopNo="//span[@class='x-btn-inner'][text()='No']";
	
	public static String xnameUser="username";
	public static String xnamePassword="password";
	public static String xnameOldPwd="oldPassword";
	public static String xnameNewPwd="newPassword";
	public static String xnameConfirmPwd="confirmpassword";
	//*********************************	
	//frame test
	public static String xcmsMonitor="//div[@class='x-panel x-column x-panel-default']//img[@src='resources/images/status_group.png']";
	public static String xcmsClose="//following::img[@class='x-tool-close'][2]";
	//********************************
	//users test
	public static String usersCmsx="//img[@src='resources/images/s_rights.png']";
	public static String userAddCmsx="//button[@data-qtip='Add a new user']";
	public static String userSaveCmsx="//button[@data-qtip='Save']";
	public static String userCancelCmsx="//button[@data-qtip='Cancel']";
	
}
