package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class StartPage {
    private SelenideElement buyButton = $(byText("Купить"));
    private SelenideElement byCard = $(byText("Оплата по карте"));
    private SelenideElement buyOnCreditButton = $(byText("Купить в кредит"));
    private SelenideElement byCredit = $(byText("Кредит по данным карты"));

    public PaymentPage buyPaymentByCard() {
        buyButton.click();
        byCard.shouldBe(visible);
        return new PaymentPage();
    }

    public CreditPage buyPaymentByCredit() {
        buyOnCreditButton.click();
        byCredit.shouldBe(visible);
        return new CreditPage();
    }

}
