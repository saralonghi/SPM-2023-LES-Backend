package com.example.pnmbackend.selenium.test;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestUAT {

    @Autowired
    WebDriver driver;

    JavascriptExecutor js;

    @BeforeEach
    void setUp() {
        js = (JavascriptExecutor) driver;
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    @Order(1)
    public void register() {
        // Test name: Register
        // Step # | name | target | value
        // 1 | open | /home |
        driver.get("http://localhost:4200/home");
        // 2 | setWindowSize | 1512x858 |
        driver.manage().window().setSize(new Dimension(1512, 858));
        // 3 | click | id=btn-register |
        driver.findElement(By.id("btn-register")).click();
        // 4 | click | id=name |
        driver.findElement(By.id("name")).click();
        // 5 | click | id=name |
        driver.findElement(By.id("name")).click();
        // 6 | type | id=name | Pasta di Aldo
        driver.findElement(By.id("name")).sendKeys("Pasta di Aldo");
        // 7 | click | id=email |
        driver.findElement(By.id("email")).click();
        // 8 | type | id=email | leonardo01.curzi@studenti.unicam.it
        driver.findElement(By.id("email")).sendKeys("leocurzi@yopmail.com");
        // 9 | click | id=fiscalid |
        driver.findElement(By.id("fiscalid")).click();
        // 10 | type | id=fiscalid | 000000000
        driver.findElement(By.id("fiscalid")).sendKeys("000000000");
        // 11 | click | id=phone |
        driver.findElement(By.id("phone")).click();
        // 12 | type | id=phone | 3297554175
        driver.findElement(By.id("phone")).sendKeys("3297554175");
        // 13 | click | id=address |
        driver.findElement(By.id("address")).click();
        // 14 | type | id=address | Via Carlo Didimi 9
        driver.findElement(By.id("address")).sendKeys("Via Carlo Didimi 9");
        js.executeScript("window.scrollBy(0,100)");
        // 15 | click | id=city |
        driver.findElement(By.id("city")).click();
        // 16 | type | id=city | Treia
        driver.findElement(By.id("city")).sendKeys("Treia");
        // 17 | click | id=province |
        driver.findElement(By.id("province")).click();
        // 18 | select | id=province | label=Macerata
        {
            WebElement dropdown = driver.findElement(By.id("province"));
            dropdown.findElement(By.xpath("//option[. = 'Macerata']")).click();
        }
        // 19 | click | id=postalCode |
        driver.findElement(By.id("postalCode")).click();
        // 20 | type | id=postalCode | 62010
        driver.findElement(By.id("postalCode")).sendKeys("62010");
        js.executeScript("window.scrollBy(0,100)");
        // 21 | click | id=password |
        driver.findElement(By.id("password")).click();
        // 22 | type | id=password | Leopato1
        driver.findElement(By.id("password")).sendKeys("Leopato1");
        // 23 | click | id=confpassword |
        driver.findElement(By.id("confpassword")).click();
        // 24 | type | id=confpassword | Leopato1
        driver.findElement(By.id("confpassword")).sendKeys("Leopato1");
        // 25 | click | css=.w-100 |
        js.executeScript("window.scrollBy(0,300)");
        driver.findElement(By.cssSelector(".w-100")).click();
    }

    @Test
    @Order(2)
    public void approvedProducer() throws InterruptedException {
        // Test name: ApprovedProducer
        // Step # | name | target | value
        // 1 | open | /home |
        driver.get("http://localhost:4200/home");
        // 2 | setWindowSize | 1512x858 |
        driver.manage().window().setSize(new Dimension(1512, 858));
        // 3 | click | id=btn-login |
        driver.findElement(By.id("btn-login")).click();
        // 4 | click | linkText=Accedi come Amministratore |
        driver.findElement(By.linkText("Accedi come Amministratore")).click();
        // 5 | click | id=email |
        driver.findElement(By.id("email")).click();
        // 6 | type | id=email | admin@admin.it
        driver.findElement(By.id("email")).sendKeys("admin@admin.it");
        // 7 | click | id=password |
        driver.findElement(By.id("password")).click();
        // 8 | type | id=password | admin123
        driver.findElement(By.id("password")).sendKeys("admin123");
        // 9 | click | css=.w-100:nth-child(8) |
        driver.findElement(By.cssSelector(".w-100:nth-child(8)")).click();
        Thread.sleep(1000);
        // 10 | click | linkText=2 Produttori |
        driver.findElement(By.linkText("1 Produttori")).click();
        // 11 | mouseOver | linkText=2 Produttori |
        {
            WebElement element = driver.findElement(By.linkText("1 Produttori"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        // 12 | mouseOut | linkText=2 Produttori |
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        // 13 | click | css=.list-group:nth-child(1) .limited-text |
        driver.findElement(By.cssSelector(".list-group:nth-child(1) .limited-text")).click();
        // 14 | click | css=.btnAccetta |
        driver.findElement(By.cssSelector(".btnAccetta")).click();
        // 15 | click | css=.container |
        driver.findElement(By.cssSelector(".container")).click();
        // 16 | click | id=btn-Logout |
        driver.findElement(By.id("btn-Logout")).click();
    }

    @Test
    @Order(3)
    public void loginProducer() throws InterruptedException {
        // Test name: LoginProducer
        // Step # | name | target | value
        // 1 | open | /home |
        driver.get("http://localhost:4200/home");
        // 2 | setWindowSize | 1492x837 |
        driver.manage().window().setSize(new Dimension(1492, 837));
        // 3 | click | css=.justify-content-lg-end |
        driver.findElement(By.cssSelector(".justify-content-lg-end")).click();
        // 4 | click | id=btn-login |
        driver.findElement(By.id("btn-login")).click();
        // 5 | mouseOver | id=btn-login |
        {
            WebElement element = driver.findElement(By.id("btn-login"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        // 6 | click | id=email |
        driver.findElement(By.id("email")).click();
        // 7 | type | id=email | leocurzi1409@gmail.com
        driver.findElement(By.id("email")).sendKeys("leocurzi@yopmail.com");
        // 8 | click | id=password |
        driver.findElement(By.id("password")).click();
        // 9 | type | id=password | Leopato1
        driver.findElement(By.id("password")).sendKeys("Leopato1");
        // 10 | sendKeys | id=password | ${KEY_ENTER}
        driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        // 11 | click | id=dati |
        driver.findElement(By.id("dati")).click();
        // 12 | click | id=other-img |
        driver.findElement(By.id("other-img")).click();
        // 13 | click | id=crea-newsletter |
        driver.findElement(By.id("crea-newsletter")).click();
        // 14 | click | id=prod-newsletterhistory |
        driver.findElement(By.id("prod-newsletterhistory")).click();
        // 15 | click | css=#side_nav > .header-box |
        driver.findElement(By.cssSelector("#side_nav > .header-box")).click();
        // 16 | click | id=dati |
        driver.findElement(By.id("dati")).click();

    }

    @Test
    @Order(4)
    public void modifyProducer() throws InterruptedException {
        // Test name: Modify Producer
        // Step # | name | target | value
        // 1 | open | /home |
        driver.get("http://localhost:4200/home");
        // 2 | setWindowSize | 1512x858 |
        driver.manage().window().setSize(new Dimension(1512, 858));
        // 3 | click | id=btn-login |
        driver.findElement(By.id("btn-login")).click();
        // 4 | mouseOver | id=btn-login |
        {
            WebElement element = driver.findElement(By.id("btn-login"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        // 5 | click | id=email |
        driver.findElement(By.id("email")).click();
        // 6 | type | id=email | leocurzi@yopmail.com
        driver.findElement(By.id("email")).sendKeys("leocurzi@yopmail.com");
        // 7 | click | id=password |
        driver.findElement(By.id("password")).click();
        // 8 | type | id=password | Leopato1
        driver.findElement(By.id("password")).sendKeys("Leopato1");
        // 9 | sendKeys | id=password | ${KEY_ENTER}
        driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        // 10 | click | css=.btn-Indietro-Modifica |
        driver.findElement(By.cssSelector(".btn-Indietro-Modifica")).click();
        // 11 | mouseOver | css=.btn-Indietro-Modifica |
        {
            WebElement element = driver.findElement(By.cssSelector(".btn-Indietro-Modifica"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        // 12 | click | id=inputProdotti |
        driver.findElement(By.id("inputProdotti")).click();
        // 13 | type | id=inputProdotti | pasta
        driver.findElement(By.id("inputProdotti")).sendKeys("pasta");
        // 14 | click | id=inputDescrizione |
        driver.findElement(By.id("inputDescrizione")).click();
        // 15 | type | id=inputDescrizione | Produttori dal 1999
        driver.findElement(By.id("inputDescrizione")).sendKeys("Produttori dal 1999");
        // 16 | click | id=inputLinkFB |
        driver.findElement(By.id("inputLinkFB")).click();
        // 17 | type | id=inputLinkFB | https://www.facebook.com/LaPastadiAldo
        driver.findElement(By.id("inputLinkFB")).sendKeys("https://www.facebook.com/LaPastadiAldo");
        // 18 | click | css=.btn-Applica |
        driver.findElement(By.cssSelector(".btn-Applica")).click();
    }

    @Test
    @Order(5)
    public void createUsers() {
        // Test name: CreateUsers
        // Step # | name | target | value
        // 1 | open | /home |
        driver.get("http://localhost:4200/home");
        // 2 | setWindowSize | 1512x858 |
        driver.manage().window().setSize(new Dimension(1512, 858));
        // 3 | click | id=email |
        driver.findElement(By.id("email")).click();
        // 4 | type | id=email | andreapalmieri@yopmail.com
        driver.findElement(By.id("email")).sendKeys(getEmail());
        // 5 | sendKeys | id=email | ${KEY_ENTER}
        driver.findElement(By.id("email")).sendKeys(Keys.ENTER);
        // 6 | click | css=.close-btn |
        driver.findElement(By.cssSelector(".close-btn")).click();
        // 7 | mouseDownAt | id=email | 240.234375,14.40625
        {
            WebElement element = driver.findElement(By.id("email"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).clickAndHold().perform();
        }
        // 8 | mouseMoveAt | css=.newsletter-text | -3.765625,37
        {
            WebElement element = driver.findElement(By.cssSelector(".newsletter-text"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        // 9 | mouseUpAt | css=.newsletter-text | -3.765625,37
        {
            WebElement element = driver.findElement(By.cssSelector(".newsletter-text"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).release().perform();
        }
        // 10 | click | css=.d-grid |
        driver.findElement(By.cssSelector(".d-grid")).click();
    }

    private String getEmail() {
        Faker faker = new Faker();
        String name = faker.name().firstName().toLowerCase() + faker.name().lastName().toLowerCase();
        return name + "@yopmail.com";
    }

    @Test
    @Order(6)
    public void createNewsletter() throws InterruptedException {
        // Test name: CreateNewsletter
        // Step # | name | target | value
        // 1 | open | /home |
        driver.get("http://localhost:4200/home");
        // 2 | setWindowSize | 1512x858 |
        driver.manage().window().setSize(new Dimension(1512, 858));
        // 3 | click | id=btn-login |
        driver.findElement(By.id("btn-login")).click();
        // 4 | click | id=email |
        driver.findElement(By.id("email")).click();
        // 5 | type | id=email | leocurzi@yopmail.com
        driver.findElement(By.id("email")).sendKeys("leocurzi@yopmail.com");
        // 6 | click | id=password |
        driver.findElement(By.id("password")).click();
        // 7 | type | id=password | Leopato1
        driver.findElement(By.id("password")).sendKeys("Leopato1");
        // 8 | sendKeys | id=password | ${KEY_ENTER}
        driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
        Thread.sleep(1000);
        // 9 | click | id=crea-newsletter |
        driver.findElement(By.id("crea-newsletter")).click();
        // 10 | click | css=.form-control:nth-child(2) |
        driver.findElement(By.cssSelector(".form-control:nth-child(2)")).click();
        // 11 | type | css=.form-control:nth-child(2) | Nuovo Spaghetto Aldo
        driver.findElement(By.cssSelector(".form-control:nth-child(2)")).sendKeys("Nuovo Spaghetto Aldo");
        // 12 | click | css=.form-control:nth-child(5) |
        driver.findElement(By.cssSelector(".form-control:nth-child(5)")).click();
        // 13 | type | css=.form-control:nth-child(5) | Offerta a 10€
        driver.findElement(By.cssSelector(".form-control:nth-child(5)")).sendKeys("Offerta a 10€");
        // 15 | click | id=btn-Logout |
        driver.findElement(By.id("btn-Logout")).click();
    }


    @Test
    @Order(7)
    public void deleteProducer() throws InterruptedException {
        // Test name: DeleteProducer
        // Step # | name | target | value
        // 1 | open | /home |
        driver.get("http://localhost:4200/home");
        // 2 | setWindowSize | 1512x858 |
        driver.manage().window().setSize(new Dimension(1512, 858));
        // 3 | click | id=btn-login |
        driver.findElement(By.id("btn-login")).click();
        // 4 | click | linkText=Accedi come Amministratore |
        driver.findElement(By.linkText("Accedi come Amministratore")).click();
        // 5 | click | id=email |
        driver.findElement(By.id("email")).click();
        // 6 | type | id=email | admin@admin.it
        driver.findElement(By.id("email")).sendKeys("admin@admin.it");
        // 7 | click | id=password |
        driver.findElement(By.id("password")).click();
        // 8 | type | id=password | admin123
        driver.findElement(By.id("password")).sendKeys("admin123");
        // 9 | click | css=.w-100:nth-child(8) |
        driver.findElement(By.cssSelector(".w-100:nth-child(8)")).click();
        Thread.sleep(1000);
        // 10 | click | linkText=1 Cronologia dei Produttori |
        driver.findElement(By.linkText("1 Cronologia dei Produttori")).click();
        // 11 | click | css=.Ripristina |
        driver.findElement(By.cssSelector(".Ripristina")).click();
        // 12 | click | linkText=1 Produttori |
        driver.findElement(By.linkText("1 Produttori")).click();
        Thread.sleep(1000);
        {
            WebElement element = driver.findElement(By.linkText("1 Produttori"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        // 12 | mouseOut | linkText=2 Produttori |
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        // 13 | click | css=.list-group:nth-child(1) .limited-text |
        driver.findElement(By.cssSelector(".list-group:nth-child(1) .limited-text")).click();
        // 14 | click | css=.btnRifiuta |
        driver.findElement(By.cssSelector(".btnRifiuta")).click();
        // 16 | click | linkText=1 Cronologia dei Produttori |
        driver.findElement(By.linkText("1 Cronologia dei Produttori")).click();
        Thread.sleep(1000);
        // 17 | click | css=.Elimina |
        driver.findElement(By.cssSelector(".Elimina")).click();
    }

}


