package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.DataHelperSQL;
import ru.netology.page.PaymentPage;
import ru.netology.page.StartPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UsualPaymentTest {
    PaymentPage paymentPage = new PaymentPage();
    StartPage startPage = new StartPage();

    @BeforeEach
    void CleanDataBaseAndOpenWeb() { //очистить базу данных и открыть веб страницу
        cleanDataBase();
        startPage = open("http://localhost:8080", StartPage.class);
        startPage.buyPaymentByCard();
    }

    private void cleanDataBase() {
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldApproveFirstCard() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment();
        var expected = DataHelper.getStatusFirstCard();
        var actual = DataHelperSQL.getPurchaseByDebitCard();
        assertEquals(expected, actual);
    }

    @Test
    void shouldApproveOwnerNameWithTheLetter() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getLetterEWithDots();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment();
        var expected = DataHelper.getStatusFirstCard();
        var actual = DataHelperSQL.getPurchaseByDebitCard();
        assertEquals(expected, actual);
    }

    @Test
    void shouldApproveDoubleNameOfTheOwner() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getDoubleNameOfTheOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment();
        var expected = DataHelper.getStatusFirstCard();
        var actual = DataHelperSQL.getPurchaseByDebitCard();
        assertEquals(expected, actual);
    }

    @Test
    void shouldApproveSecondCard() {
        var cardNumber = DataHelper.getSecondCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutUnsuccessfulPaymentRefused();
        var expected = DataHelper.getStatusSecondCard();
        var actual = DataHelperSQL.getPurchaseByDebitCard();
        assertEquals(expected, actual);
    }

    @Test
    void shouldLessThan16DigitsInTheCard() {
        var cardNumber = DataHelper.getLessThan16DigitsInTheCard();
        var month = DataHelper.getValidMonth(); //
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat();
    }

    @Test
    void should16ZerosInTheCard() {
        var cardNumber = DataHelper.get16ZerosInTheCard();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutUnsuccessfulPaymentRefused();
    }

    @Test
    void shouldLettersSymbolsTextInTheCard() {
        var cardNumber = DataHelper.getLettersSymbolsText();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat();
    }

    @Test
    void shouldEmptyFieldInTheCard() {
        var cardNumber = DataHelper.getEmptyFieldInTheCard();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat();
    }

    @Test
    void shouldLettersSymbolsTextInTheMonth() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getLettersSymbolsTextInTheMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat();
    }

    @Test
    void shouldMonthNumberMore12() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getMonthNumberMore12();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectCardExpirationDate();
    }

    @Test
    void shouldMonthFieldEmpty() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getMonthFieldEmpty();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat();
    }
    @Test
    void shouldYearFieldPrevious() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth(); //
        var year = DataHelper.getYearFieldPrevious();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutCardExpiration();
    }

    @Test
    void shouldYearMoreThan6YearsOfTheCurrentYear() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth(); //
        var year = DataHelper.getMoreThan6YearsOfTheCurrentYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectCardExpirationDate();
    }


    @Test
    void shouldYearZero() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth(); //
        var year = DataHelper.getYearZero();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutCardExpiration();
    }
    @Test
    void shouldLettersSymbolsTextInTheYear() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getLettersSymbolsTextInTheYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat();
    }

    @Test
    void shouldYearFieldEmpty() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getYearFieldEmpty();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat();
    }

    @Test
    void shouldOnlyNameOwner() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth(); //
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getOnNameOwnertr();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment();
    }


    @Test
    void shouldNameNndPatronymicWithSmallLetterInTheOwner() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getNameNndPatronymicWithSmallLetterInTheOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment();
    }

    @Test
    void shouldMoreThan30CharactersInTheOwner() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getMoreThan30CharactersInTheOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment();
    }

    @Test
    void shouldLettersSymbolsTextInTheOwner() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getLettersSymbolsTextInTheOwner();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment();
    }


    @Test
    void shouldOwnerFieldEmpty() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getOwnerFieldEmpty();
        var cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutTheMandatoryFillingInOfTheField();
    }


    @Test
    void shouldCvcZero() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getCvcZero();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment();
    }


    @Test
    void shouldLettersSymbolsTextInTheCvc() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getLettersSymbolsTextInTheCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat();
    }


    @Test
    void shouldEmptyFieldInTheCvc() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getCvcFieldEmpty();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat();
    }

}
