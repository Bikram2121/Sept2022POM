package com.qa.opencart.tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.utils.Constants;
import com.qa.opencart.utils.ExcelUtil;

public class AccountsPageTest extends BaseTest {

	@BeforeClass
	public void accPageSetup() {
		accountsPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}

	@Test(priority = 1)
	public void accPageTitleTest() {
		String actTitle = accountsPage.getAccountPageTitle();
		System.out.println("acc page title: " + actTitle);
		Assert.assertEquals(actTitle, Constants.ACCOUNTS_PAGE_TITLE);
	}

	@Test(priority = 3)
	public void isLogoutExistTest() {
		Assert.assertTrue(accountsPage.isLogoutLinkExist());
	}

	@Test(priority = 4)
	public void accPageSectionsTest() {
		List<String> actAccSecList = accountsPage.getAccountSecList();
		Assert.assertEquals(actAccSecList, Constants.getExpectedAccSecList());
	}

	@DataProvider
	public static Object[][] productData() {
		return ExcelUtil.getTestData(Constants.PRODUCTDATA1_SHEET_NAME);
	}

	@Test(priority = 5, dataProvider = "productData")
	public void searchTest(String productName) {
		searchResultPage = accountsPage.doSearch(productName);
		Assert.assertTrue(searchResultPage.getProductsListCount() > 0);
	}

	@DataProvider
	public Object[][] productSelectData() {
		return ExcelUtil.getTestData(Constants.PRODUCTDATA2_SHEET_NAME);
	}

	@Test(priority = 6, dataProvider = "productSelectData")
	public void selectProductTest(String productName, String mainProductName) {
		searchResultPage = accountsPage.doSearch(productName);
		productInfoPage = searchResultPage.selectProduct(mainProductName);
		Assert.assertEquals(productInfoPage.getProductHeader(), mainProductName);
	}

}
