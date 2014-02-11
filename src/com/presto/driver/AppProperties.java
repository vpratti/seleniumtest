package com.presto.driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class AppProperties 
{
	private FileInputStream configFile=null;
	private Properties testConfigFile=null;
	
	private String browserType;
	private String environment;
	private String browserVersion;
	private String testURL;
	private String OS;
	private String excelPath;
	private String chromeDriver;
	private String resultPath;
	private String googleDoc;
	
	public AppProperties()
	{
		try
		{
			configFile=new FileInputStream(".\\config.properties");
			testConfigFile=new Properties();
			testConfigFile.load(configFile);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public String getBrowserType()
	{
		browserType=testConfigFile.getProperty("browserType");
		return browserType;
	}
	
	public String getEnvironment()
	{
		environment = testConfigFile.getProperty("Env");
		return environment;
	}
	
	public String getBrowserVersion()
	{
		browserVersion = testConfigFile.getProperty("BrowserVersion");
		return browserVersion;
	}
	
	public String getTestURL()
	{
		if (getEnvironment().equalsIgnoreCase("dev"))
		{
			testURL = testConfigFile.getProperty("devtesturl");
		}
		else if (getEnvironment().equalsIgnoreCase("qa"))
		{
			testURL = testConfigFile.getProperty("qatesturl");
		}
		else if (getEnvironment().equalsIgnoreCase("stage"))
		{
			testURL = testConfigFile.getProperty("stagetesturl");
		}
		else if (getEnvironment().equalsIgnoreCase("multisite"))
		{
			testURL = testConfigFile.getProperty("multisitetesturl");
		}
		else
		{
			testURL = testConfigFile.getProperty("trainingtesturl");
		}
		
		return testURL;
	}
	
	public String getOS()
	{
		OS = testConfigFile.getProperty("OS");
		return OS;
	}
	
	public String getExcelPath()
	{
		excelPath = testConfigFile.getProperty("excelPath");
		return excelPath;
	}
	
	public String getChromeDriver()
	{
		chromeDriver = testConfigFile.getProperty("chromeDriver");
		return chromeDriver;
	}
	
	public String getResultPath()
	{
		resultPath = testConfigFile.getProperty("resultPath");
		return resultPath;
	}
	
	public String getGoogleDoc()
	{
		googleDoc = testConfigFile.getProperty("googleDoc");
		return googleDoc;
	}
}

