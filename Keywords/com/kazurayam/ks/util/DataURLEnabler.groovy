package com.kazurayam.ks.util

import com.kms.katalon.core.annotation.Keyword
import java.awt.image.BufferedImage
import java.awt.*
import javax.imageio.ImageIO

/**
 * 
 * @author kazurayam
 */
public class DataURLEnabler {

	@Keyword
	static void enable() {
		// as described in the Javadoc of java.net.URL class at
		// https://docs.oracle.com/javase/7/docs/api/java/net/URL.html
		String propName = 'java.protocol.handler.pkgs'
		String prop = System.getProperty(propName)
		String previousProp = (prop == null) ? "" : prop + "|"
		String pkg = 'com.kazurayam.net'
		if ( ! previousProp.contains(pkg) ) {
			System.setProperty(propName, previousProp + pkg)
		}
	}


	/**
	 * for demo
	 * 
	 * I learned a code shared at
	 * https://stackoverflow.com/questions/14996746/data-uri-how-to-create-them-in-java
	 * by Andrew Thompson. Thanks, Andrew.
	 * 
	 * @param image
	 * @return
	 */
	static String demoImage() {
		BufferedImage image = demoImagePng()
		ByteArrayOutputStream baos = new ByteArrayOutputStream()
		ImageIO.write(image, 'png', baos)
		// byte[] -> base64 encoded string
		String base64encoded = Base64.getEncoder().encodeToString(baos.toByteArray())
		// base64 encoded string -> URLEncoded string
		String urlEncoded = URLEncoder.encode(base64encoded, 'utf-8')
		return "data:image/png;base64," + urlEncoded
	}

	/**
	 * for demo
	 * 
	 * @return
	 */
	static BufferedImage demoImagePng() {
		int sz = 200;
		BufferedImage image = new BufferedImage(
				sz, sz, BufferedImage.TYPE_INT_ARGB);

		// paint the image..
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLUE);
		for (int ii = 0; ii < sz; ii += 5) {
			g.drawOval(ii, ii, sz - ii, sz - ii);
		}
		g.dispose();
		return image
	}
}
