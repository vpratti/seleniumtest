package com.presto.testscripts;

import org.testng.annotations.Test;

import com.presto.common.*;
import com.presto.driver.*;
import com.presto.driver.CommonData.Browser;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.*;

public class PRST_CONT_001  
{
	CommonFunctions commonFunctions = new CommonFunctions();
	CommonData common = new CommonData();
	SeleniumProperties sp = null;
	AppProperties app = null;
	String testStatus = "Fail";
	String errorMessage = "";
	int step;
	ResultSummary results;
	List<Browser> browsers;
	int testCount = 0;
	HashMap<String, String> dataSource;
	GoogleDoc googleDoc;
	String resultSheet = "";
	String resultCell = "";
	String resultMessage = "";
	ExcelLibrary excel = null;
	
	@Test
	public void PRST_CONT_001() throws Exception
	{	
		String testName = getClass().getSimpleName();
		results = new ResultSummary();
		results.createLogFile(testName);
		
		//Initialize Test Variables	
		try
		{	
			excel = new ExcelLibrary();		
			dataSource = excel.getExcel(testName);			
			testCount = dataSource.size();
			
			if(testCount == 0)
			{
				throw new Exception("Could not find any data to run the test.  Check the " + testName + " page.");
			}		
			
			browsers = common.browsers;
			if(browsers.isEmpty())
			{
				throw new Exception("No browsers set to run.");
			}
			
			googleDoc = new GoogleDoc();
			if(googleDoc == null)
			{
				throw new Exception("Google Doc not found!");
			}
		}
		catch (Exception ex)
		{
			errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
			results.writeFailLog(testName, errorMessage, null);
			throw new Exception(errorMessage);
		}
		
		//Run the Test
		try
		{
			for (Browser browser : browsers)
			{
				resultSheet = common.getKeyValue(dataSource, "resultWorksheet");
				if(browser.browserType.equalsIgnoreCase("firefox"))
				{
					resultCell = common.getKeyValue(dataSource, "resultCellFirefox");
				}
				else
				{
					resultCell = common.getKeyValue(dataSource, "resultCellChrome");
				}
				resultMessage = common.getKeyValue(dataSource, "resultCellMessage");
				
				step = 0;
				try
				{
					//Initialize properties for Selenium
					sp = new SeleniumProperties(browser);
					app = new AppProperties();
					sp.driver.manage().window().maximize();
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--Error setting up Selenium for " + browser + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, null);
					throw new Exception(errorMessage);
				} 
				
				//Step One: Login page should be displayed.
				step = 1;
				try
				{
					String validLogInPage = "";
					if (app.getEnvironment().equalsIgnoreCase("dev"))
					{
						validLogInPage = app.getTestURL() + common.getKeyValue(dataSource, "validLogInPage");
					}
					else
					{
						validLogInPage = app.getTestURL() + common.getKeyValue(dataSource, "qaValidLogInPage");
					}
					sp.driver.get(app.getTestURL());
					//Assertion
					if (!sp.driver.getCurrentUrl().equalsIgnoreCase(validLogInPage))
					{
						throw new Exception("Incorrect starting URL. Expected " + validLogInPage + "and got " + sp.driver.getCurrentUrl()+ ".");
					}
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Correct Login Page");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				//Step Two: User should not be allowed to log in with invalid username and password.
				step = 2;
				try
				{
					String invalidUserName = common.getKeyValue(dataSource, "invalidUserName");
					String invalidPassword = common.getKeyValue(dataSource, "invalidPassword");
					String expectedErrorMessage = common.getKeyValue(dataSource, "expectedErrorMessage");
					commonFunctions.LogIn(sp.driver, invalidUserName, invalidPassword);
					
					String txtErrorID = "";
					if(app.getEnvironment().equalsIgnoreCase("dev"))
					{
						txtErrorID = "dnn_ctr1463_ctl00_lblMessage";
					}
					else 
					{
						txtErrorID = "dnn_ctr463_ctl00_lblMessage";
					}
					WebElement txtError = sp.driver.findElement(By.id(txtErrorID));
					if(!txtError.getText().equalsIgnoreCase(expectedErrorMessage))
					{
						throw new Exception("Incorrect error message.  Expected " + expectedErrorMessage + " but got " + txtError.getText() + ".");
					} 	
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Correct Login Failed Message");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				//Step Three: User should be allowed to log in with valid username and password.
				step = 3;
				try
				{
					String validUserName = common.getKeyValue(dataSource, "validUserName"); 
					String validPassword = common.getKeyValue(dataSource, "validPassword");
					String validAssetPage = app.getTestURL() + common.getKeyValue(dataSource, "validAssetPage");
					commonFunctions.LogIn(sp.driver, validUserName, validPassword);
					Thread.sleep(12000);
					
					if (!sp.driver.getCurrentUrl().equalsIgnoreCase(validAssetPage))
					{
						throw new Exception("Incorrect starting URL. Expected " + validAssetPage + "and got " + sp.driver.getCurrentUrl()+ ".");
					}
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Correct Dashboard Page");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}	
					
				sp.driver.close();
				
				//Write to GoogleDoc				
				results.writeToGoogleDoc(googleDoc, resultSheet, resultCell, resultMessage, "");
			}
			
			results.closeLogFile(testName);
		}
		catch (Exception ex)
		{
			results.writeToGoogleDoc(googleDoc, resultSheet, resultCell, resultMessage, ex.getMessage());
		}
	}	//End PrestoLogIn
}	//End Class
