package com.presto.common;

import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;

import com.presto.driver.*;

public class CommonFunctions 
{
	public void LogIn (WebDriver driver, String userName, String password) throws Exception
	{
		String ddBoxID = "";
		String selectionID = "";
		String txtUserID = "";
		String txtPasswordID = "";
		String btnLoginID = "";
		
		//Get Environment
		String environment = "";
		AppProperties app = new AppProperties();
		environment = app.getEnvironment();	
		
		
		//Set object IDs
		if(environment.equalsIgnoreCase("dev"))
		{
			ddBoxID = "dropDownContainer";
			selectionID = "prestoTab";
			txtUserID = "dnn$ctr1463$Login$DNN$Login_DNN$txtUsername";
			txtPasswordID = "dnn$ctr1463$Login$DNN$Login_DNN$txtPassword";
			btnLoginID = "dnn_ctr1463_Login_DNN_Login_DNN_cmdLogin";			
		}
		else 
		{
			ddBoxID = "dropDownContainer";
			selectionID = "prestoTab";
			txtUserID = "dnn$ctr463$Login$Login_DNN$txtUsername";
			txtPasswordID = "dnn$ctr463$Login$Login_DNN$txtPassword";
			btnLoginID = "dnn_ctr463_Login_Login_DNN_cmdLogin";	
		}
		
		//Select Value in drop down box
		//1.) Find Drop Down Box
		Actions action = new Actions(driver);		
		WebElement ddBox = driver.findElement(By.className(ddBoxID));
		action.moveToElement(ddBox).click(ddBox).build().perform();
		WebElement adminSelection = driver.findElement(By.id(selectionID));
		action.moveToElement(adminSelection).click(adminSelection).build().perform();
		
		//2.) Input User and Password
		WebElement txtUser = driver.findElement(By.cssSelector("[id$='txtUsername']"));
		//WebElement txtUser = driver.findElement(By.name(txtUserID));
		txtUser.clear();
		txtUser.sendKeys(userName);
		WebElement txtPassword = driver.findElement(By.cssSelector("[id$='txtPassword']"));
		//WebElement txtPassword = driver.findElement(By.name(txtPasswordID));
		txtPassword.clear();
		txtPassword.sendKeys(password);
		
		//3.) Press the Login Button
		WebElement loginButton = driver.findElement(By.cssSelector("[id$='cmdLogin']"));
		//WebElement loginButton = driver.findElement(By.id(btnLoginID));
		loginButton.click();		
	}

	public void LogOut (WebDriver driver) throws Exception

	{
		Actions action = new Actions(driver);
		List<WebElement> dropDownLists = driver.findElements(By.className("caret"));
		WebElement logOutDropDown = dropDownLists.get(11);
		action.moveToElement(logOutDropDown).build().perform();
		WebElement logOut = driver.findElement(By.id("dnn_dnnLOGIN_loginLink"));
		action.moveToElement(logOut).click().build().perform();		
	}

	public void SelectCreateAsset (WebDriver driver, String assetAction, String assetType) throws Exception
	{		
		//Get Environment
		String environment = "";
		AppProperties app = new AppProperties();
		environment = app.getEnvironment();		

		Actions action = new Actions(driver);		
		
		List<WebElement> dropDownLists = driver.findElements(By.className("caret"));
		WebElement lnkCreate = dropDownLists.get(1);
		action.moveToElement(lnkCreate).build().perform();
		
		WebElement lnkBlogPost = driver.findElement(By.linkText(assetAction));
		action.moveToElement(lnkBlogPost).build().perform();
		
		
		WebElement lnkBlog = driver.findElement(By.linkText(assetType));
		action.moveToElement(lnkBlog).click().build().perform();
	}

