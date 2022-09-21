package com.qa.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.utils.Constants;
import com.qa.opencart.utils.ElementUtil;

import io.qameta.allure.Step;

public class LoginPage {

	//declare private driver
		private WebDriver driver;
		private ElementUtil eleUtil;
		
		//page constructor
		public LoginPage(WebDriver driver)
		{
			this.driver = driver;
			eleUtil = new ElementUtil(driver);
		}
		
		//By locators
		
		private By emailID = By.id("input-email");
		private By password = By.id("input-password");
		private By loginButton = By.xpath("//input[@type = 'submit']");
		private By forgotPwdLink = By.linkText("Forgotten Password");
		private By registerLink = By.linkText("Register");
		private By loginErrorMsg = By.cssSelector("div.alert.alert-danger.alert-dismissible");
		
		
		
		
		//Page Actions
		@Step("getting login page title")
		public String getLoginPageTitle()
		{
			return eleUtil.doGetTitle(Constants.LOGIN_PAGE_TITLE, Constants.DEFAULT_TIME_OUT);
		}
		
		@Step("getting login page url")
		public boolean getLoginPageUrl()
		{
			return eleUtil.waitForURLToContain(Constants.LOGIN_PAGE_URL_FRACTION , Constants.DEFAULT_TIME_OUT);
		}
		
		@Step("checking forgot pwd link exist or not")
		public boolean isForgotPwdLinkExist()
		{
			return eleUtil.doIsDisplayed(forgotPwdLink);
		}
		
		@Step("checking registration link exist or not")
		public boolean isRegisterLinkExist()
		{
			return eleUtil.doIsDisplayed(registerLink);
		}
		
		@Step("do login with username : {0} and password : {1}")
		public AccountsPage doLogin(String un , String pwd)
		{
			System.out.println("login with : " +un + ":" + pwd);
			eleUtil.doSendKeys(emailID, un);
			eleUtil.doSendKeys(password, pwd);
			eleUtil.doClick(loginButton);
			return new AccountsPage(driver);
		}
		
		@Step("do login with wrong username : {0} and wrong password : {1}")
		public boolean doLoginWithWrongCredentials(String un , String pwd)
		{
			System.out.println("try to login with wrong credentials : " +un + ":" + pwd);
			eleUtil.doSendKeys(emailID, un);
			eleUtil.doSendKeys(password, pwd);
			eleUtil.doClick(loginButton);
			String errorMsg =	eleUtil.doGetText(loginErrorMsg);
			System.out.println(errorMsg);
			if (errorMsg.contains(Constants.LOGIN_ERROR_MESSG)) {
				System.out.println("login is not successful");
				return false;
			}
			return true;
		}

		@Step("navigating to registration page")
		public RegistrationPage goToRegisterPage()
		{
			eleUtil.doClick(registerLink);
			return new RegistrationPage(driver);
		}
	
	
	
}
