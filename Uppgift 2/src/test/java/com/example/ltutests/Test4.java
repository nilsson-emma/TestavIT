package com.example.ltutests;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.sleep;


//Test for downloading course syllabus
public class Test4 {
    @Test
    public void DownloadSyllabus() {

        open("https://www.ltu.se/edu/course/I00/I0015N/I0015N-Test-av-IT-system-1.81215?kursView=kursplan");
        WebDriverRunner.getWebDriver().manage().window().maximize();

        // locate the cookie button
        SelenideElement cookie;
        cookie = $x("//button[text()='Tillåt alla cookies']");
        cookie.click();

        //Locates and clicks the web element vt23
        SelenideElement vt23 = $x("//a[text()='V23']");
        vt23.click();
        sleep(1000);

        //Downloads the course syllabus PDF
        SelenideElement download;
        try {
            download = $("a.utbplan-pdf-link");
            download.click();
            sleep(3000);
        } catch (Exception e) {
            System.out.println("Couldn't download file.");
            e.printStackTrace();
        }
    }
}
