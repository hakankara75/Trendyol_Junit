package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

   public abstract class TestBase {
        //TestBase class'indan obje olusturmanin onune gecilmesi icin abstract yapilabilir
//Orn: TestBase base=new TestBase();
//Bu class'a extend ettigimiz test classlarinda ulasabiliriz.
        protected static WebDriver driver;
        protected static ExtentReports extentReports; //Extent Report icin: Raporlamayı başlatır
        protected static ExtentHtmlReporter extentHtmlReporter;//Extent Report icinÇ Raporu HTML formatında düzenler
        protected static ExtentTest extentTest;//Extent Report icin: Tüm test aşamalarında extentTest objesi ile bilgi ekleriz

        @Before
        public void setUp() throws Exception {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

            //Extent Report icin asagisi
            //----------------------------------------------------------------------------------------
            extentReports = new ExtentReports();
            String tarih = new SimpleDateFormat("_hh_mm_ss_ddMMyyyy").format(new Date());
            String dosyaYolu = "TestOutput/reports/extentReport_" + tarih + ".html";
            extentHtmlReporter = new ExtentHtmlReporter(dosyaYolu);
            extentReports.attachReporter(extentHtmlReporter);
            //Raporda gözükmesini istediğimiz bilgiler için
            extentReports.setSystemInfo("Browser", "Chrome");
            extentReports.setSystemInfo("Tester", "Hakan");
            extentHtmlReporter.config().setDocumentTitle("Extent Report");
            extentHtmlReporter.config().setReportName("Smoke Test Raporu");
            extentTest = extentReports.createTest("ExtentTest", "Test Raporu");

        }

        @After
        public void tearDown() throws Exception {
            extentReports.flush(); //Extent Report icin
            Thread.sleep(3000);
            driver.quit();
        }
       /**
        bu metot ile alert deki metin alinir
        */
       public static void alertText() {
           driver.switchTo().alert().getText();
       }

       //DropDown VisibleText
    /*
        Select select2 = new Select(gun);
        select2.selectByVisibleText("7");
        //ddmVisibleText(gun,"7"); --> Yukarıdaki kullanım yerine sadece method ile handle edebilirim
     */

       /** Bu metot xpath ile alinan locate sendkey gonderir
        * @param xPath buraya elementin xpath locati verilecek
        * @param sendKeys buraya elemente dongerilecek metin girilecek
        */
       public void webElementSendKeys(String xPath, String sendKeys){
           driver.findElement(By.xpath(xPath)).sendKeys(sendKeys);

       }

       /**
        bu metot ile dropdown menude yazan bir text secilir
        @param ddm girilmesi gereken menunun locatidir
        @param secenek dropdown menunde yazan gorun textin string halidir
        */
       public static void ddmVisibleText(WebElement ddm, String secenek) {
           Select select = new Select(ddm);
           select.selectByVisibleText(secenek);
       }

       /**
        bu metot ile dropdown menudeki seceneklerden birisi index ile secilir
        @param ddm girilmesi gereken menunun locatidir
        @param index dropdown menunde yazan index numarasidir
        */
       public static void ddmIndex(WebElement ddm, int index) {
           Select select = new Select(ddm);
           select.selectByIndex(index);
       }
       /**
        bu metot ile dropdown menudeki valued seceneklerinden birisi strin gonderilerek secilir
        @paramType ddm girilmesi gereken menunun locatidir
        @paramType secenek dropdown menunde yazan ve girilmesi gereken value nun string halidir
        */
       public static void ddmValue(WebElement ddm, String secenek) {
           Select select = new Select(ddm);
           select.selectByValue(secenek);
       }

       /**
        bu metot ile acilan ilk pencereye donulur
        @param sayi girilmesi gereken gecilecek pencerenin indexidir
        */
       public static void switchToWindow1(int sayi) {
           List<String> tumWindowHandles = new ArrayList<String>(driver.getWindowHandles());
           driver.switchTo().window(tumWindowHandles.get(sayi));
       }

       /**
        bu metot ile acilan pencereye gecilir
        @param sayi girilmesi gereken gecilecek pencerenin indexidir
        */
       public static void switchToWindow2(int sayi) {
           driver.switchTo().window(driver.getWindowHandles().toArray()[sayi].toString());
       }

       /**
        * Explicit wait yapar
        bu metot ile bir element gorulene kadar kodlar bekletilir
        @param element girilmesi gereken locate dir
        @param sayi girilmesi gereken saniyedir
        */
       public static void visibleWait(WebElement element, int sayi) {
           WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(sayi));
           wait.until(ExpectedConditions.visibilityOf(element));
       }
       /**
        bu metot ile bir element gorulene kadar kodlar bekletme suresi return eder
        @param locator girilmesi gereken locate dir
        @param locator girilmesi gereken saniyedir
        */
       public static WebElement visibleWait(By locator, int sayi) {
           WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(sayi));
           return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
       }
       /**
        bu metot ile alert gorulene kadar kodlar bekletilir
        @param sayi girilmesi gereken saniyedir
        */
       public static void alertWait(int sayi) {
           WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(sayi));
           wait.until(ExpectedConditions.alertIsPresent());
       }
       /**
        bu metot ile tum sayfanin screenshot i alinir
        */
       public static void tumSayfaScreenShoot() {
           String tarih = new SimpleDateFormat("_hh_mm_ss_ddMMyyyy").format(new Date());
           String dosyaYolu = "TestOutput/screenshot" + tarih + ".png";
           TakesScreenshot ts = (TakesScreenshot) driver;
           try {
               FileUtils.copyFile(ts.getScreenshotAs(OutputType.FILE), new File(dosyaYolu));
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       }
       /**
        bu metot ile webelementin screenshot i alinir
        @param element girilmesi gereken locate dir
        */
       public static void webElementScreenShoot(WebElement element) {
           String tarih = new SimpleDateFormat("_hh_mm_ss_ddMMyyyy").format(new Date());
           String dosyaYolu = "TestOutput/webElementScreenshot" + tarih + ".png";
           try {
               FileUtils.copyFile(element.getScreenshotAs(OutputType.FILE), new File(dosyaYolu));
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       }
       /**
        bu metot ile mause element ustunde bekletilir
        @param webElement girilmesi gereken locate dir
        */
       public static void moveToElement(WebElement webElement) {
           Actions actions = new Actions(driver);
           actions.moveToElement(webElement).perform();
       }

       /**
        bu metot ile herhangi bir webelemente JavascriptExecutor kullanarak tiklayabilirim
        @param webElement girilmesi gereken locate dir
        */
       public static void clickByJavaScript(WebElement webElement) {
           JavascriptExecutor jse = (JavascriptExecutor) driver;

           jse.executeScript("arguments[0].click();", webElement);

       }

       /**
        * bu metot elementin ustune JavascriptExecutor ile goturur
        @param webElement girilmesi gereken locate dir
        */
       public static void scrollIntoViewByJavaScript(WebElement webElement) {
           JavascriptExecutor jse = (JavascriptExecutor) driver;//Casting
           jse.executeScript("arguments[0].scrollIntoView(true);", webElement);

       }

       /**
        * bu metot javascript kodu ile elemente string gonderir(java sendkey() ile ayni)
        @param webElement girilmesi gereken locate dir
        @param string locate gonderilecek olan deger
        */
       public static void sendKeyWithJavaScript(String string, WebElement webElement) {
           JavascriptExecutor jse = (JavascriptExecutor) driver;//Casting
           jse.executeScript("arguments[0].value = '"+string+"';", webElement);

       }
       /**
        bu metot javascript kodu ile sayfayi en alta goturur
        */
       public static void scrollEndByJavaScript() {
           JavascriptExecutor js = (JavascriptExecutor) driver;
           js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
       }

       /**
        bu metot javascript kodu ile sayfayi en yukari goturur
        */
       public static void scrollTopByJavaScript() {
           JavascriptExecutor js = (JavascriptExecutor) driver;
           js.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
       }

       /**bu metot sayfayi girilen string degerindeki elemente goturur
        * @param str girilmesi gereken elementin locatinin string halidir
        */
       public static void scrollToElementWithString(String str) {
           WebElement bottom = driver.findElement(By.xpath(str));
           Actions actions = new Actions(driver);
           actions.scrollToElement(bottom).perform();

       }

       /**
        * @param element girilmesi gereken locatidir
       bu metot sayfayi girilen elemente goturur
        */
       public static void scrollToElementWithWebElement(WebElement element) {
           WebElement bottom = element;
           Actions actions = new Actions(driver);
           actions.scrollToElement(bottom).perform();
       }

       /**
        * @param sleep girilmesi gereken saniye
       bu metot girilen saniye kadar java kodlarini bekletir
        */
       public static void threadSleep(int sleep) {
           try {
               Thread.sleep(sleep * 1000);
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
       }

       /**
        * @param str degeri expected metin
        * @param atr degeri actual metin
       bu metot expected metinin alertteki actual metini icerdigini dogrulamak icin
        */
       public static void assertTextContainsAssertTrue(String str, String atr) {
           assertTrue(str.contains(atr));
       }

       /**Bu metot bir webelementin secili olup olmadigini dogrular
        *  @param webElement girilecek webelement dir.
        */
       public void assertTrueIsSelected(WebElement webElement){
           Assert.assertTrue(webElement.isSelected());
       }

       /** Bu metot iki string degerin birbirine equal olup olmadigini dogrular
        @param str girilecek 1. metindir
        @param str1 girilecek 2. metindir
        */
       public void assertTrueEquals(String str, String str1){
           Assert.assertTrue(str.equals(str1));
       }

       /**
        bu metot alerti kabul eder
        */
       public static void switchAlertAccept() {
           driver.switchTo().alert().accept();
       }

       /**
        bu metot alerti reddeder
        */
       public static void switchAlertDismiss() {
           driver.switchTo().alert().dismiss();
       }

       /**
        * @param str olarak alerte gonderilecek metin girilmeli
       bu metot girilen metini alerte mesaj olarak gonderir
        */
       public static void switchAlertSendKey(String str) {
           driver.switchTo().alert().sendKeys(str);
       }

       /**
        * @param str olarak xpath locati girilmeli
       bu metot girilen xpath locati ile webelement olusturur
        */
       public static String findByXpathString(String str) {
           String location = driver.findElement(By.xpath(str)).getText();
           return location;
       }
       /**
        * @param str olarak xpath locati girilmeli
        *            bu metot girilen xpath locati ile webelement olusturur
        */
       public static WebElement findXpathWebelement(String str) {
           WebElement element = driver.findElement(By.xpath(str));
           return element;
       }
       /** bu metot girilen id locati ile webelement olusturur
        * @param str olarak id locati girilmeli
        */
       public static WebElement findByIdWebelement(String str) {
           WebElement w = driver.findElement(By.id(str));
           return w;
       }

       /**
        bu metot ekrani bir masue tekeri donmesi kadar asagi kaydirir
        */
       public static void pageDown() {
           Actions actions = new Actions(driver);
           actions.sendKeys(Keys.PAGE_DOWN).perform();
       }

       /**
        bu metot ekrani bir masue tekeri donmesi kadar yukari kaydirir
        */
       public static void pageUp() {
           Actions actions = new Actions(driver);
           actions.sendKeys(Keys.PAGE_UP).perform();
       }

       /**
        bu metot ekrani bir tik asagi kaydirir
        */
       public static void arrowDown() {
           Actions actions = new Actions(driver);
           actions.sendKeys(Keys.ARROW_DOWN).perform();
       }

       /**
        bu metot ekrani bir tik yukari kaydirir
        */
       public static void arrowUp() {
           Actions actions = new Actions(driver);
           actions.sendKeys(Keys.ARROW_UP).perform();
       }

       /** bu metot webelementin gorunur oldugunu dogrulamak icindir
        @param  webElement girilecek olan webelementdir
        */
       public static void assertTrueIsDisplayed(WebElement webElement) {
           assertTrue(webElement.isDisplayed());
       }

       /**
        @param  webElement girilmesi gereken element locati
        @param str gonderilmek istenen metin
         *bu metot ile metin kutusuna sendkeys gonderir
        */
       public static void sendAttributeJavaScript(WebElement webElement, String str) {
           JavascriptExecutor js = (JavascriptExecutor) driver;
           js.executeScript("arguments[0].setAttribute('value', '" + str + "')", webElement);
       }

       /**
        @param id girilmesi gereken id degeri
        @param attributeName gonderilmesi gereken attribute ismi
        bu metot ile girilen attribute degerleri ile texti alabilirim
        */
       public static void getValueByJavaScript(String id, String attributeName) {
           JavascriptExecutor js = (JavascriptExecutor) driver;
           String string = js.executeScript("return document.getElementById('" + id + "')." + attributeName + "").toString();
           System.out.println(string);
           //        NOT: document.querySelector("p").value;  -> TAG KULLANILABILIR
//             document.querySelector(".example").value; -> CSS DEGERI KULLANILABILIR
//             document.querySelector("#example").value; -> CSS DEGERI KULLANILABILIR
       }

       /**
        *  JavaScript ile webelement olusturma
        * @param javascriptYolu internet sitesinden sag klik ile JS yolunu kopyala ile alınan metin olacak
        */

       public static WebElement webelementJavaScript(String javascriptYolu) {
           JavascriptExecutor js = (JavascriptExecutor) driver;
           WebElement webElement = (WebElement) js.executeScript("return "+javascriptYolu+"");
           return webElement;
       }

       /**
        *  JavaScript ile webelement olusturup isEnabled oldugunu sorgulama
        * @param str internet sitesinden sag klik ile JS yolunu kopyala ile alınan metin olacak
        */
       public static void assertIsEnabled(String str){
           JavascriptExecutor js = (JavascriptExecutor) driver;
           WebElement webElement = (WebElement) js.executeScript("return "+str+"");
           Assert.assertTrue(webElement.isEnabled());
       }

       /**
        *  JavaScript ile cift klik yapma
        * @param element ile locate verilir
        */
       public static void doubleClick (WebElement element){
           Actions actions=new Actions(driver);
           actions.doubleClick(element).perform();
       }


       /**bu metot search boxa sendkeys gonderir
        * @param webElement girilmesi gereken element dir
        * @param str sendkey ile gonderilmek istenen metindir
        */
       public static void typeWithJavaScript(WebElement webElement, String str) {
           JavascriptExecutor js = (JavascriptExecutor) driver;
           js.executeScript("arguments[0].setAttribute('value', '" + str + "')", webElement);
       }


       /**
        * Bu metot ile pencere degistirilir. ikinci pencereye gecilir.
        * @param firstPage parametresine ilk pencerenin handle degeri girilir.
        */

       public void switchToHandle( String firstPage){
           firstPage=driver.getWindowHandle();
           Set<String> pagesHandles=driver.getWindowHandles();
           for (String str: pagesHandles){
               if(!str.equals(firstPage)){
                   driver.switchTo().window(str);

               }
           }

       }

       /**
        * Bu metot Action class kullanarak bir webelementin ustune gidip bekler
        * @param element yerine webelement'in locate koyulmalidir
        */
       public static void moveToElementWithAction(WebElement element){
           Actions action = new Actions(driver);
           action.moveToElement(element).perform();
           try {
               Thread.sleep(3000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
       //*[contains(@name,'q')]
       //*[contains(@title,'Ara')]
       //*[contains(@maxlength,'2048')]
       //*[contains(@maxlength,'20')]
       // * [cointains ( @Attribute = ’Value’) ]
    }


