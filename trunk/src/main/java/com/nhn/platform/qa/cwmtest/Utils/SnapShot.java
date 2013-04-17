/*********************************************************************************
 *Copyright(C),2012-2013,NHN
 *FileName:     SnapShot.java
 *Author:       CN15350(Fred)
 *Date:         27/11/2012
 *Description:  Snapshot the whole window and save it to log file.

 *                
 *Modify History:
     1.Date:
       Author:
       Modification:
     2.��������
 **********************************************************************************/

package com.nhn.platform.qa.cwmtest.Utils;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.OutputType;

public class SnapShot {

	static boolean __Debug = false;

	static String imageFormat = "png"; // image format
	static int serialNum = 0;
	static Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

	static void Initilize() {
		serialNum = 0;
	}

	/****************************
	 * snapShot the whole screen *
	 ****************************/
	public static void screenShoot(String dirPath,String picName,String htmlPath) {

		try {
			serialNum++;
			// copy a screen shoot capture to a BufferedImage object screens
			// hoot
			BufferedImage screenShoot = (new Robot()).createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
			String picPathName = dirPath + "\\images\\" + picName;
			File dir=new File(dirPath + "\\images");
			if(!dir.exists()){
			dir.mkdirs();
			}
			File f = new File(picPathName);
			if (__Debug) {
				System.out.print("Save File " + picName);
			}
			// Write to file
			ImageIO.write(screenShoot, imageFormat, f);

			if (__Debug) {
				System.out.print("..Finished!\n");
			}

			appendSnapShotToLogFile("images/"+picName,htmlPath);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public static void appendSnapShotToLogFile(String imageName,String htmlPath) {
		String content = "";
		content += "<table width=\"90%\"><tr><td>\n";
		//content += "<img src=\"" + imageName + "\" width=\"" + EtcIO.logPicWidth + "\" height=\"" + EtcIO.logPicHeight + "\" onclick=\"showPic(this);\" />\n";
		content += "<img src=\"" + imageName + "\" />\n";
		content += "</td></tr></table>\n";

		EtcIO.AppendContent(htmlPath, content);
	}
	public static void appendSnapShot(WebDriver driver,String dirPath,String picName,String htmlPath) {
		File screenShotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(screenShotFile,new File(dirPath + "\\images\\" + picName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String content = "\n<br /><a href=\"images/"+picName+"\" target=\"_blank\"><img src=\"images/"+picName+"\" width=\"" + EtcIO.logPicWidth + "\" height=\"" + EtcIO.logPicHeight + "\" onclick=\"showPic(this);\" /></a>";
		EtcIO.AppendContent(htmlPath, content);
	}

}