package com.ifmo.soul_mate_testing

import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selectors.*
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.WebDriverLogs
import com.codeborne.selenide.logevents.SelenideLogger
import io.qameta.allure.selenide.AllureSelenide
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import org.openqa.selenium.Capabilities
import org.openqa.selenium.Keys
import org.openqa.selenium.logging.LogType
import org.openqa.selenium.logging.LoggingPreferences
import org.openqa.selenium.remote.DesiredCapabilities
import java.util.logging.Level
import org.openqa.selenium.remote.RemoteWebDriver

import org.openqa.selenium.remote.CapabilityType





class MainPageTest {
    private val mainPage = MainPage()

    @BeforeEach
    fun setUpAllure() {
        SelenideLogger.addListener("allure", AllureSelenide())
    }

    @BeforeEach
    fun setUp() {
        Configuration.startMaximized = true
        open("http://localhost:3000/")
    }


    @Test
    fun login() {
        mainPage.usernameField.sendKeys("god_6")
        mainPage.passwordField.sendKeys("1234")
        mainPage.loginButton.click()

        assertEquals("Заявки на спасение", element(byXpath("/html/body/div[@id='root']/div[@class='Menu']/h3/a")).ownText)
    }

    @Test
    fun businessCaseTest() {
        mainPage.usernameField.sendKeys("soul_86")
        mainPage.passwordField.sendKeys("1234")
        mainPage.loginButton.click()

        element(byXpath("/html/body/div[@id='root']/div[@class='Menu']/h3/a")).click()
//        sleep(10000)
//        val trainButtons = elements(byXpath(".//*[@class='btn btn-secondary']"))
        val trainButtons = elements(byText("Тренировать"))
//        val trainButtons = elements(byCssSelector("Тренировать"))
        sleep(500)
        for (elem in trainButtons)
//            if (elem.ownText != "Log out")
            for (i in 1..10) {
                elem.click()
                sleep(300)
            }
//        for (i in 1..10)
//            for (j in 0..9) {
//                element(byXpath("//*[@id=\"root\"]/div[2]/div[1]/div/div/div[2]/div[2]/div/div/div/div/div/div[$i]/div[3]/button")).click()
//                sleep(10)
//            }
        element(byClassName("MuiButton-label")).waitUntil(visible, 31000).click()
        assertEquals("Что может наш сервис?", element(byClassName("Home-subtitle")).ownText)
    }


//    @Test
//    fun search() {
//        mainPage.searchButton.click()
//
//        element(byId("header-search")).sendKeys("Selenium")
//        element(byXpath("//button[@type='submit' and text()='Search']")).click()
//
//        element(byClassName("js-search-input")).shouldHave(attribute("value", "Selenium"))
//    }

//    @Test
//    fun toolsMenu() {
//        mainPage.toolsMenu.hover()
//
//        element(byClassName("menu-main__popup-wrapper")).shouldBe(visible)
//    }
//
//    @Test
//    fun navigationToAllTools() {
//        mainPage.seeAllToolsButton.click()
//
//        element(byClassName("products-list")).shouldBe(visible)
//
//        assertEquals("All Developer Tools and Products by JetBrains", Selenide.title())
//    }
}
