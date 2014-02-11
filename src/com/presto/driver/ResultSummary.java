package com.presto.driver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import org.openqa.selenium.WebDriver;

public class ResultSummary 
{
	public static String pth = "";
	public static int failcounter;
	public static int slno = 1;
	public String Snapshotpath;
	public static int screencapcounter = 1;
	public AppProperties app = new AppProperties();

	public void createSummaryFile() throws IOException 
	{
		try 
		{
			String sfile = app.getResultPath();
			System.out.println("sfile " + sfile);
			Calendar c = new GregorianCalendar();
			String currtime = c.getTime().toString();

			String Browser = app.getBrowserType();
			String Url = app.getTestURL();
			String Os = app.getOS();
			String BrowserVersion = app.getBrowserVersion();

			BufferedWriter bw1 = new BufferedWriter(new FileWriter(sfile
					+ "/Summary.html"));
			bw1.write("<html>");
			bw1.write("<head>");
			bw1.write("<title>Result Summary</title>");
			bw1.write("</head><body>");

			bw1.write("<table style=font-family:arial border =1 cellspacing=1 frame=Vsides bgcolor=#CC9999 Align=Center>");
			bw1.write("<tr><th width=775>Presto : Automated Test Script Execution Summary Report</th></tr>");
			bw1.write("</table>");

			bw1.write("<table style=font-family:calibri border =1 cellspacing=1 frame=Vsides bgcolor=6699FF Align=Center>");
			bw1.write("<tr><td  width=500 ><B>Execution Started Time</B></td><td width=270><B>"
					+ currtime + "</B></td></tr>");
			bw1.write("<tr><td  width=500 ><B>Environment </B></td><td width=270><B>"
					+ Url + "</B></td></tr>");
			bw1.write("<tr><td  width=500 ><B>Browser </B></td><td width=270><B>"
					+ Browser + "</B></td></tr>");
			bw1.write("<tr><td  width=500 ><B>Browser Version </B></td><td width=270><B>"
					+ BrowserVersion + "</B></td></tr>");
			bw1.write("<tr><td  width=500 ><B>Operating System </B></td><td width=270><B>"
					+ Os + "</B></td></tr>");
			bw1.write("<tr><td  width=500 ><B>Automation Tool</B></td><td width=270><B>Selenium</B></td></tr></table><br/>");
			bw1.write("<table style=font-family:calibri border =2 cellspacing=1 frame=Vsides bgcolor=#E8E8E8 Align=Center><tr><td Align=Center width=50><B>SL NO</B></td><td Align=center width=632><B>TestScript Name</B></td><td width=80 Align=center><B>Result</B></td><td width=80 Align=center><B>Rally TC No.</B></td></tr>");
			bw1.flush();
			bw1.close();
			
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
	}

	/**
	 * <p>
	 * <b>Result summary function name :</b> writePassSummary
	 * </p>
	 * <p>
	 * <b>Description :</b> Writing Pass Message to the html log file
	 * </p>
	 * 
	 * @param scriptname
	 * @param status
	 * @throws IOException
	 */
	public void writePassSummary(String scriptname, String status, String TestID) throws IOException 
	{
		try 
		{
			String rpath = app.getResultPath();
			System.out.println("rpath    " + rpath);
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(rpath
					+ "/summary.html", true));
			bw1.write("<tr><td Align=center>" + slno + "</td><td><a href="
					+ scriptname + ".html>" + scriptname
					+ "</a></td><td bgcolor=#00FF00 Align=center>" + status
					+ "</td><td bgcolor=#00FF00 Align=center>" + TestID
					+ "</td></tr>");
			slno++;
			bw1.flush();
			bw1.close();
			rpath=null;
		} 
		catch (Exception e) 
		{
			System.out.println("Write PassSummary error = " + e);
		}
	}

	/**
	 * <p>
	 * <b>Result summary function name :</b> writeFailSummary
	 * </p>
	 * <p>
	 * <b>Description :</b> Writing Fail Message to the html log file
	 * </p>
	 * 
	 * @param scriptname
	 * @param status
	 * @throws IOException
	 */
	public void writeFailSummary(String scriptname, String status, String TestID ) throws IOException 
	{
		try 
		{
			String rpath = app.getResultPath();
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(rpath
					+ "/summary.html", true));
			bw1.write("<tr><td Align=center>" + slno + "</td><td><a href="
					+ scriptname + ".html>" + scriptname
					+ "</a></td><td bgcolor=#FF0000 Align=center>" + status
					+ "</td><td bgcolor=#00FF00 Align=center>" + TestID
					+ "</td></tr>");
			slno++;
			bw1.flush();
			bw1.close();
			rpath=null;
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
	}
	
