package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage {
    private SelenideElement cardNumberLine = $("[placeholder='0000 0000 0000 0000']");
    private SelenideElement cardSize = $("[maxlength='19']");

    private SelenideElement monthLine = $("[placeholder='08']");

    private SelenideElement yearLine = $("[placeholder='22']");
    private SelenideElement ownerLine = $$("[class='input__control']").get(3);
    private SelenideElement CvcLine = $("[placeholder='999']");
    private SelenideElement buttonContinue = $(byText("Продолжить"));

    private SelenideElement successfulSending = $(withText("Операция одобрена Банком."));
    private SelenideElement failedSending = $(withText("Ошибка! Банк отказал в проведении операции."));
    private SelenideElement invalidFormat = $(withText("Неверный формат"));
    private SelenideElement invalidCardExpirationDate = $(withText("Неверно указан срок действия карты"));
    private SelenideElement cardExpired = $(withText("Истёк срок действия карты"));
    private SelenideElement requiredLine = $(withText("Поле обязательно для заполнения"));

    public void fillOutLine(String cardNumber, String month, String year, String owner, String cvc) {
        cardNumberLine.setValue(cardNumber);
        monthLine.setValue(month);
        yearLine.setValue(year);
        ownerLine.setValue(owner);
        CvcLine.setValue(cvc);
        buttonContinue.click();
    }

    public void messageAboutSuccessfullPayment() { //сообщение об успешной оплате
        successfulSending.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void messageAboutUnsuccessfullPaymentRefused() { //сообщение о неудачной оплате-отказ
        failedSending.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void messageAboutIncorrectDataFormat() { //сообщение о некорректном формате данных
        invalidFormat.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void messageAboutIncorrectCardExpirationDate() { //сообщение о неверном указании срока  действия карты
        invalidCardExpirationDate.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void messageAboutCardExpiration () { //сообщение об истечении срока действия карты
        cardExpired.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void messageAboutTheMandatoryFillingInOfTheField () { //сообщение об обязательности заполнения поля
        cardExpired.shouldBe(visible, Duration.ofSeconds(10));
    }

}

