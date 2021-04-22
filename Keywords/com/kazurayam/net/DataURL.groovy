package com.kazurayam.net

import java.awt.*;
import java.awt.image.BufferedImage
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.imageio.ImageIO;

import com.google.common.net.MediaType

/**
 * An implementation of Data URL
 * 
 * https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs
 * 
 * @author kazurayam
 */
public class DataURL {

	private MediaType mediaType
	private Boolean isBase64encoded = false
	private String data

	// short-hand method for test scripts  ----------------------------
	/**
	 * Accepts a String argument in "data:" format URL, transfer the data portion into a temporary file,
	 * and returns a "file:" format URL that points to the temporary file.
	 * The temporary file will be automatically created and swept out.
	 *
	 * @param str e.g, "data:text/html; charset=utf-8,<div>Hello, world!</div>"
	 * @return e.g "file:/var/folders/7m/lm7d6nx51kj0kbtnsskz6r3m0000gn/T/DataURL1608147919965481881.html"
	 */
	static String transfer(String str) {
		DataURL dataURL = DataURL.parse(str)
		return dataURL.toTempFileURLString()
	}


	// factory methods ------------------------------------------------

	/**
	 * parse a string in the format of "data:<mediatype>[;base64],<data>" 
	 * where 
	 * - <mediatype> can contain optional charset.
	 * - <data> is URL-encoded
	 * 
	 * A few examples quoted from https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs
	 * 
	 * <code>data:,Hello%2C%20World!</code>
	 * The text/plain data Hello, World!. Note how the comma is percent-encoded as %2C, and the space character as %20.
	 * 
	 * <code>data:text/plain;base64,SGVsbG8sIFdvcmxkIQ==</code>
	 * 
	 * base64-encoded version of the above
	 * 
	 * <code>data:text/html,%3Ch1%3EHello%2C%20World!%3C%2Fh1%3E</code>
	 * 
	 * An HTML document with <h1>Hello, World!</h1>
	 * 
	 * <code>data:text/html,<script>alert('hi');</script></code>
	 * 
	 * An HTML document that executes a JavaScript alert. Note that the closing script tag is required.
	 * 
	 * @param dataurl
	 */
	private static final String pattern = "^data:\\s*(([\\w\\.\\+\\-/\\*]+)?\\s*(;\\s*charset=(utf-8|US-ASCII))?)?(;\\s*(base64))?\\s*,(.+)\$"

	static DataURL parse(String dataurl) {
		Objects.requireNonNull(dataurl)
		Pattern p = Pattern.compile(pattern)
		Matcher m = p.matcher(dataurl)
		if (m.matches()) {
			MediaType mt = MediaType.PLAIN_TEXT_UTF_8
			if (m.group(2) != null) {
				mt = MediaType.parse(m.group(1))
			}
			String base64encode = m.group(6)
			String d = URLDecoder.decode(m.group(7), 'utf-8')
			return new DataURL(mt, ('base64' == base64encode), d)
		} else {
			throw new IllegalArgumentException("input string \'${dataurl}\' does not match regex \'${p.toString()}\'")
		}
	}

