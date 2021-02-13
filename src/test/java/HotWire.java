import com.codeborne.selenide.commands.ToString;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HotWire {

    @Test
    public void flightSearch() {
        //Open HotWire in Chrome browser
        FirefoxDriver browser = openBrowser("http://www.hotwire.com/ ");

        //Go to Bundles option
        bundlesOption(browser);

        //Search for the SFO to LAX flight
        inputCities(browser, "SFO", "LAX");

        //Calculate the flight days
        //Departing next day
        //Returning 20 days after
        flightDays(browser);

        //Click the Find Deal button
        findDeal(browser);

        //Wait for the page to load until the results are displayed
        results(browser);
    }

    public FirefoxDriver openBrowser(String site){
        //ChromeDriver browser;
        //System.setProperty("webdriver.chrome.driver","C:\\Utils\\chromedriver.exe");
        //browser = new ChromeDriver();
        System.setProperty("webdriver.gecko.driver", "c:\\Utils\\geckodriver.exe");
        FirefoxDriver browser = new FirefoxDriver();
        browser.get(site);
        return browser;
    }

    public void bundlesOption(FirefoxDriver browser){
        WebElement bundlesOption = browser.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/meso-native-marquee/div[1]/div/div/div[1]/div/div[2]/div[4]"));
        bundlesOption.click();
    }

    public void inputCities(FirefoxDriver browser, String departure, String destination) {
        browser.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        WebElement departureCity = browser.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div[2]/meso-native-marquee/div[1]/div/div/div/div/div[3]/form/div[2]/div/div/input"));
        departureCity.click();
        departureCity.sendKeys(departure);

        WebElement destinationCity = browser.findElementByXPath("//*[@id=\"root\"]/div[2]/div[2]/meso-native-marquee/div[1]/div/div/div[1]/div/div[3]/form/div[3]/div/div/input");
        destinationCity.click();
        destinationCity.sendKeys(destination);
    }

    public void flightDays(FirefoxDriver browser){
        LocalDate localDate = LocalDate.now();
        LocalDate departureDate = localDate.plusDays(1);
        LocalDate returningDate = localDate.plusDays(21);

        WebElement departureInput = browser.findElementByXPath("//*[@id=\"root\"]/div[2]/div[2]/meso-native-marquee/div[1]/div/div/div[1]/div/div[3]/form/div[4]/div/div/div/div/div[1]/div[2]");
        departureInput.click();//*[@id="tipContent-916"]
        browser.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        WebElement dateWidget = browser.findElementByXPath("//*[contains(text(),tipContent)]/span[2]/div/div[2]/div[2]/div/div[1]/table");
        List<WebElement> columns = dateWidget.findElements(By.tagName("td"));
        for (WebElement element:columns){
            String date=element.getText();
            if (date.equalsIgnoreCase(String.valueOf(departureDate.getDayOfMonth()))){
                element.click();
                break;
            }
        }
        WebElement dateWidgetMonth2 = browser.findElementByXPath("//*[contains(text(),tipContent)]/span[2]/div/div[2]/div[2]/div/div[2]/table");
        List<WebElement> columns2 = dateWidgetMonth2.findElements(By.tagName("td"));
        for (WebElement element:columns2) {
            String date = element.getText();
            if (date.equalsIgnoreCase(String.valueOf(returningDate.getDayOfMonth()))) {
                element.click();
                break;
            }
        }
    }
    public void findDeal(FirefoxDriver browser){
        WebElement findDealButton = browser.findElementByClassName("btn__label");
        findDealButton.click();
    }

    public void results(FirefoxDriver browser) {
        WebDriverWait wait = new WebDriverWait(browser, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"resultsContainer\"]")));
    }
}
