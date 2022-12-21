package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
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


public class CreditPaymentTest {
    CreditPage creditPage = new CreditPage();
    StartPage startPage = new StartPage();

    @BeforeEach
    void CleanDataBaseAndOpenWeb() {
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

    // Двойное имя в поле Владелец
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

    // 1. В поле "Номер карты" ввести номер карты, содержащий меньше 16 цифр.
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

    // 2. В поле "Номер карты" ввести номер карты, содержащий 16 нулей
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

    // 3. В поле "Номер карты" ввести номер карты, содержащий латинские буквы, арабскую вязь, иероглифы, спецсимволы
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

    // 4. Поле "Номер карты" оставить пустым
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

    // 5. В поле "Месяц" ввести латинские буквы, арабскую вязь, иероглифы, спецсимволы
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

    // 6. В поле "Месяц" ввести номер месяца больше 12
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

    //7_Негативный тест с пустым полем месяца
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

    // 8. В поле "Год" ввести прошедший год
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

    // 9. В поле "Год" ввести год на 6 лет больше текущего года
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

    // 10. Нули в поле Год
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

    // 11. Символы в поле Год
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

    // 12. Поле "Год" оставить пустым
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

    // 13. В поле "Владелец" ввести только имя
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

    // 14. В поле "Владелец" ввести только имя фамилию и отчество с маленькой буквы
    @Test
    void shouldNameAndPatronymicWithSmallLetterInTheOwner() {
        var cardNumber = DataHelper.getFirstCardNumber();
        var month = DataHelper.getValidMonth();
        var year = DataHelper.getValidYear();
        var owner = DataHelper.getNameNndPatronymicWithSmallLetterInTheOwner();
        var cvc = DataHelper.getValidCvc();
        creditPage.fillOutLine(cardNumber, month, year, owner, cvc);
        creditPage.messageAboutSuccessfullPayment();
    }

    // 15. В поле "Владелец" ввести более 30 символов
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

    // 16. В поле "Владелец" ввести латинские буквы, арабскую вязь, иероглифы, спецсимволы
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

    // 17. Поле "Владелец" оставить пустым
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

    // 18. Тест с нулями в поле CVC
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

    //19. тест с символами, иероглифами в поле Cvc
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

    // 20. Тест с пустым полем CVC
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
