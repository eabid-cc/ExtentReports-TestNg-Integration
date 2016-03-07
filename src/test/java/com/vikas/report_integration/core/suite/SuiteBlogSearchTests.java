package com.vikas.report_integration.core.suite;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;
import com.vikas.report_integration.core.BaseTest;

public class SuiteBlogSearchTests extends BaseTest{
	
	/**
	 * Test case to use demonstrate use of ExtentReports with no data provider test
	 */
	@Test
	public void testSearchResultContainingSearchText(){
		
		String blogTitle = driver.findElement(By.className("title")).getText();
		testReporter.log(LogStatus.INFO, "Actual blog title found: "+ blogTitle);
		Assert.assertEquals(blogTitle, "Vikas Thange","Blog title failed");
	}
	/**
	 * Test case to demonstrate the use of ExtendReports when used with Data provider.
	 * @param searchText Text to search on blog
	 * @param expectedFirstHeading expected blog title of first search result
	 */
	@Test(dataProvider="SearchData")
	public void testSearchResultCountDataDriven(String searchText,String expectedFirstHeading){
		searchBlog(searchText);
		String firstBlogTitle = driver.findElement(By.xpath("//*[@class='date-outer'][1]//div[@itemscope='itemscope']/h3")).getText();
		testReporter.log(LogStatus.INFO, "First article title : "+ firstBlogTitle);
		Assert.assertEquals(firstBlogTitle,expectedFirstHeading,"First article title from search result failed.");
	}
	/**
	 * Data provider method to get search text and expected result for multi iteration test
	 * @return 2D array with text to search on blog and expected article title
	 */
	@DataProvider(name="SearchData")
	public Object[][] getProductsData(){
		// We can't use testReporter in data provider as we need to form actual test name with the help of parameter value of test
		Object[][] params= new String[][]{
			{"SDLC","Software Development Life Cycle - SDLC"},
			{"Test Planning","Software testing life cycle (STLC)"},
			{"Testing","Levels of Software Testing"},
			{"Agile","Failing test case intentionally"},
		};
		return params;
	}
	/**
	 * Method to search given text on blog page
	 * @param textToSearch Text to search on blog page
	 */
	private void searchBlog(String textToSearch){
		driver.switchTo().frame("navbar-iframe");
		testReporter.log(LogStatus.INFO, "Searching blog with text: "+textToSearch);
		driver.findElement(By.id("b-query")).sendKeys(textToSearch);
		driver.findElement(By.id("b-query-icon")).click();
		testReporter.log(LogStatus.INFO, "Search done");
		driver.switchTo().defaultContent();
	}
}