	/**
	 * 
	 * @param mediaType
	 * @param image
	 * @return
	 */
	static DataURL toImageDataURL(MediaType mediaType, BufferedImage image) {
		Objects.requireNonNull(mediaType)
		Objects.requireNonNull(image)
		if (mediaType.type != 'image') {
			throw new IllegalArgumentException("mediaType: ${mediaType} is not an image")
		}
		if (mediaType == MediaType.PNG ||
		mediaType == MediaType.JPEG ||
		mediaType == MediaType.GIF) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream()
			ImageIO.write(image, mediaType.subtype, baos)
			// byte[] -> base64 encoded string
			String base64encoded = Base64.getEncoder().encodeToString(baos.toByteArray())
			// base64 encoded string -> URLEncoded strinng
			String urlEncoded = URLEncoder.encode(base64encoded, 'utf-8')
			DataURL dataURL = new DataURL(mediaType, true, urlEncoded)
			return dataURL
		} else {
			throw new IllegalArgumentException("mediaType: ${mediaType} is not supported")
		}
	}


	// methods to transfer this `data:` URL to a `file:` URL ----------

	/**
	 *
	 * @return e.g. 'file:/var/folders/7m/lm7d6nx51kj0kbtnsskz6r3m0000gn/T/DataURL1608147919965481881.html' as an instance of String
	 */
	String toTempFileURLString() {
		return toTempFileURL().toExternalForm()
	}

	/**
	 *
	 * @return e.g. 'file:/var/folders/7m/lm7d6nx51kj0kbtnsskz6r3m0000gn/T/DataURL1608147919965481881.html' as an instance of URL
	 */
	URL toTempFileURL() {
		return toTempFile().toURI().toURL()
	}

	/**
	 *
	 * @return e.g 'file:/var/folders/7m/lm7d6nx51kj0kbtnsskz6r3m0000gn/T/DataURL1608147919965481881.html' as an instance of File
	 */
	File toTempFile() {
		if (! FileExtensions.containsKey(this.mediaType)) {
			throw new IllegalStateException("mediaType \'${this.mediaType.toString()}\' is not supported by FileExtensions")
		}
		String prefix = 'DataURL'
		String suffix = '.' + FileExtensions.get(this.mediaType).getExt()
		Path tempFile = Files.createTempFile(prefix, suffix)
		Files.write(tempFile, this.getDataBytes())
		return tempFile.toFile()
	}


	// constructor ----------------------------------------------------

	/**
	 * sole constructor
	 * 
	 * @param mediaType
	 * @param isBase64encoded
	 * @param data
	 */
	DataURL(MediaType mediaType, Boolean isBase64encoded, String data) {
		Objects.requireNonNull(mediaType)
		Objects.requireNonNull(data)
		this.mediaType = mediaType
		this.isBase64encoded = isBase64encoded
		this.data = data
	}


	// accessors ------------------------------------------------------

	MediaType getMediaType() {
		return mediaType
	}

	Boolean isBase64encoded() {
		return isBase64encoded
	}

	String getData() {
		return data
	}

	byte[] getDataBytes() {
		if (this.isBase64encoded()) {
			return Base64.getDecoder().decode(this.getData())
		} else {
			if (this.getMediaType().charset().isPresent()) {
				return this.getData().getBytes(this.getMediaType().charset().get())
			} else {
				return this.getData().getBytes(StandardCharsets.UTF_8)
			}
		}
	}


	// overriding Object fundamental methods --------------------------
	@Override
	String toString() {
		StringBuilder sb = new StringBuilder()
		sb.append("data:")
		sb.append(this.getMediaType().toString())
		if (this.isBase64encoded()) {
			sb.append(";base64")
		}
		sb.append(",")
		sb.append(this.getData())
		return sb.toString()
	}

	@Override
	boolean equals(Object o) {
		if (o == null)
			return false
		if (this.getClass() != o.getClass())
			return false
		DataURL other = (DataURL)o
		return Objects.equals(mediaType, other.mediaType) &&
				Objects.equals(isBase64encoded, other.isBase64encoded) &&
				Objects.equals(data, other.data)
	}

	@Override
	int hashCode() {
		return Objects.hash(mediaType, isBase64encoded, data)
	}


	/**
	 * Demonstration how to use DataURL class.
	 * 
	 * I learned a code shared at
	 * https://stackoverflow.com/questions/14996746/data-uri-how-to-create-them-in-java
	 * by Andrew Thompson. Thanks, Andrew.
	 * 
	 * @throws Exception
	 */
	static void main(String[] args) throws Exception {
		// create a demo image
		BufferedImage image = createDemoImage()

		// convert the image to a dataURL
		DataURL dataURL = DataURL.toImageDataURL(MediaType.PNG, image)

		// write the HTML which renders the dataURL of PNG
		String html = "<html><body><img src='" + dataURL.toString() + "'></body></html>"

		// write the HTML to a file
		File f = File.createTempFile('DesktopDemo', '.html')
		FileWriter fw = new FileWriter(f)
		fw.write(html)
		fw.flush()
		fw.close()

		// display the HTML
		Desktop.getDesktop().open(f);
	}

	static BufferedImage createDemoImage() {
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
