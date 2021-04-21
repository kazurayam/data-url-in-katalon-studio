package com.kazurayam.ks.dataurlsupport

import java.util.regex.Matcher
import java.util.regex.Pattern
import java.nio.file.Files
import com.google.common.net.MediaType
import java.nio.file.Path

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
		sb.append("{")
		sb.append('"mediatype":"')
		sb.append(this.getMediaType().toString())
		sb.append('"')
		sb.append(',')
		sb.append('"isBase64encoded":')
		sb.append(this.isBase64encoded())
		sb.append(',')
		sb.append('"data":"')
		sb.append(this.getData())
		sb.append('"')
		sb.append("}")
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
	URL toTempFile() {
		String prefix = null
		String suffix = '.tmp'
		Path tempFile = Files.createTempFile(prefix, suffix)
	}
}
