import com.kazurayam.net.DataURL
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String name = "world"

String html = """<html>
  <body>
    <h1>Test Cases/main/openBrowser_HTML_as_data</h1>
    <p>Hello, ${name}!</p>
  </body>
</html>
"""

String encoded = URLEncoder.encode(html, 'utf-8')

// save the html code into a temporary file, obtain the URL of 'file:' scheme
// e.g. 'file:///var/folders/7m/lm7d6nx51kj0kbtnsskz6r3m0000gn/T/DataURL8225716410597530989.html'
String fileurl = DataURL.transfer("data:text/html; charset=utf-8,${encoded}")

// open the temporary file in browser
WebUI.openBrowser('')
WebUI.navigateToUrl(fileurl)
WebUI.delay(3)
//WebUI.closeBrowser()
