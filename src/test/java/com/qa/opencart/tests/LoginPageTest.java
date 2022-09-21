package com.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.qa.opencart.listeners.TestAllureListener;
import com.qa.opencart.utils.Constants;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Epic("Epic 100 : Design OpenCart Application - Login Page")
@Story("US 101: OPen Cart Login Page Design with multiple features")
@Listeners(TestAllureListener.class)
public class LoginPageTest extends BaseTest {

	@Description("login page title test")
	@Severity(SeverityLevel.MINOR)
	@Test(priority = 1)
	public void loginPageTitleTest() {

		String actualTitle = loginPage.getLoginPageTitle();
		System.out.println("page title is : " + actualTitle);
		Assert.assertEquals(actualTitle, Constants.LOGIN_PAGE_TITLE);
	}

	@Description("login page url test")
	@Severity(SeverityLevel.NORMAL)
	@Test(priority = 2)
	public void loginPageUrlTest() {
		Assert.assertTrue(loginPage.getLoginPageUrl());
	}

	@Description("login page forgot pwd test")
	@Severity(SeverityLevel.CRITICAL)
	@Test(priority = 3)
	public void forgotPwdTest() {
		Assert.assertTrue(loginPage.isForgotPwdLinkExist());
	}

	@Description("login page register link test")
	@Severity(SeverityLevel.CRITICAL)
	@Test(priority = 4)
	public void registerLinkTest() {
		Assert.assertTrue(loginPage.isRegisterLinkExist());
	}

	@Description("login page login test with correct credentials")
	@Severity(SeverityLevel.BLOCKER)
	@Test(priority = 5)
	public void LoginTest() {
		accountsPage = loginPage.doLogin(prop.getProperty("username").trim(), prop.getProperty("password").trim());

		Assert.assertEquals(accountsPage.getAccountPageTitle(), Constants.ACCOUNTS_PAGE_TITLE);
	}

}
