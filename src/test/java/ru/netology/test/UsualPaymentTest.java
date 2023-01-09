package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import lombok.val;

import org.junit.jupiter.api.*;

import ru.netology.data.CardInfo;
import ru.netology.data.DataHelperSQL;
import ru.netology.page.StartPage;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.netology.data.DataHelper.*;

import static com.codeborne.selenide.Selenide.*;


public class UsualPaymentTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080/");
        DataHelperSQL.clearTables();
    }

    // #1
    @SneakyThrows
    @Test
    void shouldStatusBuyPaymentValidActiveCard() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkApprovedForm();
        assertEquals("APPROVED", DataHelperSQL.getPaymentStatus());
    }

    //# 2 - failed
    @SneakyThrows
    @Test
    void shouldStatusBuyPaymentValidDeclinedCard() {
        CardInfo card = new CardInfo(getValidDeclinedCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkDeclinedForm();
        assertEquals("DECLINED", DataHelperSQL.getPaymentStatus());
    }

    //# 3
    @SneakyThrows
    @Test
    void shouldBuyPaymentInvalidCard() {
        CardInfo card = new CardInfo(getInvalidNumberCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkDeclinedForm();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #4
    @SneakyThrows
    @Test
    void shouldBuyPaymentInvalidPatternCard() {
        CardInfo card = new CardInfo(getInvalidPatternNumberCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkCardNumberError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #5
    @SneakyThrows
    @Test
    void shouldBuyPaymentEmptyCard() {
        CardInfo card = new CardInfo(getEmptyNumberCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkCardNumberError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #6 failed
    @SneakyThrows
    @Test
    void shouldBuyPaymentZeroCard() {
        CardInfo card = new CardInfo(getZeroNumberCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkCardNumberError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #7 failed
    @SneakyThrows
    @Test
    void shouldBuyPaymentInvalidMonthCardExpiredCardError() {
        CardInfo card = new CardInfo(getValidActiveCard(), getPreviousMonth(), getCurrentYear(), getValidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkExpiredCardError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #8
    @SneakyThrows
    @Test
    void shouldBuyPaymentInvalidMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getInvalidMonth(), getCurrentYear(), getValidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkMonthError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #9 failed
    @SneakyThrows
    @Test
    void shouldBuyPaymentZeroMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getZeroMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkMonthError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #10
    @SneakyThrows
    @Test
    void shouldBuyPaymentEmptyMonth() {
        CardInfo card = new CardInfo(getValidActiveCard(), getEmptyMonth(), getNextYear(), getValidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkMonthError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #11
    @SneakyThrows
    @Test
    void shouldBuyPaymentInvalidYearCard() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getPreviousYear(), getValidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkYearError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #12
    @SneakyThrows
    @Test
    void shouldBuyPaymentEmptyYear() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getEmptyYear(), getValidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkYearError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #13
    @SneakyThrows
    @Test
    void shouldBuyPaymentZeroYear() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getZeroYear(), getValidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkYearError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #14 failed
    @SneakyThrows
    @Test
    void shouldBuyPaymentRussianOwner() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getInvalidLocaleOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkOwnerError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #15 failed
    @SneakyThrows
    @Test
    void shouldBuyPaymentFirstNameOwner() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getInvalidOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkOwnerError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #16
    @SneakyThrows
    @Test
    void shouldBuyPaymentEmptyOwner() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getEmptyOwner(), getValidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkOwnerError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #17
    @SneakyThrows
    @Test
    void shouldBuyPaymentInvalidCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getInvalidCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkCVCError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #18
    @SneakyThrows
    @Test
    void shouldBuyPaymentEmptyCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getEmptyCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkCVCError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

    // #19 failed
    @SneakyThrows
    @Test
    void shouldBuyPaymentZeroCVC() {
        CardInfo card = new CardInfo(getValidActiveCard(), getCurrentMonth(), getNextYear(), getValidOwner(), getZeroCVC());
        val mainPage = new StartPage();
        mainPage.checkPaymentButton().
                fillingForm(card).
                checkCVCError();
        assertNull(DataHelperSQL.getPaymentStatus());
    }

}