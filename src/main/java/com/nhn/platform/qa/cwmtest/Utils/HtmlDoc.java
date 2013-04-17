package com.nhn.platform.qa.cwmtest.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebDriver;

public class HtmlDoc {
	protected String DirPath = "";
	protected String IndexList = "<tr><td>$CaseID</td><td>$TaskName</td><td>$TestTime</td><td>$TestSummary</td><td style=\"font-weight:bold;\"><a href=\"$href\"><font color=\"$color\">$TestResult</font></a></td><td>$Comments</td></tr>";
	protected String IndexModel = "";
	protected String DetailModel = "";
	protected String IndexFile = "";
	protected String DetailFile = "";
	protected String CsvFile = "";

	protected String ProjectName = "CMS";
	protected String HttpPath = "http://127.0.0.1/";
	protected String ScriptPath = "http://127.0.0.1/";
	protected String TestDate = "2013-01-16";
	protected String Summary = "";
	protected int Total = 0;
	protected int Passed = 0;
	protected int Failed = 0;

	protected String CaseID = "";
	protected String TaskName = "";
	protected String TestSummary = "";
	protected String TestResult = "";
	protected String href = "";
	protected String color = "";
	protected String Comments = "none";

	protected String Precondition = "";
	protected String Steps = "";
	protected String Expects = "";
	protected String Results = "";
	protected String Remarks = "none";

	public String HomePath;
	private String testTime;

	public String getTestTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH.mm.ss");
		this.testTime = dateFormat.format(now);
		return this.testTime;
	}

	public HtmlDoc() {
		this.ProjectName = EtcIO.readValue("HtmlDoc.ProjectName");
		this.HomePath = EtcIO.readValue("HtmlDoc.HomePath");

		this.IndexModel = EtcIO.readValue("HtmlDoc.IndexModel");
		this.DetailModel = EtcIO.readValue("HtmlDoc.DetailModel");
		this.ScriptPath = EtcIO.readValue("HtmlDoc.ScriptPath");

		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		this.TestDate = dateFormat.format(now);

		this.DirPath = EtcIO.readValue("HtmlDoc.DirPath") + "\\"
				+ this.TestDate;
		this.HttpPath = EtcIO.readValue("HtmlDoc.HttpPath") + "/"
				+ this.TestDate;

		this.IndexFile = this.DirPath + "\\" + "index.html";
		this.CsvFile = this.DirPath + "\\" + "test_result.txt";

		try {
			String temp = EtcIO.readValue("HtmlDoc.IndexList");
			if (temp != null && temp.equals("")) {
				this.IndexList = temp;
			}
		} catch (Exception e) {
		}

		File dirPath = new File(this.DirPath);
		if (dirPath.exists()) {
			String  distFolder = this.DirPath + ".bak." + this.getTestTime();
			try {
				FileToolkit.moveFile(this.DirPath, distFolder);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				dirPath.delete();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dirPath.mkdirs();
		String[] search = new String[] { "$ProjectName", "$HttpPath",
				"$ScriptPath", "$TestDate" };
		String[] replace = new String[] { this.ProjectName, this.HttpPath,
				this.ScriptPath, this.TestDate };
		EtcIO.ReplaceContent(this.IndexModel, this.IndexFile, search, replace);
	}

	public void InsertHtml(String TaskName, String TestSummary,
			String TestResult, String Comments, String Precondition,
			String Steps, String Expects, String Results, String Remarks) {
		this.Total++;
		this.CaseID = this.ProjectName + "-TEST-";
		if (this.Total < 10) {
			this.CaseID += "000" + this.Total;
		} else if (this.Total < 100) {
			this.CaseID += "00" + this.Total;
		} else if (this.Total < 1000) {
			this.CaseID += "0" + this.Total;
		} else {
			this.CaseID += this.Total;
		}
		this.TestResult = TestResult;
		this.TestSummary = TestSummary;
		this.Comments = Comments;
		if (this.TestResult.trim().toLowerCase().equals("pass")) {
			this.Passed++;
			this.color = "GREEN";
		} else {
			this.Failed++;
			this.color = "RED";
		}
		this.TaskName = TaskName;
		this.href = this.HttpPath + "/" + TaskName + "-" + this.CaseID
				+ ".html";
		this.DetailFile = this.DirPath + "\\" + TaskName + "-" + this.CaseID
				+ ".html";
		this.Precondition = Precondition;
		this.Steps = Steps;
		this.Expects = Expects;
		this.Results = Results;
		this.Remarks = Remarks;
		String temprow = this.IndexList.replace("$CaseID", this.CaseID)
				.replace("$TaskName", this.TaskName)
				.replace("$TestTime", this.getTestTime())
				.replace("$TestSummary", this.TestSummary)
				.replace("$href", this.href).replace("$color", this.color)
				.replace("$TestResult", this.TestResult)
				.replace("$Comments", this.Comments);
		EtcIO.AppendContent(this.IndexFile, temprow);
		String[] search = new String[] { "$CaseID", "$TaskName", "$TestTime",
				"$TestSummary", "$color", "$TestResult", "$Comments",
				"$Precondition", "$Steps", "$Expects", "$Results", "$Remarks",
				"$href" };
		String[] replace = new String[] { this.CaseID, this.TaskName,
				this.getTestTime(), this.TestSummary, this.color,
				this.TestResult, this.Comments, this.Precondition, this.Steps,
				this.Expects, this.Results, this.Remarks, this.HttpPath };
		EtcIO.ReplaceContent(this.DetailModel, this.DetailFile, search, replace);
		EtcIO.AppendContent(this.CsvFile, this.CaseID + "`" + this.TaskName
				+ "`" + this.TestSummary + "`" + this.Precondition + "`"
				+ this.Steps + "`" + this.Expects + "`" + this.getTestTime()
				+ "`" + this.Results + "`" + this.TestResult + "`"
				+ this.Remarks + "\r\n");
	}

	public void CompleteCount() {
		String[] search = new String[] { "$Total", "$Passed", "$Failed" };
		String[] replace = new String[] { "" + this.Total, "" + this.Passed,
				"" + this.Failed };
		EtcIO.ReplaceContent(this.IndexFile, this.IndexFile, search, replace);
		this.Summary = "PassRate: "
				+ String.format("%.2f",
						((double) this.Passed / this.Total) * 100)
				+ "% \t CompleteRate: 100%";
		EtcIO.ReplaceContent(this.IndexFile, this.IndexFile,
				new String[] { "$Summary" }, new String[] { this.Summary });
	}

	public void ScreenCapture() {
		if (!this.DetailFile.equals("")) {
			SnapShot.screenShoot(this.DirPath, this.CaseID + ".png",
					this.DetailFile);
		}
	}

	public void ScreenCapture(WebDriver driver) {
		if (!this.DetailFile.equals("")) {
			SnapShot.appendSnapShot(driver, this.DirPath, this.CaseID + ".png",
					this.DetailFile);
		}
	}

	public void ScreenCapture(String imagePath) {
		if (!this.DetailFile.equals("")) {
			SnapShot.appendSnapShotToLogFile(imagePath, this.DetailFile);
		}
	}
}