	/* Let's try writing to the GoogleDoc */
	public void writeToGoogleDoc(GoogleDoc googleDoc, String sheet, String cell, String messageCell, String error) throws Exception
	{
		googleDoc.setWorksheet(sheet);
		
		String value = "";
		String message = "";	
		
		if(failcounter > 0)
		{
			value = "FAIL";
			message = error;
		}
		else
		{
			value = "PASS";
			message = "None";
		}
		
		googleDoc.updateCellValue(cell, value);
		googleDoc.updateCellValue(messageCell, message);
		
		
	}

	public void createLogFile(String scriptname) throws IOException 
	{
		try 
		{
			String rpath = app.getResultPath();
			Calendar c = new GregorianCalendar();
			String currtime = c.getTime().toString();
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(rpath + "/"
					+ scriptname + ".html"));
			bw1.write("<html><head><title>"
					+ scriptname
					+ "</title></head><body><table style=font-family:calibri border =1 cellspacing=1 frame=Vsides  bgcolor=6699FF Align=Center><tr><td><B>Test Case Name</B></td><td><B>"
					+ scriptname + "</B></td></tr>");
			bw1.write("<tr><td width=500 ><B>Execution Start Time</B></td><td width=270><B>"
					+ currtime + "</B></td></tr></table><br/>");
			bw1.write("<table style=font-family:calibri border =1 cellspacing=1 frame=Vsides bgcolor=#E8E8E8 Align=Center><tr><td Align=center><B>Step Description</B></td><td Align=center><B>Result</B></td>"
					+ "<td Align=Center><B>End Time</B></td></tr>");
			bw1.close();
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
	}
	
	public void closeLogFile(String scriptname) throws IOException
	{
		try 
		{
			String rpath = app.getResultPath();
			Calendar c = new GregorianCalendar();
			String currtime = c.getTime().toString();
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(rpath + "/"
					+ scriptname + ".html", true));
			bw1.write("<html><head><title>"
					+ scriptname
					+ "</title></head><body><table style=font-family:calibri border =1 cellspacing=1 frame=Vsides  bgcolor=6699FF Align=Center><tr><td><B>Test Case Name</B></td><td><B>"
					+ scriptname + "</B></td></tr>");
			bw1.write("<tr><td width=500 ><B>Execution End Time</B></td><td width=270><B>"
					+ currtime + "</B></td></tr></table><br/>");
			bw1.close();
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
	}

	/**
	 * <p>
	 * <b>Generic function name : </b>writePassLog
	 * </p>
	 * <p>
	 * <b>Description : </b>Writing Pass message to the HTML log file
	 * </p>
	 * 
	 * @param desc
	 * @throws IOException
	 */
	public void writePassLog(String scriptname, String desc) throws IOException 
	{
		try 
		{
			String rpath = app.getResultPath();
			Calendar c = new GregorianCalendar();
			String currtime = c.getTime().toString();
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(rpath + "/"
					+ scriptname + ".html", true));
			bw1.write("<tr><td width=418>"
					+ desc
					+ "</td><td width=78 bgcolor=#00FF00 Align=center >PASS</td>"
					+ "<td width=270 Align=left>" + currtime + "</td></tr>");
			bw1.close();
		} 
		catch (Exception e) 
		{
			System.out.println("Error = " + e);
		}
	}

	public void writeFailLog(String scriptname, String desc, WebDriver driver) throws Exception 
	{
		try 
		{
			String rpath = app.getResultPath();
			Calendar c = new GregorianCalendar();
			String currtime = c.getTime().toString();
			Snapshotpath=rpath;
			if(driver != null)
			{
				CaptureScreenshot(scriptname, driver);
			}
			
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(rpath + "/" + scriptname + ".html", true));
			bw1.write("<tr><td width=418><a href=" + Snapshotpath + ">" + desc + "</a></td><td width=78 bgcolor=#FF0000 Align=center >FAIL</td></td>"
					+ "<td width=270 Align=left>" + currtime + "</td></tr>");
			bw1.close();
			failcounter++;
		} 
		catch (Exception e) 
		{
			System.out.println("Write Failed Log Exception = " + e);
		}
	}
	

	public void CaptureScreenshot(String scriptname, WebDriver driver) throws Exception 
	{
		String rpath = app.getResultPath();
		try {
			File srcFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);

			Snapshotpath = rpath + scriptname + screencapcounter + ".png";
			System.out.println("snapshot path " + Snapshotpath);
			srcFile.renameTo(new File(Snapshotpath));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		screencapcounter++;
	}
}