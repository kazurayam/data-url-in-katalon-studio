import com.kazurayam.ks.dataurlsupport.DataURL
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

String html = '''
<html>
  <body>
    <h1>Test Cases/main/openBrowser_HTML_as_data</h1>
    <p>Hello, world!</p>
  </body>
</html>
'''

WebUI.openBrowser('')
WebUI.navigateToUrl(DataURL.transfer("data:text/html; charset=utf-8,${html.replaceAll('\n','')}"))
WebUI.delay(3)
WebUI.closeBrowser()