import com.kazurayam.ks.util.DataURLEnablerKeyword as dataurl
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// enable WebUI.openBrowser() and WebUI.navigateToURL() to accept 'data:' URI
dataurl.enable()

WebUI.openBrowser('')
WebUI.navigateToUrl("data:text/html,<h1>Hello, world!</h1>")

WebUI.delay(5)
WebUI.closeBrowser()
