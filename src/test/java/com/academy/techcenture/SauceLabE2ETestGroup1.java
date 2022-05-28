package com.academy.techcenture;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;

public class SauceLabE2ETestGroup1 {

    private WebDriver driver;
    private Faker faker;
    WebDriverWait wait;
    private final String url = "https://www.saucedemo.com/";

    @BeforeMethod
    //Setting up, driver - faker - timeouts - ChromeDriver -wait
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        this.driver = new ChromeDriver();
        this.faker = new Faker();
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        this.driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
        wait = new WebDriverWait(this.driver, Duration.ofSeconds(15));
    }//end setUp

    @Test
    public void testSauceLabE2E() throws InterruptedException {

        // 1. Launch the Chrome browser
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        // 2. Go to https://www.saucedemo.com/
        driver.get(url);

        // 3. Verify the title of the page
        boolean title = driver.getTitle().equals("Swag Labs");
        Assert.assertTrue(title, "Title is not matching!");

        // 4. Verify the SwagLabs Logo and Bot image
        WebElement loginLogo = driver.findElement(By.className("login_logo"));
        Assert.assertTrue(loginLogo.isDisplayed(), "Login logo is unable!");

        WebElement loginBotPicture = driver.findElement(By.className("bot_column"));
        Assert.assertTrue(loginBotPicture.isDisplayed(), "Login bot picture is unable!");

        // 5. Use these credentials to login
        //   Username: “standard_user”.
        WebElement userName = driver.findElement(By.id("user-name"));
        userName.sendKeys("standard_user");
        //   Password: “secret_sauce”
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("secret_sauce");

        WebElement loginBtn = driver.findElement(By.id("login-button"));
        loginBtn.click();

        // 6. Verify title when user logs in
        title = driver.getTitle().equals("Swag Labs");
        Assert.assertTrue(title, "Title is not matching!");

        // 7. Verify Products title on top left
        WebElement titleProducts = driver.findElement(By.className("title"));
        Assert.assertEquals(titleProducts.getText(), "PRODUCTS");

        // 8. Click on Menu on Top left
        WebElement menuBtn = driver.findElement(By.id("react-burger-menu-btn"));
        menuBtn.click();

        // 9. Verify there are 4 links: All items, About, Logout and Reset App State
        List<WebElement> menuElements = driver.findElements(By.xpath("//a[@class='bm-item menu-item']"));
        String[] menuOptions = {"all items", "about", "logout", "reset app state"};
        for (int i = 0; i < menuElements.size(); i++) {
            boolean menuItemCorrect = menuElements.get(i).getText().trim().toLowerCase().equals(menuOptions[i]);
            Assert.assertTrue(menuItemCorrect, "Menu items did not matched! " + menuOptions[i]);
        }

        // 10. Click on about link (this will take the driver to another page)
        WebElement aboutMenuLink = driver.findElement(By.id("about_sidebar_link"));
        aboutMenuLink.click();

        // 11. Verify the title of the new tab is “Cross Browser Testing, Selenium Testing, Mobile Testing | Sauce Labs”
        title = driver.getTitle().trim().contains("Cross Browser Testing");
        Assert.assertTrue(title, "Title is not matching!");

        // 12. Come back to previous page
        driver.navigate().back();

        // 13. Close the menu panel
        WebElement closeMenu = driver.findElement(By.id("react-burger-cross-btn"));
        if (closeMenu.isDisplayed()) {
            closeMenu.click();
        }

        // 14. Verify there are 6 products
        List<WebElement> items = driver.findElements(By.xpath("//div[@class='inventory_item_name']"));
        Assert.assertEquals(items.size(), 6, "List size not matched!");

        //15. Click on filter on top right and select PRICE(LOW TO HIGH)
        WebElement filterBtn = driver.findElement(By.xpath("//select[@class='product_sort_container']"));
        new Select(filterBtn).selectByValue("lohi");

        // 16. Verify the price of first item is $7.99 and last one is $49.99
        List<WebElement> inventoryPrices = driver.findElements(By.className("inventory_item_price"));
        boolean firstPrice = inventoryPrices.get(0).getText().trim().equals("$7.99");
        boolean lastPrice = inventoryPrices.get(inventoryPrices.size() - 1).getText().trim().equals("$49.99");
        Assert.assertTrue(firstPrice && lastPrice, "First or last Price are not correct!");

        // 17. Click on the title of the first product
        WebElement firstProduct = driver.findElement(By.xpath("//div[contains(text(),'Sauce Labs Onesie')]"));
        firstProduct.click();

        // 18. Verify Title, description, price and add to cart button is displayed
        WebElement productTitle = driver.findElement(By.xpath("//div[@class='inventory_details_name large_size']"));
        WebElement productDescription = driver.findElement(By.xpath("//div[@class='inventory_details_desc large_size']"));
        WebElement productPrice = driver.findElement(By.xpath("//div[@class='inventory_details_price']"));
        boolean addToCartBtn = driver.findElement(By.id("add-to-cart-sauce-labs-onesie")).isDisplayed();
        Assert.assertTrue(addToCartBtn, "Chart icon not displayed!");
        Assert.assertTrue(productTitle.getText().equals("Sauce Labs Onesie"), "Title of product is not correct!");
        Assert.assertTrue(productDescription.getText().contains("Rib snap infant"), "Description is not correct!");


        // 19. Verify the price is $7.99
        WebElement price = driver.findElement(By.xpath("(//div[@class='inventory_details_price'])[1]"));
        String[] priceAr = price.getText().split("\\$");
        boolean price799 = priceAr[1].equals("7.99");
        Assert.assertTrue(price799, "Price is not matching!");

        // 20. Click on add to cart button
        WebElement clickBtn = driver.findElement(By.xpath("//div/button[contains(text(),'Add to cart')]"));
        clickBtn.click();

        // 21. Verify Add to cart button has changed to Remove on the button
        WebElement removeBtn = driver.findElement(By.id("remove-sauce-labs-onesie"));
        boolean remove = removeBtn.getText().equalsIgnoreCase("remove");
        Assert.assertTrue(remove, "Button not turned REMOVE!");


        // 22. Click on Cart icon on top right
        WebElement cartBtn = driver.findElement(By.className("shopping_cart_link"));
        cartBtn.click();


        // 23. Verify Your Cart header is there
        WebElement yourCartHeader = driver.findElement(By.className("title"));
        Assert.assertTrue(yourCartHeader.isDisplayed(), "Cart header unable!");

        // 23. Verify QTY is 1
        WebElement quantityOnCart = driver.findElement(By.xpath("//div[contains(text(),'1')]"));
        Assert.assertEquals(quantityOnCart.getText(), "1", "Quantity is not matching!");


        // 24. Verify Checkout button is enabled
        WebElement checkoutBtn = driver.findElement(By.id("checkout"));
        Assert.assertTrue(checkoutBtn.isDisplayed(), "Checkout button not displayed!");

        // 25. Click on Checkout button
        checkoutBtn.click();

        // 26. Fill out the form with random First name , Last name and zip code (use faker dependency)
        WebElement firstName = driver.findElement(By.id("first-name"));
        firstName.sendKeys(faker.name().firstName());

        WebElement lastName = driver.findElement(By.id("last-name"));
        lastName.sendKeys(faker.name().lastName());

        WebElement zipcode = driver.findElement(By.id("postal-code"));
        zipcode.sendKeys(faker.address().zipCode().substring(0, 5));


        // 27. Verify Continue button is enabled and click it
        WebElement continueBtn = driver.findElement(By.id("continue"));
        Assert.assertTrue(continueBtn.isEnabled(), "Continue button unable!");
        continueBtn.click();

        // 28. Verify CHECKOUT:OVERVIEW header is there
        WebElement checkoutHeader = driver.findElement(By.className("title"));
        Assert.assertTrue(checkoutHeader.isDisplayed(), "Checkout header not displayed!");

        // 29. Verify payment information is there and print the confirmation number on the console
        WebElement paymentInformation = driver.findElement(By.xpath("//div[contains(text(),'SauceCard')]"));
        Assert.assertTrue(paymentInformation.isDisplayed(), "Payment information is not displayed!");


        // 30. Verify shipping information is : “FREE PONY EXPRESS DELIVERY!”
        WebElement shippingInfo = driver.findElement(By.xpath("//div[contains(text(),'PONY')]"));
        Assert.assertTrue(shippingInfo.isDisplayed(), "Shipping information is not displayed!");

        // 31. Verify the total price:
        // read the item total and tax and add them up and verify if it’s matching the total price web element.
        WebElement itemTotalPrice = driver.findElement(By.className("summary_subtotal_label"));
        String[] itemTotalPriceArr = itemTotalPrice.getText().split("\\$");
        Assert.assertEquals(itemTotalPriceArr[1], "7.99", "Item price is not matching!");

        WebElement summaryTax = driver.findElement(By.className("summary_tax_label"));
        String[] summaryTaxArr = summaryTax.getText().split("\\$");
        double summaryTaxDouble = Double.parseDouble(summaryTaxArr[1]);

        WebElement summaryTotal = driver.findElement(By.className("summary_total_label"));
        String[] summaryTotalArr = summaryTotal.getText().split("\\$");
        double summaryTotalDouble = Double.parseDouble(summaryTotalArr[1]);

        double itemTotal = Double.parseDouble(itemTotalPriceArr[1]);
        double sumTaxAndTotal = (summaryTaxDouble + itemTotal);
        Assert.assertEquals(summaryTotalDouble, sumTaxAndTotal, "Price is not matching!");

        // 32. Verify finish button is enabled and click it
        WebElement finishButton = driver.findElement(By.id("finish"));
        Assert.assertTrue(finishButton.isDisplayed(), "Finish button is not displayed!");
        finishButton.click();

        // 33. Verify “Thank you for your order”, “You order has been dispatched, and will arrive just as fast as the pony can get there!“,
        // and image are displayed
        WebElement completeHeader = driver.findElement(By.xpath("//span[@class='title']"));
        Assert.assertTrue(completeHeader.isDisplayed(), "Complete header is not displayed!");

        WebElement completeText = driver.findElement(By.className("complete-text"));
        Assert.assertTrue(completeText.isDisplayed(), "Complete text is not displayed!");

        WebElement ponyExpressImage = driver.findElement(By.className("pony_express"));
        Assert.assertTrue(ponyExpressImage.isDisplayed(), "Image is not displayed!");

        // 34. Verify Back home is enabled and click it
        WebElement backHomeBtn = driver.findElement(By.id("back-to-products"));
        Assert.assertTrue(backHomeBtn.isDisplayed(), "Back home is not displayed!");
        backHomeBtn.click();

        // 35. Click on menu and click on logout link
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(1_000);
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("bm-item-list")));
        driver.findElement(By.id("logout_sidebar_link")).click();

        // 36. Wait for 3 seconds and close the driver
        Thread.sleep(3_000);
        driver.close();

    }//end of the Test

    @AfterMethod
    public void tearDown() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

}//end class
