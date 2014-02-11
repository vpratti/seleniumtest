package com.presto.driver;

import java.util.*;

public class CommonData 
{
	public List<Browser> browsers;
	
	public CommonData()
	{
		ExcelLibrary excel = new ExcelLibrary();
		
		browsers = new ArrayList<Browser>();
		String browserType = "";
		String browserVersion = "";		 

		int step1 = excel.getRowCount("Browsers");

		for (int i = 1; i <= step1; i++) 
		{
			String status = excel.getExcelData("Browsers", i, 2);
			if (status.equalsIgnoreCase("yes")) 
			{
				Browser browser = new Browser();
				browser.browserType = excel.getExcelData("Browsers", i, 0);
				browser.browserVersion = excel.getExcelData("Browsers", i, 1);
				browsers.add(browser);
			}
			System.out.println("browsers " + browserType+ " = " + status);
		}
	}
	
	public String getKeyValue(HashMap<String, String> map, String keyName)
	{
		String keyValue = "";
		
		for (Map.Entry<String, String> key : map.entrySet())
		{
			if (key.getKey().equalsIgnoreCase(keyName))
			{
				keyValue = key.getValue();
			}
		}
		
		return keyValue;
	}
	
	public class Browser
	{
		public String browserType;
		public String browserVersion;	
		
		public Browser(String type, String version)
		{
			browserType = type;
			browserVersion = version;
		}
		
		public Browser()
		{
			
		}
	}

}
