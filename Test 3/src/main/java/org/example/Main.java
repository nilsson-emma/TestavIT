package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.net.URLEncoder;
//import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.Keys.ENTER;

// Här börjar programmet
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
        // Gör att Chrome-drivern finns tillgänglig globalt och därmed också för Chromedriver-konstruktorn
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        // Instansiera en ny WebDriver-instans (wrapper)
        WebDriver driver = new ChromeDriver();

        // NAVIGATE TO FACEBOOK
        logger.info("Navigate to Facebook.");
        driver.get("https://www.facebook.com/");

        // FILL OUT LOGIN FORMS
        logger.info("Find and fill out login form e-mail or phone number.");
        // Hitta "e-post- eller telefonnr-rutan" med hjälp av dess ID
        WebElement emailorphone = driver.findElement(By.id("email"));

        // Skriv epost eller telefonnr i rutan
        emailorphone.sendKeys(email);

        logger.info("Find and fill out login form e-mail or phone number. Click Log In.");
        // Skriv in lösenord i lösenordsfältet
        WebElement passwordField = driver.findElement(By.name("pass"));
        passwordField.sendKeys(password);

        // Klicka på Logga in-knappen: först identifiera den, sedan klicka på den. (Klassen kan dock användas på flera ställen och formodligen kunna byta namn över tid.)
        WebElement loginButton;
        loginButton = driver.findElement(By.cssSelector("._42ft"));

        loginButton.click();

        // ENTER THE SEARCH QUERY (e.g. name of a user, group or page) in the search bar. Click on the "Search" button.
        // Vänta 2,5 sekunder så att hemsidan hinner ladda
        logger.info("Wait 2,5 sec for the web page to load.");
        try {
            Thread.sleep(2500);
        }
        catch (Exception ex) {
        }

        //WebDriverWait wait = new WebDriverWait(driver, 3);
        ////WebElement searchInput = driver.findElement(By.cssSelector("input[type='Search']"));
        ////driver.findElements(By.tagName("input"));

        // Söker på mySearchTerm
        logger.info("Search for custum search term.");
        String mySearchTerm = "östen";

        List<WebElement> allFormChildElements = driver.findElements(By.tagName("input"));
        for (WebElement item : allFormChildElements)
        {
            if ((item.getAttribute("type").equals("search")) && (item.getAttribute("placeholder").equals("Sök på Facebook")))
            {
                item.sendKeys(mySearchTerm.toLowerCase());
                item.sendKeys(ENTER);
                break;
            }
        }

        // VERIFY THAT THE SEARCH RESULTS ARE ACCURATE AND RELEVANT
        // Vänta 2,5 sek så hela URL:n hinner bearbetas av browsern
        logger.info("Wait 2,5 sec. for the full URL to be processed by the browser.");
        try {
            Thread.sleep(2500);
        }
        catch (Exception ex) {
        }

        // Hämtar browserns URL för sökningens resultatsida, "html-omkodar" söksträngen och jämför url:en för resultatsidan med Facebooks globala struktur på URL för sökningar
        logger.info("Retrieve and print the current URL in the browser.");
        String currentUrl = driver.getCurrentUrl();
        currentUrl = currentUrl.replace("https://www.facebook.com/search/top/", "https://www.facebook.com/search/top");
        System.out.println(currentUrl);

        logger.info("Generate and print the generic search URL for the custom search.");
        String searchBaseUrl = "https://www.facebook.com/search/top?q=";
        String mySearchTermHtml = URLEncoder.encode(mySearchTerm.toLowerCase());
        System.out.println(searchBaseUrl + mySearchTermHtml);

        logger.info("Verify search results through comparison of URL:s");
        if (currentUrl.equals(searchBaseUrl + mySearchTermHtml)) {
            System.out.println("You have searched for the text you typed in.");
        }
        else {
            System.out.println("You have NOT searched for the text you typed in.");
        }
    }
}