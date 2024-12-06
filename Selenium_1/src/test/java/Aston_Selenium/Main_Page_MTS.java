package Aston_Selenium;

import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class Main_Page_MTS extends Base_Of_Test {

	@DisplayName("Тестирование заголовка") // Нахождение заголовка
	@Test
	public void testBlockTitle() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // ожидание 10 секунд
		WebElement blockTitle = wait.until(ExpectedConditions.presenceOfElementLocated(BLOCK_TITLE));
		assertEquals("Онлайн пополнение без комиссии", blockTitle.getText().trim().replaceAll("\\s+", " "));
	}

	@DisplayName("Тестирование логотипа платёжных систем") // нахождение 5 img платёжных систем
	@Test
	public void testPaymentLogos() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement partnersElement = wait.until(ExpectedConditions.presenceOfElementLocated(PARTNERS_ELEMENT));
		List<WebElement> logos = partnersElement.findElements(PAYMENT_LOGOS);
		assertEquals(5, logos.size());
	}

	@DisplayName("Проверка ссылки") // проверка перехода по ссылке
	@Test
	public void testLink() {
		cookie_agree();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement link = wait.until(ExpectedConditions.elementToBeClickable(MORE_DETAILS_LINK));
		link.click();
		driver.navigate().back();
	}

	@DisplayName("Тестирование вкладки оплаты") // заполнение полей + проверка нового окна
	@ParameterizedTest
	@CsvSource({ "297777777,56,Ivan02@mail.ru, Номер карты, Срок действия, CVC, Имя держателя (как на карте)" })
	public void testFields(String phoneNumber, String sum, String email, String NumberOfCard, String TimeOutOfCard,
			String CVC_1, String NameOwnerOfCard) {
		cookie_agree();

		WebElement inputField = driver.findElement(PHONE_NUMBER_FIELD);
		inputField.sendKeys(phoneNumber);
		inputField = driver.findElement(SUM_FIELD);
		inputField.sendKeys(sum);
		inputField = driver.findElement(EMAIL_FIELD);
		inputField.sendKeys(email);
		WebElement next = driver.findElement(NEXT_BUTTON);
		next.click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Время ожидания - 10 секунд

		driver.switchTo().frame(wait.until(ExpectedConditions.visibilityOfElementLocated(DOM2)));

		wait.until(ExpectedConditions.visibilityOfElementLocated(CARD_NUMBER_FIELD));

		Assertions.assertEquals(NumberOfCard, driver.findElement(CARD_NUMBER_FIELD).getText());
		Assertions.assertEquals(TimeOutOfCard, driver.findElement(TIME_OUT_FIELD).getText());
		Assertions.assertEquals(CVC_1, driver.findElement(CVC).getText());
		Assertions.assertEquals(NameOwnerOfCard, driver.findElement(OWNER_OF_CARD).getText());

		Assertions.assertEquals(sum + ".00 BYN", driver.findElement(CONVERTED_COST).getText());
		Assertions.assertEquals("Оплата: Услуги связи Номер:375" + phoneNumber,
				driver.findElement(CONVERTED_TEXT).getText());
		Assertions.assertEquals("Оплатить " + sum + ".00 BYN", driver.findElement(CONVERTED_PAYMENT).getText());

		List<WebElement> icons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(CONVERTED_IMG));
		Assertions.assertEquals(5, icons.size());

	}

	@DisplayName("Тестирование placeholder'ов всех услуг") // Проверка плейсхолдеров
	@ParameterizedTest
	@CsvSource({
			"Номер телефона, Сумма, E-mail для отправки чека, Номер абонента,Номер счета на 44, Номер счета на 2073" })
	public void testPlaceHolders(String expectedPhoneNumberPlaceholder, String expectedSumPlaceholder,
			String expectedEmailPlaceholder, String expectedUserNumberPlaceholder,
			String expectedCard_44_NumberPlaceholder, String expectedCard_2073_NumberPlaceholder) {

		assertEquals(expectedPhoneNumberPlaceholder,
				driver.findElement(PHONE_NUMBER_FIELD).getDomAttribute("placeholder").trim().replaceAll("\\s+", " "));
		assertEquals(expectedUserNumberPlaceholder, driver.findElement(PHONE_NUMBER_FIELD_HOME)
				.getDomAttribute("placeholder").trim().replaceAll("\\s+", " "));
		assertEquals(expectedCard_44_NumberPlaceholder,
				driver.findElement(NUMBER_FIELD_CARD).getDomAttribute("placeholder").trim().replaceAll("\\s+", " "));
		assertEquals(expectedCard_2073_NumberPlaceholder,
				driver.findElement(NUMBER_FIELD_DUTY).getDomAttribute("placeholder").trim().replaceAll("\\s+", " "));

		assertEquals(expectedSumPlaceholder,
				driver.findElement(SUM_FIELD).getDomAttribute("placeholder").trim().replaceAll("\\s+", " "));
		assertEquals(expectedSumPlaceholder,
				driver.findElement(SUM_FIELD_HOME).getDomAttribute("placeholder").trim().replaceAll("\\s+", " "));
		assertEquals(expectedSumPlaceholder,
				driver.findElement(SUM_FIELD_CARD).getDomAttribute("placeholder").trim().replaceAll("\\s+", " "));
		assertEquals(expectedSumPlaceholder,
				driver.findElement(SUM_FIELD_DUTY).getDomAttribute("placeholder").trim().replaceAll("\\s+", " "));
		assertEquals(expectedEmailPlaceholder,
				driver.findElement(EMAIL_FIELD).getDomAttribute("placeholder").trim().replaceAll("\\s+", " "));
		assertEquals(expectedEmailPlaceholder,
				driver.findElement(EMAIL_FIELD_HOME).getDomAttribute("placeholder").trim().replaceAll("\\s+", " "));
		assertEquals(expectedEmailPlaceholder,
				driver.findElement(EMAIL_FIELD_CARD).getDomAttribute("placeholder").trim().replaceAll("\\s+", " "));
		assertEquals(expectedEmailPlaceholder,
				driver.findElement(EMAIL_FIELD_DUTY).getDomAttribute("placeholder").trim().replaceAll("\\s+", " "));

	}

}
