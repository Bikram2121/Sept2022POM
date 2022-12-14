package com.qa.opencart.tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.opencart.utils.Constants;

public class ProductInfoPageTest extends BaseTest {

	
	@BeforeClass
	public void productInfoSetup() {
		accountsPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}

	@Test(priority = 1)
	public void productHeaderTest()
	{
		searchResultPage = accountsPage.doSearch("Macbook");
		productInfoPage = searchResultPage.selectProduct("MacBook Pro");
		Assert.assertEquals(productInfoPage.getProductHeader(), "MacBook Pro");	
	}
	
	@Test(priority = 2)
	public void productImagesCountTest()
	{
		searchResultPage = accountsPage.doSearch("iMac");
		productInfoPage = searchResultPage.selectProduct("iMac");
		Assert.assertEquals(productInfoPage.getProductImagesCount(), Constants.IMAC_IMAGE_COUNT);
	}
	
	@Test(priority = 3)
	public void productInfoTest()
	{
		searchResultPage = accountsPage.doSearch("Macbook");
		productInfoPage = searchResultPage.selectProduct("MacBook Pro");
		Map<String , String > actualProductInfoMap =  productInfoPage.getProductInfo();
		actualProductInfoMap.forEach((k,v) -> System.out.println(k + ":" + v));
		softAssert.assertEquals(actualProductInfoMap.get("name"), "MacBook Pro");
		softAssert.assertEquals(actualProductInfoMap.get("Brand"), "Apple");
		softAssert.assertEquals(actualProductInfoMap.get("price"), "$2,000.00");
		softAssert.assertAll();
	}
	
}
