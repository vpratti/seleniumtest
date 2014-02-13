package com.presto.testscripts;

import org.testng.annotations.Test;

import com.presto.common.*;
import com.presto.driver.*;
import com.presto.driver.CommonData.Browser;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PRST_STRBLG_309 
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
	
	@Test(groups = { "BlogCreationTest", "RegressionTest" })
	public void PRST_STRBLG_309() throws Exception
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
			
			CommonData commonData = new CommonData();
			browsers = commonData.browsers;
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
				
				//Step One: Login and Check For Correct Asset Page
				step = 1;
				try
				{
					sp.driver.get(app.getTestURL());
					
					String validUserName = common.getKeyValue(dataSource, "validUserName");
					String validPassword = common.getKeyValue(dataSource, "validPassword");
					String validAssetPage = app.getTestURL() + common.getKeyValue(dataSource, "validAssetPage");										
					commonFunctions.LogIn(sp.driver, validUserName, validPassword);
					Thread.sleep(12000);
					
					if (!sp.driver.getCurrentUrl().equalsIgnoreCase(validAssetPage))
					{
						throw new Exception("Incorrect starting URL. Expected " + validAssetPage + " and got " + sp.driver.getCurrentUrl() + ".");
					}
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Correct Dashboard Page");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}	
				
				//Step Two:  Select a Blog to Create
				step = 2;
				Actions action = new Actions(sp.driver);
				//Click Create
				try
				{	
					String txtAssetAction = "";
					String txtAssetType = common.getKeyValue(dataSource, "blogToCreate");
					if(app.getEnvironment().equalsIgnoreCase("dev"))
					{
						txtAssetAction = "Blog Post";
					}
					else
					{
						txtAssetAction = "Blog Post A-G";
					}
					commonFunctions.SelectCreateAsset(sp.driver, txtAssetAction, txtAssetType);
					
					String blogCreateURL = "";
					if (app.getEnvironment().equalsIgnoreCase("dev"))
					{
						blogCreateURL = app.getTestURL() + common.getKeyValue(dataSource, "blogCreateURL");
					}
					else
					{
						blogCreateURL = app.getTestURL() + common.getKeyValue(dataSource,  "qaBlogCreateURL");
					}
					
					if (!sp.driver.getCurrentUrl().equalsIgnoreCase(blogCreateURL))
					{
						throw new Exception("Incorrect URL. Expected " + blogCreateURL + " and got " + sp.driver.getCurrentUrl() + ".");
					}
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Correct Create Page and Name Displayed");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				//Step Three: Input Fields and Save Draft
				step = 3;
				try
				{
					commonFunctions.EnterBlogFields(sp.driver);
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				try
				{
					String btnSaveID = "";
					if(app.getEnvironment().equalsIgnoreCase("dev"))
					{
						btnSaveID = "dnn_ctr798_Edit_btnSaveDraft";
					}
					else
					{
						btnSaveID = "dnn_ctr477_Edit_btnSaveDraft";
					}
					WebElement btnSave = sp.driver.findElement(By.id(btnSaveID));
					action.moveToElement(btnSave).build().perform();
					Thread.sleep(1000);
					action.click(btnSave).build().perform();
					Thread.sleep(5000);
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Draft Saved");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				//Step Four:  Enter Metadata
				//step = 4;
				/*
				try
				{
					WebElement btnSearch = sp.driver.findElement(By.xpath(".//*[@id='rightModuleNav']/a[1]"));
					action.moveToElement(btnSearch).click().build().perform();
					
					String txtSearchField = "";
					if(app.getEnvironment().equalsIgnoreCase("dev"))
					{
						txtSearchField = "dnn_ctr798_Edit_tbSearch";
					}
					else
					{
						txtSearchField = "dnn_ctr477_Edit_tbSearch";
					}
					
					if(sp.driver.findElement(By.id(txtSearchField)) == null)
					{
						throw new Exception ("Search Box did not display properly.");
					}					
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Magnifying Glass Clicked Successfully");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}*/
				
				//Step Five:  Publish
				step = 4;	
				Thread.sleep(2000);
				try
				{
					commonFunctions.Publish(sp.driver);
					Thread.sleep(20000);
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Published Successfully");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				//Step Six:  Verify Story is at the Top
				step = 5;
				try
				{
					WebElement blogAsset = sp.driver.findElement(By.xpath(".//*[starts-with(@id,'contentHold_')]/div/div/div/span/a"));
					if(!blogAsset.getText().equalsIgnoreCase("Automation - Do Not Use - Blog"))
					{
						throw new Exception("First Asset is not Automation - Do Not Use - Blog.");
					}

					WebElement firstDraft = sp.driver.findElement(By.xpath(".//*[starts-with(@id,'contentHold_')]/div/div[4]/div[3]/span"));
					if(!firstDraft.getText().equalsIgnoreCase("Published"))
					{
						throw new Exception("First Asset is not in Draft status.");
					}					
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Blog Found and First");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				//sp.driver.close();
				
				//Write to GoogleDoc
				//results.writeToGoogleDoc(googleDoc, resultSheet, resultCell, resultMessage, "");
			}
			
			results.closeLogFile(testName);
		}
		catch (Exception ex)
		{
			results.writeToGoogleDoc(googleDoc, resultSheet, resultCell, resultMessage, ex.getMessage());
		}
	}	//End Presto Test
}	//End Class
