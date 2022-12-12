package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.DataHelperSQL;
import ru.netology.page.CreditPage;
import ru.netology.page.StartPage;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelperSQL.cleanDataBase;

public class CreditPaymentTest {
    CreditPage creditPage = new CreditPage();
    StartPage startPage = new StartPage();

    @BeforeEach
    void CleanDataBaseAndOpenWeb() {
        cleanDataBase();
        startPage = open("http://localhost:8080", StartPage.class);
        startPage.buyPaymentByCard();
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
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutSuccessfullPayment();
        var expected = DataHelper.getStatusFirstCard();
        var actual = DataHelperSQL.getPurchaseOnCreditCard();
        assertEquals(expected, actual);
    }


    @Test
    void shouldApproveOwnerNameWithTheLetter() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getLetterEWithDots();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutSuccessfullPayment();
        var expected = DataHelper.getStatusFirstCard();
        var actual = DataHelperSQL.getPurchaseOnCreditCard();
        assertEquals(expected, actual);
    }


    @Test
    void shouldApproveDoubleNameOfTheOwner() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getDoubleNameOfTheOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutSuccessfullPayment();
        var expected = DataHelper.getStatusFirstCard();
        var actual = DataHelperSQL.getPurchaseOnCreditCard();
        assertEquals(expected, actual);
    }

    @Test
    void shouldApproveSecondCard() {
        var cardNumber = DataHelper.getSecondCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutUnsuccessfullPaymentRefused();
        var expected = DataHelper.getStatusSecondCard();
        var actual = DataHelperSQL.getPurchaseOnCreditCard();
        assertEquals(expected, actual);
    }

    @Test
    void shouldLessThan16DigitsInTheCard() {
        var cardNumber = DataHelper.getLessThan16DigitsInTheCard();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutIncorrectDataFormat();
    }

    @Test
    void should16ZerosInTheCard() {
        var cardNumber = DataHelper.get16ZerosInTheCard();
        var month = DataHelper.getValidMonth(); //
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutUnsuccessfullPaymentRefused();
    }

    @Test
    void shouldLettersSymbolsTextInTheCard() {
        var cardNumber = DataHelper.getLettersSymbolsText();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutIncorrectDataFormat();
    }


    @Test
    void shouldEmptyFieldInTheCard() {
        var cardNumber = DataHelper.getEmptyFieldInTheCard();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutIncorrectDataFormat();
    }


    @Test
    void shouldLettersSymbolsTextInTheMonth() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getLettersSymbolsTextInTheMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutIncorrectDataFormat();
    }


    @Test
    void shouldMonthNumberMore12() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getMonthNumberMore12();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutIncorrectCardExpirationDate();
    }

    //2.7_Негативный тест с пустым полем месяца
    @Test
    void shouldMonthFieldEmpty() { //пустой месяц
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getMonthFieldEmpty();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutIncorrectDataFormat();
    }

    @Test
    void shouldYearFieldPrevious() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getYearFieldPrevious();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutCardExpiration();
    }

    @Test
    void shouldYearMoreThan6YearsOfTheCurrentYear() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getMoreThan6YearsOfTheCurrentYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutIncorrectCardExpirationDate();
    }


    @Test
    void shouldYearZero() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getYearZero();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutCardExpiration();
    }


    @Test
    void shouldLettersSymbolsTextInTheYear() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getLettersSymbolsTextInTheYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutIncorrectDataFormat();
    }


    @Test
    void shouldYearFieldEmpty() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getYearFieldEmpty();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutIncorrectDataFormat();
    }


    @Test
    void shouldOnlyNameOwner() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getOnNameOwnertr();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutSuccessfullPayment();
    }


    @Test
    void shouldNameNndPatronymicWithSmallLetterInTheOwner() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getNameNndPatronymicWithSmallLetterInTheOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutSuccessfullPayment();
    }

    @Test
    void shouldMoreThan30CharactersInTheOwner() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getMoreThan30CharactersInTheOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutSuccessfullPayment();
    }


    @Test
    void shouldLettersSymbolsTextInTheOwner() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getLettersSymbolsTextInTheOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutSuccessfullPayment();
    }


    @Test
    void shouldOwnerFieldEmpty() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getOwnerFieldEmpty();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutTheMandatoryFillingInOfTheField();
    }


    @Test
    void shouldCvcZero() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getCvcZero();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutSuccessfullPayment();
    }

    @Test
    void shouldLettersSymbolsTextInTheCvc() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getLettersSymbolsTextInTheCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutIncorrectDataFormat();
    }

    @Test
    void shouldEmptyFieldInTheCvc() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getValidOwner();
        var cvc = DataHelper.getCvcFieldEmpty();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutIncorrectDataFormat();
    }

}
