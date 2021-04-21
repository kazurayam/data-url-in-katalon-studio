package com.kazurayam.ks.dataurlsupport

import java.awt.image.BufferedImage

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import com.google.common.net.MediaType

class DataURLFactory {

	static DataURL toImageDataURL(MediaType mediaType, BufferedImage image) {
		Objects.requireNonNull(mediaType)
		Objects.requireNonNull(image)
		if (mediaType.type != 'image') {
			throw new IllegalArgumentException("mediaType: ${mediaType} is not an image")
		}
		if (mediaType == MediaType.PNG ||
		mediaType == MediaType.JPEG ||
		mediaType == MediaType.GIF) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, mediaType.subtype, baos);
			String dataBody = DatatypeConverter.printBase64Binary(baos.toByteArray());
			DataURL dataURL = DataURL.parse("data:" + mediaType.toString() + ";base64," + dataBody);
			return dataURL
		} else {
			throw new IllegalArgumentException("mediaType: ${mediaType} is not supported")
		}
	}
}
