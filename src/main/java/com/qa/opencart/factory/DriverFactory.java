package com.qa.opencart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {

	public WebDriver driver;
	public Properties prop;
	public static String highlight;
	public OptionsManager optionsManager;

	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

	/**
	 * This method is used to initialize the webdriver this will return the driver
	 */

	public WebDriver initDriver(Properties prop) {

		String browserName = prop.getProperty("browser");
		String browserVersion = prop.getProperty("browserversion").trim();
		
		System.out.println("browser name is : " + browserName);
		highlight = prop.getProperty("highlight");
		optionsManager = new OptionsManager(prop);

		if (browserName.equals("chrome")) {
			WebDriverManager.chromedriver().setup();

			if (Boolean.parseBoolean(prop.getProperty("remote"))) {
				// remote code
				init_remoteDriver("chrome" , browserVersion);
			} else {
				// local
				tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
			}

		} else if (browserName.equals("firefox")) {
			WebDriverManager.firefoxdriver().setup();

			if (Boolean.parseBoolean(prop.getProperty("remote"))) {
				// remote code
				init_remoteDriver("firefox" , browserVersion);
				//local
			} else {
				tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));

			}
		} else {
			System.out.println("please pass the correct browser : " + browserName);
		}

		getDriver().manage().window().maximize();
		getDriver().manage().deleteAllCookies();
		openUrl(prop.getProperty("url"));

		return getDriver();

	}

	private void init_remoteDriver(String browser , String browserVersion) {
		System.out.println("Running test on remote grid server:" + browser);
		if (browser.equalsIgnoreCase("chrome")) {
			DesiredCapabilities cap = new DesiredCapabilities();
			//cap.setBrowserName("chrome");
			
			cap.setCapability("browserName", "chrome");
			cap.setCapability("browserVersion", browserVersion);
			cap.setCapability("enableVNC", true);
			cap.setCapability(ChromeOptions.CAPABILITY, optionsManager.getChromeOptions());
			try {
				tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), cap));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (browser.equalsIgnoreCase("firefox")) {
			DesiredCapabilities cap = new DesiredCapabilities();
			//cap.setBrowserName("firefox");
			
			cap.setCapability("browserName", "firefox");
			cap.setCapability("browserVersion", browserVersion);
			cap.setCapability("enableVNC", true);
			cap.setCapability(FirefoxOptions.FIREFOX_OPTIONS, optionsManager.getFirefoxOptions());
			{
				try {
					tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), cap));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * getdriver(): it will return a thread local copy of the webdriver
	 * 
	 * @return
	 */

	public static synchronized WebDriver getDriver() {
		return tlDriver.get();
	}

	/**
	 * this method is used to initialize the properties
	 * 
	 * @return this will return properties prop reference
	 */

	public Properties initProp() {
		prop = new Properties();
		FileInputStream fis = null;
		String envName = System.getProperty("env");
		if (envName == null) {
			System.out.println("Running on Prod env");
			try {
				fis = new FileInputStream("./src/test/resources/config/config.properties");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Running on env : " + envName);
			try {
				switch (envName.toLowerCase()) {
				case "qa":
					fis = new FileInputStream("./src/test/resources/config/qa.config.properties");
					break;
				case "dev":
					fis = new FileInputStream("./src/test/resources/config/dev.config.properties");
					break;
				case "stage":
					fis = new FileInputStream("./src/test/resources/config/stage.config.properties");
					break;
				case "uat":
					fis = new FileInputStream("./src/test/resources/config/uat.config.properties");
					break;

				default:
					System.out.println("please pass the right environment.....");
					break;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		try {
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	/**
	 * take screenshot
	 * 
	 * @return
	 */

	public String getScreenshot() {
		File srcfile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir") + "/screenshots/" + System.currentTimeMillis() + ".jpg";
		File destination = new File(path);
		try {
			FileUtils.copyFile(srcfile, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * launch url
	 * 
	 * @return
	 */
	public void openUrl(String url) {
		try {
			if (url == null) {
				throw new Exception("url is null");
			}
		} catch (Exception e) {
		}
		getDriver().get(url);
	}

	public void openUrl(URL url) {
		try {
			if (url == null) {
				throw new Exception("url is null");
			}
		} catch (Exception e) {
		}
		getDriver().navigate().to(url);
	}

	public void openUrl(String baseUrl, String path) {
		try {
			if (baseUrl == null) {
				throw new Exception("url is null");
			}
		} catch (Exception e) {
		}
		getDriver().get(baseUrl + "/" + path);
	}

	public void openUrl(String baseUrl, String path, String queryParam) {
		try {
			if (baseUrl == null) {
				throw new Exception("url is null");
			}
		} catch (Exception e) {
		}
		getDriver().get(baseUrl + "/" + path + "?" + queryParam);
	}

}
