package com.presto.driver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelLibrary 
{
	AppProperties sp = new AppProperties();
	String path1 = sp.getExcelPath();
	
	public HashMap<String, String> getExcel(String sheetName) throws Exception
	{
		HashMap<String, String> returnData = new HashMap<String, String>();
		FileInputStream fis = new FileInputStream(path1);
		Workbook wb = WorkbookFactory.create(fis);
		Sheet s = wb.getSheet(sheetName);
		int rowCount = getRowCount(sheetName);
		
		for (int i = 0; i <= rowCount; i++)
		{
			String keyName = "";
			String keyValue = "";
			Row r = s.getRow(i);
			Cell c1 = r.getCell(0);
			keyName = c1.getStringCellValue();
			Cell c2 = r.getCell(1);
			keyValue = c2.getStringCellValue();
			returnData.put(keyName, keyValue);			
		}
		
		return returnData;		
	}

	public String getExcelData(String sheetName, int rowNum, int colNum) 
	{
		String retVal = null;
		try 
		{
			FileInputStream fis = new FileInputStream(path1);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(sheetName);
			Row r = s.getRow(rowNum);
			Cell c = r.getCell(colNum);
			retVal = c.getStringCellValue();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidFormatException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return retVal;
	}

	public int getRowCount(String sheetName) {
		int retVal = 0;
		try 
		{
			FileInputStream fis = new FileInputStream(path1);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(sheetName);
			retVal = s.getLastRowNum();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidFormatException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return retVal;
	}

	public void writeToExcel(String sheetName, int rowNum, int colNum, String desc) 
	{
		try 
		{
			FileInputStream fis = new FileInputStream(path1);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(sheetName);
			Row r = s.getRow(rowNum);
			Cell c = r.createCell(colNum);
			c.setCellValue(desc);
			FileOutputStream fos = new FileOutputStream(path1);
			wb.write(fos);
			fos.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidFormatException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}