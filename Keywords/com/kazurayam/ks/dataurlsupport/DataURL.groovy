package com.kazurayam.ks.dataurlsupport

import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Matcher
import java.util.regex.Pattern

import com.google.common.net.MediaType
import java.nio.charset.StandardCharsets

public class DataURL {

	private final String scheme = 'data'
	private MediaType mediaType = null
	private Boolean isBase64encoded = false
	private String data = null

	private static final String pattern = "^data:\\s*([\\s\\w\\.\\+\\-/\\*]+\\s*(;\\s*charset=utf-8)?)(;\\s*(base64))?\\s*,(.+)\$"

	/**
	 * parse a string in the format of "data:<mediatype>[;base64],<data>" where <mediatype> can contain optional charset. E.g "text/html; charset=utf-8"
	 * @param dataurl
	 */
	static DataURL parse(String str) {
		Objects.requireNonNull(str)
		Pattern p = Pattern.compile(pattern)
		Matcher m = p.matcher(str)
		if (m.matches()) {
			MediaType mt = MediaType.parse(m.group(1))
			String base64encode = m.group(4)
			String data = m.group(5)
			DataURL dataurl = new DataURL(mt, ('base64' == base64encode), data)
			return dataurl
		} else {
			throw new IllegalArgumentException("input string \'${str}\' does not match regex \'${p.toString()}\'")
		}
	}
	
	static String transit(String str) {
		DataURL dataURL = DataURL.parse(str)
		return dataURL.toTempFileURLString()
	}

	DataURL(MediaType mediaType, Boolean isBase64encoded, String data) {
		Objects.requireNonNull(mediaType)
		Objects.requireNonNull(data)
		this.mediaType = mediaType
		this.isBase64encoded = isBase64encoded
		this.data = data
	}

	MediaType getMediaType() {
		return mediaType
	}

	Boolean isBase64encoded() {
		return isBase64encoded
	}

	String getData() {
		return data
	}

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
	 * 
	 * @return
	 */
	File toTempFile() {
		String prefix = 'DataURL'
		String suffix = '.' + FileExtensions.get(this.mediaType).getExt()
		Path tempFile = Files.createTempFile(prefix, suffix)
		Files.write(tempFile, data.getBytes(StandardCharsets.UTF_8))
		return tempFile.toFile()
	}
	
	/**
	 * 
	 * @return
	 */
	URL toTempFileURL() {
		return toTempFile().toURI().toURL()
	}
	
	/**
	 * 
	 * @return e.g. 'file:/var/folders/7m/lm7d6nx51kj0kbtnsskz6r3m0000gn/T/DataURL1608147919965481881.html' on Mac
	 */
	String toTempFileURLString() {
		return toTempFileURL().toExternalForm()
	}
	
	
}
