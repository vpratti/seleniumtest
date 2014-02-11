package com.presto.driver;

import java.util.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import com.presto.driver.*;

public class GoogleDoc 
{
	AppProperties app = null;
	SpreadsheetService svc = null;
	URL sheetURL = null;
	String sheetAddress = null;
	SpreadsheetFeed feed = null;
	WorksheetEntry dataTab = null;
	CellFeed cellFeed = null;
	List<WorksheetEntry> worksheetList = null;
	SpreadsheetEntry sheet = null;
	
	public GoogleDoc() throws Exception
	{
		app = new AppProperties();
		
		svc = new SpreadsheetService("SheetService");			
		svc.setUserCredentials("gannettautomation", "withinreach");
		sheetAddress = "https://spreadsheets.google.com/feeds/spreadsheets/private/full/";
		sheetURL = new URL(sheetAddress);
		feed = svc.getFeed(sheetURL,  SpreadsheetFeed.class);

		List<SpreadsheetEntry> spreadsheetList = feed.getEntries();		
		for (SpreadsheetEntry ssEntry : spreadsheetList)
		{
			if(ssEntry.getTitle().getPlainText().equalsIgnoreCase(app.getGoogleDoc()))
			{
				sheet = ssEntry;
			}
		}

		worksheetList = sheet.getWorksheets();
	}
	
	public void setWorksheet(String sheetName) throws Exception
	{
		for (WorksheetEntry worksheet : worksheetList)
		{
			if(worksheet.getTitle().getPlainText().equalsIgnoreCase(sheetName))
			{
				dataTab = worksheet;
			}
		}
	}
	
	public String getCellValue(String worksheetName, String cellLocation) throws Exception
	{
		String cellValue = "";
		setWorksheet(worksheetName);
		
		worksheetList = sheet.getWorksheets();
		
		URL cellFeedURL = dataTab.getCellFeedUrl();			
		cellFeed = svc.getFeed(cellFeedURL, CellFeed.class);	
		
		for (CellEntry cell : cellFeed.getEntries())
		{
			if(cell.getTitle().getPlainText().equalsIgnoreCase(cellLocation))
			{
				cellValue = cell.getCell().getValue();
				return cellValue;
			}
		}
		
		return cellValue;		
	}
	
	public boolean updateCellValue(String cellLocation, String value) throws Exception
	{
		URL cellFeedURL = dataTab.getCellFeedUrl();
		cellFeed = svc.getFeed(cellFeedURL, CellFeed.class);
		
		for (CellEntry cell : cellFeed.getEntries())
		{
			if(cell.getTitle().getPlainText().equalsIgnoreCase(cellLocation))
			{
				cell.changeInputValueLocal(value);
				cell.update();
				return true;
			}
		}
		
		return false;
	}
}
