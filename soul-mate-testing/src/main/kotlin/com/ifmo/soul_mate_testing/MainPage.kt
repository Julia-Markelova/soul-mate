package com.ifmo.soul_mate_testing

import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element

class MainPage {
//    val seeAllToolsButton = element("a.wt-button_mode_primary")
//    val toolsMenu = element(byXpath("//div[contains(@class, 'menu-main__item') and text() = 'Tools']"))
//    val searchButton = element("[data-test=menu-main-icon-search]")
    val usernameField = element(byXpath("/html/body/div[@id='root']/div[@class='Menu']/input[@class='menu-input form-control'][1]"))
    val passwordField = element(byXpath("/html/body/div[@id='root']/div[@class='Menu']/input[@class='menu-input form-control'][2]"))
    val loginButton = element(byXpath("/html/body/div[@id='root']/div[@class='Menu']/button[@class='btn btn-secondary']"))
}
