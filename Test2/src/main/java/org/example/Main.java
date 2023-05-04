package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.io.File;
import java.io.IOException;


//Here the program starts
public class Main {

    // LOGGING MANAGEMENT
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("The test has started.");

        logger.info("Retrieve and print login credentials from file.");
        // RETRIEVE LOGIN CREDENTIALS (from a separate json-fil)
        File jsonFile = new File("C:\\temp\\facebook.json");

        String email = null;
        String password = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonFile);

            email = jsonNode.get("facebookCredentials").get("email").asText();
            password = jsonNode.get("facebookCredentials").get("password").asText();

            System.out.println("Email: " + email);
            System.out.println("Password: " + password);
        } catch (IOException e) {
            logger.info("Couldn't retrieve login credentials properly.");
            e.printStackTrace();
        }
        // CHROME-DRIVER
        logger.info("Access and instantiate Chrome-driver.");
        //Makes the Chrome-driver accesible globally and thereby also for the Chromedriver-construktor
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        //Use of the Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--disable-notifications");

        //Instansiate a new WebDriver-instans (wrapper)
        WebDriver driver = new ChromeDriver(options);

        // NAVIGATE TO FACEBOOK
        logger.info("Navigate to Facebook.");
        driver.get("https://www.facebook.com/");

        // FILL OUT THE LOGIN FORMS
        logger.info("Find and fill out login form e-mail or phone number.");
        // Find "e-mail- or Phone-window" with the help of it´s ID
        WebElement emailorphone = driver.findElement(By.id("email"));

        // Write email or telephone nr in the field
        emailorphone.sendKeys(email);

        logger.info("Find and fill out login form e-mail or phone number. Click Log In.");
        // Write the password in the password field
        WebElement passwordField = driver.findElement(By.name("pass"));
        passwordField.sendKeys(password);

        // Click the login bitton: first identify it, then click it. (This class kan be used on different places, and can presumably change it´s name over time)
        WebElement loginButton;
        loginButton = driver.findElement(By.cssSelector("._42ft"));

        loginButton.click();

        // Wait for the cookie popup window to appear and then dismiss it
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
        // Find the status-element using the xpath
        WebElement createPostButton =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'xv8uw2v')]")));

        // Click on the element
        createPostButton.click();

        //SEND IN TEXT
        WebElement createPostTextInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@aria-label, 'Vad gör du just nu') and @role='textbox']")));
        createPostTextInput.sendKeys("This is a Test");

        WebElement postButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@aria-label, 'Publicera')]")));
        postButton.click();

        } catch (Exception e) {
            // LOG THE EXCEPTION
            logger.error("An exception occurred while posting on Facebook: ", e);
        }

    } //Here the Main ends
} //End of the program
