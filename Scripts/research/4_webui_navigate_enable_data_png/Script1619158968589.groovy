import com.kazurayam.ks.util.DataURLEnablerKeyword as dataurl
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// create a "data:image/png;base64,xxxxxxxxxxxxxxx" for demo
String pngdataurl = dataurl.demoImage()
println pngdataurl

// enable WebUI.openBrowser() and WebUI.navigateToURL() to accept 'data:' URI
dataurl.enable()

WebUI.openBrowser('')
WebUI.navigateToUrl(pngdataurl)

WebUI.delay(5)
WebUI.closeBrowser()
