package com.kazurayam.ks.dataurlsupport

import java.awt.image.BufferedImage

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import com.google.common.net.MediaType

class Converter {

	static String toPng(BufferedImage image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		String dataBody = DatatypeConverter.printBase64Binary(baos.toByteArray());
		String dataURL = "data:" + MediaType.PNG.toString() + ";base64," + dataBody;
		return dataURL
	}
}
