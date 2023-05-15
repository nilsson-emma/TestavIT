package com.example.ltutests;

import org.junit.jupiter.api.Test;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

//Download course syllabus
public class Test4 {
    @Test
    public void DownloadSyllabus() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        var driver = new ChromeDriver(options);

        // Sett the WebDriver instance to Selenide
        setWebDriver(driver);

        // Opens the course plan web page of ltu.se
        driver.get("https://www.ltu.se/edu/course/I00/I0015N/I0015N-Test-av-IT-system-1.81215?kursView=kursplan");

        // locate the cookie button
        SelenideElement cookie;
        cookie = $x("//button[text()='Tillåt alla cookies']");
        cookie.click();

        //Locates and clicks the web element vt23
        SelenideElement vt23 = $x("//a[text()='V23']");
        vt23.click();
        sleep(1000);

        SelenideElement download;
        try {
            // File testAvItSyllabus = $("a.utbplan-pdf-link").download();
            download = $("a.utbplan-pdf-link");
            download.click();
            sleep(3000);
        } catch (Exception e) {
            System.out.println("Couldn't download file.");
            e.printStackTrace();
        }
    }
}
