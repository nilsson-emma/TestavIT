package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;

// Här börjar programmet
public class Main {

    // LOGGING MANAGEMENT
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("The test has started.");

        logger.info("Retrieve and print login credentials from file.");
        //Gör så att man kan hämta inloggningsuppgifter från en separat json-fil.
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
        //Gör att Chrome-drivern finns tillgänglig globalt och därmed också för Chromedriver-konstruktorn
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        //Instansiera en ny WebDriver-instans (wrapper)
        WebDriver driver = new ChromeDriver();

        // NAVIGATE TO FACEBOOK
        logger.info("Navigate to Facebook.");
        driver.get("https://www.facebook.com/");

        // FILL OUT LOGIN FORMS
        logger.info("Find and fill out login form e-mail or phone number.");

        // Find "e-mail- or Phone-window" with the help of it´s ID
        WebElement emailorphone = driver.findElement(By.id("email"));

        // Skriv epost eller telefonnr i rutan
        emailorphone.sendKeys(email);

        logger.info("Find and fill out login password. Click Log In.");
        // Skriv in lösenord i lösenordsfältet
        WebElement passwordField = driver.findElement(By.name("pass"));
        passwordField.sendKeys(password);

        // Klicka på Logga in-knappen: först identifiera den, sedan klicka på den. (Klassen kan dock användas på flera ställen och formodligen kunna byta namn över tid.)
        WebElement loginButton;
        loginButton = driver.findElement(By.cssSelector("._42ft"));

        loginButton.click();

        //LOGIN VERIFICATION
        ////Verify that the user is successfully logged in
        ////boolean isStatusInputFieldDisplayed = driver.findElement(By.xpath("//span[contains(text(),'Vad gör du just nu')]")).isDisplayed();

        WebElement statusFieldElement = null;

        //Verify if the login failed because the status-field was not found
        try {
            statusFieldElement = driver.findElement(By.xpath("//span[contains(text(),'Vad gör du just nu')]"));
        }
        //If the program crashes because the status field was not found:
        catch(Exception ex) {
            System.out.println("Login Failed!");
            logger.info("Couldn't find the status field, login failed.");
            return;
        }

        //Verify if the status-field was found and the login was successful
        logger.info("Login successful");
        //Om programmet hittat statusfältet skrivs Login Successful, annars skrivs Login Failed
        if (statusFieldElement.isDisplayed()) {
            System.out.println("Login Successful!");
        }
        else {
            System.out.println("Login Failed!");
            logger.info("Login failed.");
            return;
        }

        //GO TO MY OWN PROFILE PAGE
        //Verify that the profile page was found
        logger.info("Profile page found");
        // Redirected to their profile page by clicking the profile picture
        driver.get("https://www.facebook.com/me");
    }
}