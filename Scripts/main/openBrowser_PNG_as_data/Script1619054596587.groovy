import java.awt.image.BufferedImage

import com.google.common.net.MediaType
import com.kazurayam.net.DataURL
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// create a demo image
BufferedImage image = DataURL.createDemoImage()

// convert the image to a dataURL
DataURL dataurl = DataURL.toImageDataURL(MediaType.PNG, image)

println dataurl

// save the image into a temporary file, obtain a URL with 'file:' scheme that points to the file
String fileurl = DataURL.transfer(dataurl.toString())

// open the image file in browser
WebUI.openBrowser('')
WebUI.navigateToUrl(fileurl)
WebUI.delay(3)
WebUI.closeBrowser()
