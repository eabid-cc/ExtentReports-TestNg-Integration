package com.vikas.report_integration.core;
import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.vikas.report_integration.core.utils.ExtentReportFactory;

public class BaseTest implements IHookable{
	
	protected WebDriver driver;
	final String AUT_URL="http://vikasthange.blogspot.in";
	
	
	protected ExtentTest testReporter ;
	@BeforeMethod
	public void initDriver(Method method){
		
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.get(AUT_URL);
	}
	@AfterMethod(alwaysRun=true)
	public void cleanUp(ITestResult result, Method method){
		String testname = getTestName(result);
		testReporter.log(LogStatus.INFO, "After test:"+ testname);
		if(result.getStatus()==ITestResult.FAILURE){
			testReporter.log(LogStatus.FAIL, "Test Failed: "+result.getThrowable().getMessage());
		}
		else if(result.getStatus()==ITestResult.SUCCESS){
			testReporter.log(LogStatus.PASS, "Test Passed");
		}else if(result.getStatus()==ITestResult.SKIP){
			testReporter.log(LogStatus.SKIP, "Test Skipped");
		}else{
			testReporter.log(LogStatus.ERROR, "Error while executing test");
		}
		testReporter = null;
		ExtentReportFactory.closeTest(testname);
		driver.close();
		
	}
	/**
	 * Get a human-readable name for the test.
	 *
	 * @param name The name of the test that will be run.
	 * @param parameters The parameters that will be passed to the test.
	 * @return The test name.
	 */
	private String getTestName(ITestResult testResult) {
		String name = testResult.getName();
		Object[] parameters = testResult.getParameters();
		StringBuilder testName = new StringBuilder();

		testName.append(name);
		testName.append("(");
		for(int i = 0; i < parameters.length; i++) {
			testName.append("[" + parameters[i].toString() + "]");
			if(i != parameters.length - 1) {
				testName.append(",");
			}
		}
		testName.append(")");

		return testName.toString();
	}
	/*
	 * After suite will be responsible to close the report properly at the end
	 * You an have another afterSuite as well in the derived class and this one
	 * will be called in the end making it the last method to be called in test exe
	 */
	@AfterSuite
	public void afterSuite() {
		ExtentReportFactory.closeReport();
	}
	public void run(IHookCallBack callBack, ITestResult testResult) {
		String description = testResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).description();
		String testName = getTestName(testResult);
		ExtentReportFactory.getTest( testName, description);
		testReporter = ExtentReportFactory.getTest();
		testReporter.log(LogStatus.INFO, "Started test :"+  testName);
		System.out.println("Method: "+ testName);
		callBack.runTestMethod(testResult);
		
	}
}
