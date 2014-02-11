package com.presto.driver;

import java.util.ArrayList;

public class Scripts {

	ExcelLibrary excelLib = new ExcelLibrary();

	public ArrayList<String> getScripts() 
	{
		ArrayList testList = new ArrayList();
		int step1 = excelLib.getRowCount("Scenarios");

		for (int i = 1; i <= step1; i++) 
		{
			String status = excelLib.getExcelData("Scenarios", i, 2);
			if (status.equalsIgnoreCase("yes")) 
			{
				String testName = excelLib.getExcelData("Scenarios", i, 1);
				testList.add(testName);
			}
			System.out.println("scenarios " + status);
		}

		return testList;
	}

}