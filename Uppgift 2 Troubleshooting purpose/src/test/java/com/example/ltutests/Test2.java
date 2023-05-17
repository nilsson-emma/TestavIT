package com.example.ltutests;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;


// Create Transcript
    public class Test2 {

        @Test
        public void CreateTranscript() {
            //COMMON CODE FOR TEST 2 AND 3

            //Selenide:
            Configuration.browser = "chrome";
            Configuration.browserCapabilities.setCapability("disableNotifications", true);
            //Configuration.browserCapabilities.setCapability("remote-allow-origins", "*");

            //Selenium:
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            //Brief additional info about the remote allow origins: https://chromedriver.chromium.org/security-considerations

            //WebDriverRunner är Selenide. ChromeDriver(options) är Selenium.
            WebDriverRunner.setWebDriver(new ChromeDriver(options)); // Set up the WebDriver

            //Resterande är Selenide:
            // Navigate to LTU
            open("https://www.ltu.se/");
            WebDriverRunner.getWebDriver().manage().window().maximize();

            // Accept cookies by clicking on the button.
            $("button#CybotCookiebotDialogBodyButtonDecline").click();
            //$(By.cssSelector("button[id='CybotCookiebotDialogBodyButtonDecline']")).click();

            // Find the link "Student" and click on it.
            $("div.ltu-menu-login.is-hidden-touch a:nth-of-type(1)").click();
            //$(By.xpath("/html/body/header/div[2]/div[1]/div[1]/div[3]/div/a[1]")).click();

            // Find the link "Registerutdrag" and click on it.
            $("a[href='https://www.student.ladok.se/student/#/intyg']").click();
            //$(By.xpath("/html/body/main/div/div/div[1]/div/div[2]/div/div/div/div/ul/li[1]/a/div")).click();

            // Wait for the "inloggning via ditt lärosäte" to be visible and click on it.
            //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            //SelenideElement firstLadokButton = wait.until(ExpectedConditions.visibilityOfElementLocated(".btn-ladok-inloggning"));
            //firstLadokButton.click();

            //Find "Inloggning via ditt lärosäte" and click on it
            SelenideElement firstLadokButton = $(".btn-ladok-inloggning");
            firstLadokButton.click();

            // Retrieve login credentials (from a separate json-fil)
            File jsonFile = new File("C:\\Users\\emmae\\IdeaProjects\\ltu.json");

            String university = null;
            String uniHref = null;
            String email = null;
            String password = null;

           try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonFile);

                university = jsonNode.get("ltuCredentials").get("university").asText();
                uniHref = jsonNode.get("ltuCredentials").get("uniHref").asText();
                email = jsonNode.get("ltuCredentials").get("email").asText();
                password = jsonNode.get("ltuCredentials").get("password").asText();

            } catch (IOException e) {
                System.out.println("Couldn't retrieve login credentials properly.");
                e.printStackTrace();
            }

            //Find the text field to type in the university and click on it (the search field)
            SelenideElement clickToSearch = $("input[id='searchinput']");
            clickToSearch.click();
            //SelenideElement clickToSearch = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[id='searchinput']")));
            //clickToSearch.click();

            //Send the university name to the search field
            clickToSearch.sendKeys(university);

            //Click on the link that corresponds to the university name typed in
            SelenideElement confirmSearch = $("a[data-href='" + uniHref + "']");
            confirmSearch.click();
            //var confirmSearch = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[data-href='" + uniHref + "']")));
            //confirmSearch.click();
            try {
                //Find the text field intended for username and send text to it
                var usernameInput = $("input[id='username']");
                usernameInput.sendKeys(email);

                //Find the text field for password and send text to it
                var passwordInput = $("input[id='password']");
                passwordInput.sendKeys(password);

                //Find the login-button and click on it
                SelenideElement submitButton = $("input.btn-submit");
                //var submitButton = $(By.xpath("//input[@class='btn-submit']"));

                //Click on the submit button
                submitButton.click();

            } catch (Exception e) {
                System.out.println("Couldn't insert/submit login credentials properly.");
                e.printStackTrace();
            }

           //Find the element Transcripts/intyg and click on it
            SelenideElement transcriptButton = $("a[href='/student/app/studentwebb/intyg']");
            //if(!transcriptButton.isDisplayed()) {
            //    var menuButton = $(By.cssSelector("button[class='navbar-toggler']"));
            //    menuButton.click();
            //}
            transcriptButton = $("a[href='/student/app/studentwebb/intyg']");
            transcriptButton.click();

            //UNIQUE CODE FOR TEST 2 (NOT COMMON WITH TEST 3)

            //Find the button to start to generate a transcript and click on it
            try {
                var createButton = $(".btn-ladok-brand");
                createButton.click();

                //Wait for the dropdown to be visible
                //Selenide.Wait().withTimeout(Duration.ofSeconds(10));

                //Locate the dropdown and select an option by value
                $("#intygstyp").selectOptionByValue("1: Object");

                //Find the transcript's start date field and type in 2023-01-01
                var startDate = $("input[name='start']");
                startDate.sendKeys("2023-01-01");

                //Find the transcript's end date field and type in 2023-07-01
                var endDate = $("input[name='slut']");
                endDate.sendKeys("2023-07-01");

                //Find the radio button to choose English language of the transcript and select English
                var english = $("input[id='en']");
                english.click();

                //Find the final button to create the transcript and click on it
                var finalCreateButton = $(".me-lg-3");
          //      finalCreateButton.click();
            }
            catch (Exception e) {
                System.out.println("Couldn't apply transcript settings or create the transcript properly.");
                e.printStackTrace(
                );
            }
        }
    }

