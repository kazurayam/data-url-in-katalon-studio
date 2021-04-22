import java.awt.image.BufferedImage

import com.google.common.net.MediaType
import com.kazurayam.ks.dataurlsupport.DataURL
import com.kazurayam.ks.dataurlsupport.DataURLFactory
import com.kazurayam.ks.dataurlsupport.UsageExample
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// create a demo image
BufferedImage image = UsageExample.createDemoImage()

// convert the image to a dataURL
DataURL dataURL = DataURLFactory.toImageDataURL(MediaType.PNG, image)

// save the image into a temporary file, obtain a file: URL that points thee file
String fileURL = dataURL.transit()

WebUI.openBrowser('')
WebUI.navigateToUrl(fileURL)
WebUI.delay(3)
WebUI.closeBrowser()