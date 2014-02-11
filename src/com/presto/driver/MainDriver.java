package com.presto.driver;

import java.util.*;

import org.testng.TestNG;

public class MainDriver {

	public static void main(String[] args) throws Exception
	{				
		//Get Scripts to run	
		//1.) Grab Data from Excel Sheet (Scripts file)
		Scripts s = new Scripts();
		ArrayList<String> scriptsToExecute = s.getScripts();		
		
		//2.) Process Test Steps to Determine Which Tests to Run		
		Class[] testSuite = new Class[scriptsToExecute.size()];
		for (int i = 0; i < scriptsToExecute.size(); i++) 
		{
			testSuite[i] = Class.forName("com.presto.testscripts." + scriptsToExecute.get(i));
		}
		
		//3.) Open TestNG to Run Tests.	
		TestNG testNGInstance = new TestNG();
		testNGInstance.setTestClasses(testSuite);
		testNGInstance.run();		
	}
}
