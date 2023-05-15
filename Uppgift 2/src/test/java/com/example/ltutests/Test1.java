package com.example.ltutests;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

import com.codeborne.selenide.Configuration;

import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;


//Retrieve final examination information
public class Test1 {
    @Test
    public void ExaminationInformation() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);

        // Sett the WebDriver instance to Selenide
        setWebDriver(driver);

        // Opens the web page of ltu.se
        driver.get("https://www.ltu.se/student");

        // Finds a specific element by xpath and click it
        driver.findElement(By.xpath("/html/body/main/div/div/div[1]/div/div[1]/div/div/div[3]/a")).click();

        // Loads the JSON file containing login credentials
        File jsonFile = new File("C:\\temp\\ltu.json");

        //// Initializes userid and password as null
        String userid = null;
        String password = null;

        // Reads and parse the JSON file to extract login credentials
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonFile);

            // Extracts userid and password from the parsed JSON
            userid = jsonNode.get("ltuCredentials").get("email").asText();
            password = jsonNode.get("ltuCredentials").get("password").asText();
        } catch (
                IOException e) {
            // Prints the stack trace in case of IOException
            e.printStackTrace();
        }

        // Enters userid and password in their respective fields
        WebElement useridTextField = $("input[id='username']");
        useridTextField.sendKeys (userid);
        WebElement passwordTextField = $("input[id='password']");
        passwordTextField.sendKeys (password);

        // Fimds and clicks on the login button
        WebElement loginButton = $("input[class='btn-submit']");
        loginButton.click();

        //Finds and clicks the tentamen element
        WebElement tentamen = $("a[id$='261']");
        tentamen.click();

        //Finds and clicks the element tentamensschema
        WebElement tentamensschema = $x("//a[@href='https://tenta.ltu.se/index.jsp']");
        tentamensschema.click();

        //Switches to newly opened tab
        switchTo().window(1);
        //Finds and clicks the webelement search
        WebElement search = $x("//*[@id='enkel_sokfalt']");
        search.click();
        //Enters "I0015N" in the search field and hits RETURN
        search.sendKeys("I0015N");
        search.sendKeys(Keys.RETURN);

        //Finds and clicks the sokschema button
        WebElement sokschema = $x("//*[@id=\"enkel_sokknapp\"]");
        sokschema.click();

        //Finds and clicks the webelement course
        WebElement course = $x("//*[@id=\"enkel_resultat\"]/ul/li[1]/table/tbody/tr/td[2]/a");
        course.click();

        //Switches to newly opened tab
        switchTo().window(2);

        // Verifies the exam date
        try {
            // Gets the text of the element containing the exam date
            WebElement examDate = $x("//tr[contains(., '30 Maj')]");

            String dateText = examDate.getText();

            // Checks if the text contains "30 Maj" and prints corresponding message
            if (dateText.contains("30 Maj")) {
                System.out.println("The exam date is correct.");
            } else {
                System.out.println("The exam date is incorrect.");
            }
        } catch (Exception e) {
            // Prints the error message in case of any Exception
            System.out.println("An error occurred: " + e.getMessage());
        }
        // Sets reportsFolder to the desired location
        Configuration.reportsFolder = "target/screenshots";

        // File reference to the old screenshot
        File oldScreenshot = new File(Configuration.reportsFolder + "/final_examination.png");

        // Delete the old screenshot if it exists
        if (oldScreenshot.exists()) {
            oldScreenshot.delete();
        }
        // Takes screenshot
        File screenshot = new File(screenshot("final_examination.jpeg"));

        // Prints out the path of the screenshot for debugging purposes:
        System.out.println("Screenshot taken: " + screenshot.getAbsolutePath());

    } //End of method
} //End of Main
