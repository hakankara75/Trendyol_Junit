package test2;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.TestBase;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertEquals;

//"https://www.trendyol.com/" sitesine git
//sayfanin handle degerini al
//arama kutusuna "erkek atlet" yaz
//arama sonuclarindan kac urun ciktigini al
//sol tarafta marka bolumunda Tutku checkbox sec
//secim sonuclarindan kac urun ciktigini al
public class C02_Erkek_Atlet extends TestBase {

    @Test
    public void name() {
        extentTest = extentReports.createTest("ExtentTest", "Trendyol Sözleşme ve Formlar bölümü Test Raporu");
        //"https://www.trendyol.com/" sitesine git
        driver.get("https://www.trendyol.com/");
        try {
            driver.findElement(By.id("Rating-Review")).click();
            driver.findElement(By.id("rejectAllButton")).click();
        } catch (Exception e) {

        }
        extentTest.info("https://www.trendyol.com/ sitesine gidildi");

        //sayfanin handle degerini al
        String ilkSayfa = driver.getWindowHandle();
        threadSleep(2);
        extentTest.info("sayfanin handle degeri alindi");

        //arama kutusuna "erkek atlet" yaz
        WebElement aramaKutusu = findXpathWebelement("//input[@data-testid='suggestion']");
        aramaKutusu.sendKeys("erkek atlet", Keys.ENTER);
        threadSleep(2);
        extentTest.info("arama kutusuna 'erkek atlet' yazildi");

        //arama sonuclarindan kac urun ciktigini al
        String aramaSonucu = findByXpathString("//div[@class='dscrptn']");
        threadSleep(2);
        String arama = aramaSonucu.trim().replaceAll("\\D", "");
        System.out.println(arama);
        extentTest.info("arama sonuclarindan kac urun ciktigini alindi");

        //sol tarafta marka bolumunda Tutku checkbox sec
        findByXpathClick("(//div[@class='fltr-item-text'])[1]");
        extentTest.info("sol tarafta marka bolumunda Tutku checkbox secildi");

        //secim sonuclarindan kac urun ciktigini al
        threadSleep(2);
        String secimSonucu = findByXpathString("//*[text()='\" araması için ']//*");
        System.out.println(secimSonucu);
        threadSleep(2);
        String secim = secimSonucu.trim().replaceAll("\\D", "");
        System.out.println(secim);
        extentTest.info("secim sonuclarindan kac urun ciktigini alindi");

        //secim sonucunda urun sayisinin secim onceis urun sayisi ile ayni olmadigini dogrula
        Assert.assertNotEquals(arama, secim);
        extentTest.info("secim sonucunda urun sayisinin secim onceis urun sayisi ile ayni olmadigini dogrulandi");

        //sonuclarda kadin atleti oldugunu dogrula
        List<WebElement>atlet = null;

        try {
            for (int i = 0; i < 50; i++) {
            atlet= driver.findElements(By.xpath("//div[@class='p-card-wrppr with-campaign-view']"));
                pageDown();

            }
        }catch (Exception e) {

        }


        int sayac = 0;
        for (WebElement w : atlet) {
            if (w.getText().contains("kadın") || w.getText().contains("Kadın")) {
                                webElementScreenShoot(w);

                sayac++;
            }
        }
        System.out.println("Listelenen ürünlerde " + sayac + " adet Kadın ürünü var");
        int size = atlet.size();
        System.out.println("Listin elaman sayisi: " + size);


        asserTextContainsAssertTrue(atlet.toString(), "Kadın");
        extentTest.info("secim sonucunda urun sayisinin secim onceis urun sayisi ile ayni olmadigini dogrulandi");
        extentTest.pass("Test sonlandırıldı");
    }
}
