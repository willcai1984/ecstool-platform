package com.nhn.platform.qa.cwmtest.Utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Properties;

public class EtcIO {

	static String confPath = "conf/conf.properties";
	static int browserType = Integer.parseInt(readValue("browser.type")); 
	// 0 -
	// chrome,
	// others
	// -
	// firefox

	// chrome driver path
	public static String chromeDriverPath = readValue("browser.chromeDriverPath");
	// ie driver path
	public static String IEDriverPath = readValue("browser.IEDriverPath");

	// Business Server IP Address and port number
	public static String baseUrl = readValue("cwmServer.BaseUrl");

	// admin user and password
	public static String userName = readValue("userName");
	public static String userPwd = readValue("userPwd");
	public static String language = readValue("Language");

	// log jQuery dir
	public static String jQueryPath = readValue("jQuery.path");
	// Log Small Pic size
	public static String logPath = readValue("result.path");
	public static int logPicWidth = Integer.parseInt(readValue("result.picWidth"));
	public static int logPicHeight = Integer.parseInt(readValue("result.picHeight"));
	
	// XML file tag name
	public static String RunTag = "Run";
	public static String PassTag = "Pass";
	public static String FailTag = "Fail";
	public static String ErrorTag = "Error";

	public static String readValue(String key) {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					confPath));
			props.load(in);
			String value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void ReplaceContent(String sourcePath, String targetPath,
			String[] search, String[] replace) {
		try {
			FileReader reader = new FileReader(sourcePath);
			char[] dates = new char[1024];
			int count = 0;
			StringBuilder sb = new StringBuilder();
			while ((count = reader.read(dates)) > 0) {
				String str = String.valueOf(dates, 0, count);
				sb.append(str);
			}
			reader.close();
			// 从构造器中生成字符串，并替换搜索文本
			String str = sb.toString();
			for (int i = 0; i < search.length; i++) {
				str = str.replace(search[i], replace[i]);
			}
			FileWriter writer = new FileWriter(targetPath);
			writer.write(str.toCharArray());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 追加文件：使用RandomAccessFile
	 * 
	 * @param fileName
	 *            文件名
	 * @param content
	 *            追加的内容
	 */
	public static void AppendContent(String fileName, String content) {
		RandomAccessFile randomFile = null;
		try {
			// 打开一个随机访问文件流，按读写方式
			randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
// import java.io.BufferedWriter;
// import java.io.File;
// import java.io.FileOutputStream;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.io.OutputStreamWriter;
// import java.io.RandomAccessFile;
//
// /**
// *
// * @author malik
// * @version 2011-3-10 下午10:49:41
// */
// public class AppendFile {
//
// public static void method1(String file, String conent) {
// BufferedWriter out = null;
// try {
// out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,
// true)));
// out.write(conent);
// } catch (Exception e) {
// e.printStackTrace();
// } finally {
// try {
// if(out != null){
// out.close();
// }
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// }
//
// /**
// * 追加文件：使用FileWriter
// *
// * @param fileName
// * @param content
// */
// public static void method2(String fileName, String content) {
// FileWriter writer = null;
// try {
// // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
// writer = new FileWriter(fileName, true);
// writer.write(content);
// } catch (IOException e) {
// e.printStackTrace();
// } finally {
// try {
// if(writer != null){
// writer.close();
// }
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// }
//
// /**
// * 追加文件：使用RandomAccessFile
// *
// * @param fileName 文件名
// * @param content 追加的内容
// */
// public static void method3(String fileName, String content) {
// RandomAccessFile randomFile = null;
// try {
// // 打开一个随机访问文件流，按读写方式
// randomFile = new RandomAccessFile(fileName, "rw");
// // 文件长度，字节数
// long fileLength = randomFile.length();
// // 将写文件指针移到文件尾。
// randomFile.seek(fileLength);
// randomFile.writeBytes(content);
// } catch (IOException e) {
// e.printStackTrace();
// } finally{
// if(randomFile != null){
// try {
// randomFile.close();
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// }
// }
// }
