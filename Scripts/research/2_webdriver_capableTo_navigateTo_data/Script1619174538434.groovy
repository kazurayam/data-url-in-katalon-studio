import org.openqa.selenium.WebDriver

import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

WebUI.openBrowser('')
WebDriver driver = DriverFactory.getWebDriver()
driver.navigate().to("data:text/html,<h1>Hello, world!</h1>")

Thread.sleep(3000)
driver.quit()