	public void EnterBlogFields (WebDriver driver) throws Exception
	{
		String txtHeadline = "";
		String txtURLEnding = "";
		String txtPromoBrief = "";
		String ddFrontAssignment = "";
		String ddItemClass = "";
		
		//Get Environment
		String environment = "";
		AppProperties app = new AppProperties();
		environment = app.getEnvironment();	
		
		//Set object IDs
		if(environment.equalsIgnoreCase("dev"))
		{
			txtHeadline = "dnn_ctr798_Edit_txtHeadline";
			txtURLEnding = "dnn_ctr798_Edit_txtSEOName";
			txtPromoBrief = "dnn_ctr798_Edit_txtBrief";
			ddFrontAssignment = "dnn_ctr798_Edit_FrontAssignment1_Input";
			ddItemClass = "rcbItem";
		}
		else 
		{
			txtHeadline = "dnn_ctr477_Edit_txtHeadline";
			txtURLEnding = "dnn_ctr477_Edit_txtSEOName";
			txtPromoBrief = "dnn_ctr477_Edit_txtBrief";
			ddFrontAssignment = "dnn_ctr477_Edit_FrontAssignment1_Input";
			ddItemClass = "rcbItem";
		}		

		Actions action = new Actions(driver);		
		WebElement headline = driver.findElement(By.id(txtHeadline));
		headline.clear();
		headline.sendKeys("Automation - Do Not Use - Blog");
		
		WebElement urlEnding = driver.findElement(By.id(txtURLEnding));
		urlEnding.clear();
		urlEnding.sendKeys("AutomationBlog");
		
		WebElement promoBrief = driver.findElement(By.id(txtPromoBrief));
		promoBrief.clear();
		promoBrief.sendKeys("Automation - Do Not Use - Blog");
		
		WebElement frontAssignment = driver.findElement(By.id(ddFrontAssignment));		
		action.moveToElement(frontAssignment).click(frontAssignment).build().perform();
		List<WebElement> lstOptions = driver.findElements(By.className(ddItemClass));
		WebElement optionSelection = lstOptions.get(4);
		action.moveToElement(optionSelection).click().build().perform();
	}
	
	public void SearchAssets (WebDriver driver, String searchType) throws Exception
	{
		String btnRunSearchID = "";
		String ddSearchID = "";
		List<WebElement> searchChoices = null;
		
		//Get Environment
		String environment = "";
		AppProperties app = new AppProperties();
		environment = app.getEnvironment();	
		
		//Set object IDs
		if(environment.equalsIgnoreCase("dev"))
		{
			btnRunSearchID = "dnn_ctr798_Edit_btnSearch";
			ddSearchID = "dnn_ctr798_Edit_cboassetTypeFilter_Input";
		}
		else 
		{
			btnRunSearchID = "dnn_ctr477_Edit_btnSearch";
			ddSearchID = "dnn_ctr477_Edit_cboassetTypeFilter_Input";
		}		

		Actions action = new Actions(driver);	
						
		WebElement ddSearch = driver.findElement(By.id(ddSearchID));
		action.moveToElement(ddSearch).click().build().perform();
		
		if(!searchType.equalsIgnoreCase("any"))
		{
			searchChoices = driver.findElements(By.className("rcbItem"));
			for (WebElement searchItem : searchChoices)
			{
				if(searchItem.getText().equalsIgnoreCase(searchType))
				{
					action.moveToElement(searchItem).click().build().perform();
					break;
				}
			}
		}
		else
		{
			WebElement btnRunSearch = driver.findElement(By.id(btnRunSearchID));
			action.moveToElement(btnRunSearch).click().build().perform();
		}	
		Thread.sleep(40000);
	}
	
	public void ClickToAddAsset (WebDriver driver) throws Exception
	{
		String txtOptionID = "";
		
		//Get Environment			
		AppProperties app = new AppProperties();
		String environment = app.getEnvironment();
		
		//Set object IDs
		if(environment.equalsIgnoreCase("dev"))
		{
			txtOptionID = ".//*[@id='dnn_ctr798_Edit_rdlb_search_i1']/span/div[1]";
		}
		else 
		{
			txtOptionID = ".//*[@id='dnn_ctr477_Edit_rdlb_search_i1']/span/div[1]";
		}		

		Actions action = new Actions(driver);						
		WebElement selectedOption = driver.findElement(By.xpath(txtOptionID));
		
		action.click(selectedOption).build().perform();
		action.contextClick(selectedOption).build().perform();					
		
		Thread.sleep(500);
		WebElement btnAssociate = driver.findElement(By.className("rmLink"));
		action.click(btnAssociate).build().perform();
		
		Thread.sleep(500);
		WebElement btnOK = driver.findElement(By.className("rwPopupButton"));
		action.click(btnOK).build().perform();		
	}
	
	public void Publish(WebDriver driver) throws Exception
	{
		String btnPublishID = "";
		
		//Get Environment			
		AppProperties app = new AppProperties();
		String environment = app.getEnvironment();
		
		//Set object IDs
		if(environment.equalsIgnoreCase("dev"))
		{
			btnPublishID = "dnn_ctr798_Edit_btnPublish";
		}
		else 
		{
			btnPublishID = "dnn_ctr477_Edit_btnPublish";
		}		

		Actions action = new Actions(driver);						
		WebElement btnPublish = driver.findElement(By.id(btnPublishID));
		
		action.click(btnPublish).build().perform();
	}
}
