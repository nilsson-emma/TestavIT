package com.example.ltutests;

import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;


//Log out from LTU
public class Test5 {
    @Test
    public void Logout() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);

        // Set the WebDriver instance to Selenide
        setWebDriver(driver);

        // Opens the web page of ltu.se
        driver.get("https://www.ltu.se/student");

        // Finds a specific element by xpath and click it
        $("html > body > main > div > div > div:nth-of-type(1) > div > div:nth-of-type(1) > div > div > div:nth-of-type(3) > a").click();

        // Loads the JSON file containing login credentials
        File jsonFile = new File("C:\\temp\\ltu.json");

        // Initializes userid and password as null
        String userid = null;
        String password = null;

        // Reads and parses the JSON file to extract login credentials
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonFile);

            // Extracts userid and password from the parsed JSON
            userid = jsonNode.get("ltuCredentials").get("email").asText();
            password = jsonNode.get("ltuCredentials").get("password").asText();
        } catch (IOException e) {
            // Prints the stack trace in case of IOException
            e.printStackTrace();
        }

        // Enters userid and password in their respective fields
        WebElement useridTextField = $("input[id='username']");
        useridTextField.sendKeys (userid);
        WebElement passwordTextField = $("input[id='password']");
        passwordTextField.sendKeys (password);

        // Finds and clicks on the login button
        WebElement loginButton = $("input[class='btn-submit']");
        loginButton.click();

        // Find the drop-down element that contains Log out.
        $(".user-avatar-link.dropdown-toggle").click();

        // Click on Log out.
        $("a[href='/c/portal/logout']").click();
    }
}
