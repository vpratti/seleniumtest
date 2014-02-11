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

public class PRST_STRBLG_316 
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
	public void PRST_STRBLG_316() throws Exception
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
				
				//Step Four:  Click Magnifying Glass
				step = 4;
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
				}
				
				//Step Five:  Validate Search Choices
				step = 5;	
				Thread.sleep(12000);
				List<WebElement> searchChoices = null;
				//List<String> SearchList = Arrays.asList("Embed", "External Video", "Gallery", 
				//		"Image", "Interactive", "Map", "POI", "Pull Quote", "Text", "Video", "Live Video", "Video-playlist");
				List<String> SearchList = Arrays.asList("Video", "Live Video", "Video-playlist");
				
				try
				{
					String ddSearchID = "";
					if(app.getEnvironment().equalsIgnoreCase("dev"))
					{
						ddSearchID = "dnn_ctr798_Edit_cboassetTypeFilter_Input";
					}
					else
					{
						ddSearchID = "dnn_ctr477_Edit_cboassetTypeFilter_Input";
					}					
					WebElement ddSearch = sp.driver.findElement(By.id(ddSearchID));
					action.moveToElement(ddSearch).click().build().perform();
					
					WebElement searchItemFound = null;
					searchChoices = sp.driver.findElements(By.className("rcbItem"));
					for (String txtSearchType : SearchList)
					{							
						for (WebElement searchItem : searchChoices)
						{
							if(searchItem.getText().equalsIgnoreCase(txtSearchType))
							{
								searchItemFound = searchItem;
								break;
							}
						}
						
						if(searchItemFound == null)
						{
							throw new Exception (txtSearchType + " choice not found.");
						}
					}										
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Elements in Drop Down Box Verified");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				//Step Six:  Select "Any" to Search 
				step = 6;
				Thread.sleep(12000);
				try
				{
					commonFunctions.SearchAssets(sp.driver, "Any");
					ValidateSearchOrder();
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Any Search Results in Order");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				//Step Seven:  Add Asset to Story 
				step = 7;
				Thread.sleep(12000);
				try
				{
					commonFunctions.ClickToAddAsset(sp.driver);	
					
					WebElement lnkAssets = sp.driver.findElement(By.xpath(".//*[@id='rightModuleNav']/a[2]"));
					action.click(lnkAssets).build().perform();
					String lnkAssetCountPath = "";
					if(app.getEnvironment().equalsIgnoreCase("dev"))
					{
						lnkAssetCountPath = "dnn_ctr798_Edit_lbl_assetNum";
					}
					else
					{
						lnkAssetCountPath = "dnn_ctr477_Edit_lbl_assetNum";
					}
					WebElement lblAssetCount = sp.driver.findElement(By.id(lnkAssetCountPath));

					if(!lblAssetCount.getText().equalsIgnoreCase("1"))
					{
						throw new Exception("Asset Count not updated.");
					}
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Asset Added from \"Any\" Search.");
				}
				catch (Exception ex)
				{
					errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
					results.writeFailLog(testName, errorMessage, sp.driver);
					throw new Exception(errorMessage);
				}
				
				
				//Add an asset of each kind.
				int assetCount = 0;
				for (String assetType : SearchList)
				{					
					if(!assetType.equalsIgnoreCase("External Video"))
					{
						step++;
						WebElement btnSearch = sp.driver.findElement(By.xpath(".//*[@id='rightModuleNav']/a[1]"));
						action.moveToElement(btnSearch).click().build().perform();
						try
						{
							commonFunctions.SearchAssets(sp.driver, assetType);
							ValidateSearchOrder();
							
							results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--" + assetType + " Search Results in Order");
						}
						catch (Exception ex)
						{
							errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
							results.writeFailLog(testName, errorMessage, sp.driver);
							throw new Exception(errorMessage);
						}
						
						step++;
						Thread.sleep(12000);
						try
						{
							commonFunctions.ClickToAddAsset(sp.driver);		
							
							WebElement lnkAssets = sp.driver.findElement(By.xpath(".//*[@id='rightModuleNav']/a[2]"));
							action.click(lnkAssets).build().perform();
							String lnkAssetCountPath = "";
							if(app.getEnvironment().equalsIgnoreCase("dev"))
							{
								lnkAssetCountPath = "dnn_ctr798_Edit_lbl_assetNum";
							}
							else
							{
								lnkAssetCountPath = "dnn_ctr477_Edit_lbl_assetNum";
							}
							WebElement lblAssetCount = sp.driver.findElement(By.id(lnkAssetCountPath));
							
							assetCount++;
							if(!lblAssetCount.getText().equalsIgnoreCase((String.valueOf(assetCount))))
							{
								throw new Exception("Asset Count not updated.");
							}
							
							results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Asset Added from \"" + assetType + "\" Search.");
						}
						catch (Exception ex)
						{
							errorMessage = "Step " + step + ": FAIL--" + ex.getMessage() + "--Stack: " + ex.getStackTrace();
							results.writeFailLog(testName, errorMessage, sp.driver);
							throw new Exception(errorMessage);
						}
					}					
				}
				
				//Next Step:  Publish 
				step++;
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
				
				//Next Step:  Verify Publish and Assets
				step++;
				try
				{
					List<WebElement> lstContents = sp.driver.findElements(By.className("thumbDescription"));
					for (WebElement eleContent : lstContents)
					{
						if(eleContent.getText().equalsIgnoreCase("Automation - Do Not Use - Blog"))
						{
							action.click(eleContent).build().perform();
							Thread.sleep(12000);
							break;
						}
					}
					
					String lnkAssetCountPath = "";
					if(app.getEnvironment().equalsIgnoreCase("dev"))
					{
						lnkAssetCountPath = "dnn_ctr659_Edit_lbl_assetNum";
					}
					else
					{
						lnkAssetCountPath = "dnn_ctr477_Edit_lbl_assetNum";
					}
					WebElement lblAssetCount = sp.driver.findElement(By.id(lnkAssetCountPath));
					
					if(!lblAssetCount.getText().equalsIgnoreCase(String.valueOf(assetCount)))
					{
						throw new Exception("Assets not added correctly or incorrect asset selected.");
					}
					
					results.writePassLog(testName, "Step " + step + ": " + browser.browserType + "--Published Successfully");
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
	
	void ValidateSearchOrder() throws Exception
	{
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy kk:mm:ss aa");

		WebElement searchResultList = null;
		if(app.getEnvironment().equalsIgnoreCase("dev"))
		{
			try
			{
				searchResultList = sp.driver.findElement(By.xpath(".//*[@id='dnn_ctr798_Edit_rdlb_search_i0']/span/div[1]/div[4]"));
			}
			catch (Exception ex)
			{
				if(searchResultList == null)
				{
					searchResultList = sp.driver.findElement(By.xpath(".//*[@id='dnn_ctr798_Edit_rdlb_search_i0']/span/div/div[1]/div[4]"));
				}
			}
			finally
			{
				if(searchResultList == null)
				{
					throw new Exception("Item in Search List Not Found.");
				}
			}			
		}
		else
		{
			try
			{
				searchResultList = sp.driver.findElement(By.xpath(".//*[@id='dnn_ctr477_Edit_rdlb_search_i0']/span/div[1]/div[4]"));
			}
			catch (Exception ex)
			{
				if(searchResultList == null)
				{
					searchResultList = sp.driver.findElement(By.xpath(".//*[@id='dnn_ctr477_Edit_rdlb_search_i0']/span/div/div[1]/div[4]"));
				}
			}
			finally
			{
				if(searchResultList == null)
				{
					throw new Exception("Item in Search List Not Found.");
				}
			}
		}					
		String firstItemDate = searchResultList.getText().substring(searchResultList.getText().indexOf("|") + 2, 
				searchResultList.getText().length());
		
		if(app.getEnvironment().equalsIgnoreCase("dev"))
		{
			searchResultList = sp.driver.findElement(By.xpath(".//*[@id='dnn_ctr798_Edit_rdlb_search_i1']/span/div[1]/div[4]"));
		}
		else
		{
			searchResultList = sp.driver.findElement(By.xpath(".//*[@id='dnn_ctr477_Edit_rdlb_search_i1']/span/div[1]/div[4]"));
		}
		String secondItemDate = searchResultList.getText().substring(searchResultList.getText().indexOf("|") + 2, 
				searchResultList.getText().length());
		
		Date dtFirst = (Date)formatter.parse(firstItemDate);
		Date dtSecond = (Date)formatter.parse(secondItemDate);
		
		if(dtSecond.after(dtFirst))
		{
			throw new Exception("Dates are not in order.");
		}							
	}
}	//End Class
