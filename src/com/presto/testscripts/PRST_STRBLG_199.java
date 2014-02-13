package com.presto.testscripts;

import org.testng.annotations.Test;

import com.presto.common.*;
import com.presto.driver.*;
import com.presto.driver.CommonData.Browser;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.*;

import org.testng.Assert;

public class PRST_STRBLG_199 
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
	public void PRST_STRBLG_199() throws Exception
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
				
				//Step Two:  Move to Create->Blog Post
				step = 2;
				Actions action = new Actions(sp.driver);
				//Click Create
				try
				{			
					try
					{
						List<WebElement> dropDownLists = sp.driver.findElements(By.className("caret"));
						WebElement lnkCreate = dropDownLists.get(1);
						action.moveToElement(lnkCreate).build().perform();
					}
					catch (Exception ex)
					{
						throw new Exception("Create Element not found.");
					}
					
					try
					{
						String txtBlogPost = "";
						if(app.getEnvironment().equalsIgnoreCase("dev"))
						{
							txtBlogPost = "Blog Post";
						}
						else
						{
							txtBlogPost = "Blog Post A-G";
						}
						WebElement lnkBlogPost = sp.driver.findElement(By.linkText(txtBlogPost));
						action.moveToElement(lnkBlogPost).build().perform();
					}
					catch (Exception ex)
					{
						throw new Exception("Blog Post Element not found.");
					}					
					
					try
					{
						String blogToCreate = common.getKeyValue(dataSource, "blogToCreate");
						WebElement lnkBlog = sp.driver.findElement(By.linkText(blogToCreate));
					}
					catch (Exception ex)
					{
						throw new Exception("Blog to create not found.");
					}
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Dropdown Menu Displayed");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				//Step Three: Select a Blog to Create
				step = 3;
				try
				{
					String blogToCreate = common.getKeyValue(dataSource, "blogToCreate");
					WebElement lnkBlog = sp.driver.findElement(By.linkText(blogToCreate));
					action.moveToElement(lnkBlog).click().build().perform();
					
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
					
					WebElement displayName = sp.driver.findElement(By.className("contentGroupName"));
					if (!displayName.getText().equalsIgnoreCase(blogToCreate))
					{
						throw new Exception("Incorrect Blog Name.  Expected " + blogToCreate + " and got " + displayName.getText() + ".");
					}
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Correct Create Page and Name Displayed");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}	
				
				//Step Four: Input Fields
				step = 4;
				try
				{
					commonFunctions.EnterBlogFields(sp.driver);
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Input Fields Successfully");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				
				//Step Five: Save Draft
				step = 5;
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
					action.moveToElement(btnSave).click().build().perform();
					Thread.sleep(5000);
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Draft Saved");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				//Step Six: Navigate to versions tab
				step = 6;
				try
				{
					WebElement lnkVersion = sp.driver.findElement(By.linkText("Versions"));
					action.moveToElement(lnkVersion).click().build().perform();
					Thread.sleep(6000);
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Navigate To Versions Tab");
					
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				//Step Seven: Verify the display of the versions
				step = 7;
				try
				{
					WebElement revType = sp.driver.findElement(By.className("revisionType"));					
					Assert.assertEquals(revType.getText().toString().trim(),"ACTIVE [Created]");
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Verify Display Of Versions");
					
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
