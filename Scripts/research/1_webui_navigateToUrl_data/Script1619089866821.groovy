import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

WebUI.openBrowser('')
WebUI.navigateToUrl("data:text/html,<h1>Hello, world!</h1>")

WebUI.delay(5)
WebUI.closeBrowser()
