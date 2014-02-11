package com.presto.driver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.presto.driver.CommonData.Browser;

public class SeleniumProperties 
{
	public WebDriver driver = null;
	String chromedrvpath = null;
	String iedrvpath = null;
	
	public SeleniumProperties(Browser br)
	{
		AppProperties app = new AppProperties();
		String browser = br.browserType;
		String env = app.getEnvironment();
		String version = br.browserVersion;
		String testURL = app.getTestURL();
		String os = app.getOS();

		if(browser.equalsIgnoreCase("firefox"))
		{
			ProfilesIni profileIni = new ProfilesIni();
			driver = new FirefoxDriver();			
		}
		else if(browser.equalsIgnoreCase("chrome"))
		{
			String userName = System.getProperty("user.name");
			String driverPath = app.getChromeDriver();
			System.setProperty("webdriver.chrome.driver", driverPath);
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized");
			
			driver = new ChromeDriver();
		}
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get(app.getTestURL());
	}

}
