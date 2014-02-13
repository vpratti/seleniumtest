package com.presto.testscripts;

import org.testng.annotations.Test;

import com.presto.common.*;
import com.presto.driver.*;
import com.presto.driver.CommonData.Browser;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.*;

public class PRST_STRBLG_002 
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
	
	@Test(groups = { "StoryCreationTest", "SmokeTest", "RegressionTest" })
	public void PRST_STRBLG_002() throws Exception
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
				
				//Step Three: Input Fields
				step = 3;
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
				
				//Step Four: Save Draft
				String assetID = "";
				step = 4;
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
					
					String lblCheckIDxpath = "";
					if(app.getEnvironment().equalsIgnoreCase("dev"))
					{
						lblCheckIDxpath = ".//*[@id='dnn_ctr798_Edit_rpvTextTab']/div/div[1]/div[1]/div[4]/div[4]/div[2]";
					}
					else
					{
						lblCheckIDxpath = ".//*[@id='dnn_ctr477_Edit_rpvTextTab']/div/div[1]/div[1]/div[4]/div[4]/div[2]";
					}
					
					WebElement lblCheckID = sp.driver.findElement(By.xpath(lblCheckIDxpath));
					assetID = lblCheckID.getText();
					int firstSpace = assetID.indexOf(" ");
					assetID = assetID.substring(firstSpace + 1, assetID.indexOf(" ", firstSpace + 1));
					if(assetID == null)
					{
						throw new Exception ("ID not assigned or found.");
					}					
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Draft Saved");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}				
				
				//Step Five: Verify Source Text Field
				step = 5;
				try
				{
					String txtSourceID = "";
					if(app.getEnvironment().equalsIgnoreCase("dev"))
					{
						txtSourceID = "dnn_ctr798_Edit_txtSource";
					}
					else
					{
						txtSourceID = "dnn_ctr477_Edit_txtSource";
					}
					WebElement txtSource = sp.driver.findElement(By.id(txtSourceID));
					
					String txtSourceValue = txtSource.getAttribute("value");					
					if (!txtSourceValue.equalsIgnoreCase(common.getKeyValue(dataSource, "validSourceText")))
					{
						throw new Exception("Did not go back to Asset Page. Expected " + common.getKeyValue(dataSource, "validSourceText") + " and got " + txtSourceValue + ".");
					}
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Source Field Validated");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}				
				
				//Step Six: Verify XML
				/*String currentWindowHandle = sp.driver.getWindowHandle();
				step = 6;
				try
				{
					//Click XML 
					String lnkXMLid = "";
					if(app.getEnvironment().equalsIgnoreCase("dev"))
					{
						lnkXMLid = ".//*[@id='dnn_ctr798_Edit_rpvTextTab']/div/div[1]/div[1]/div[4]/div[4]/div[2]/a[1]";
					}
					else
					{
						lnkXMLid = ".//*[@id='dnn_ctr477_Edit_rpvTextTab']/div/div[1]/div[1]/div[4]/div[4]/div[2]/a[1]";
					}
					
					WebElement lnkXML = sp.driver.findElement(By.xpath(lnkXMLid));
					action.moveToElement(lnkXML).click().build().perform();					
					for(String winHandle : sp.driver.getWindowHandles())
					{
						sp.driver.switchTo().window(winHandle);
					}
					
					String xmlAddress = app.getTestURL() + common.getKeyValue(dataSource, "urlXML") + assetID + ".xml";
					if(!sp.driver.getCurrentUrl().equalsIgnoreCase(xmlAddress))
					{
						throw new Exception("Invalid or Missing XML Window");
					}						
					sp.driver.close();
					sp.driver.switchTo().window(currentWindowHandle);					
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--XML Validated");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}*/
				
				//Step Six: Verify JSON
				step = 6;
				try
				{
					//Click JSON 
					String lnkJSONid = "";
					if(app.getEnvironment().equalsIgnoreCase("dev"))
					{
						lnkJSONid = ".//*[@id='dnn_ctr798_Edit_rpvTextTab']/div/div[1]/div[1]/div[4]/div[4]/div[2]/a[1]";
					}
					else
					{
						lnkJSONid = ".//*[@id='dnn_ctr477_Edit_rpvTextTab']/div/div[1]/div[1]/div[4]/div[4]/div[2]/a[1]";
					}
					
					WebElement lnkJSON = sp.driver.findElement(By.xpath(lnkJSONid));
					action.moveToElement(lnkJSON).click().build().perform();					
					for(String winHandle : sp.driver.getWindowHandles())
					{
						sp.driver.switchTo().window(winHandle);
					}
					
					String jsonAddress = app.getTestURL() + common.getKeyValue(dataSource, "urlJSON") + assetID + ".json";
					if(!sp.driver.getCurrentUrl().equalsIgnoreCase(jsonAddress))
					{
						throw new Exception("Invalid or Missing JSON Window");
					}	
					sp.driver.close();
					/*sp.driver.switchTo().window(currentWindowHandle);*/
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--JSON Validated");
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
