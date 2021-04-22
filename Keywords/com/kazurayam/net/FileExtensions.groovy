package com.kazurayam.net

import com.google.common.net.MediaType

public class FileExtensions {

	private static Map<MediaType, FileExtension> m = new HashMap()

	/*
	 * refer to https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
	 * for the list of well known Media types
	 */
	static {
		m.put(MediaType.CSV_UTF_8,			new FileExtension('csv',	"CSV"))
		m.put(MediaType.parse('text/csv'),	new FileExtension('csv',	"CSV"))
		m.put(MediaType.HTML_UTF_8,			new FileExtension('html',	"HyperText Markup Language (HTML), UTF-8"))
		m.put(MediaType.parse('text/html'),	new FileExtension('html',	"HyperText Markup Language (HTML)"))
		m.put(MediaType.JPEG,				new FileExtension('jpg',	"JPEG image"))
		m.put(MediaType.JSON_UTF_8,			new FileExtension('json',	"JSON format"))
		m.put(MediaType.parse('application/json'),	new FileExtension('json',	"JSON format"))
		m.put(MediaType.PDF,				new FileExtension('pdf',	"PDF"))
		m.put(MediaType.PNG,				new FileExtension('png',	"Portable Network Graphics"))
		m.put(MediaType.PLAIN_TEXT_UTF_8,	new FileExtension('txt',	"Plain text, UTF-8"))
		m.put(MediaType.parse('text/plain'),new FileExtension('txt',	"Plain text"))
		m.put(MediaType.MICROSOFT_EXCEL,	new FileExtension('xlsx',	"Microsoft Excel spreadsheets"))
		m.put(MediaType.XML_UTF_8,			new FileExtension('xml',	"XML"))
		m.put(MediaType.parse('application/xml'),	new FileExtension('xml',	'XML'))
		m.put(MediaType.parse('text/xml'),	new FileExtension('xml',	'XML'))
	}

	static Boolean containsKey(MediaType mediaType) {
		return m.containsKey(mediaType)
	}

	static FileExtension get(MediaType mediaType) {
		return m.get(mediaType)
	}

	static Set<MediaType> supportedMediaTypes() {
		return m.keySet()
	}



	/**
	 * a HTML file "index.html" has a FileExtension 'html' (no . included)
	 * a PNG file "image.png" has a FileExtension 'png'
	 *
	 * @author kazurayam
	 */
	static class FileExtension {

		private String ext
		private String description

		FileExtension(String ext, String description) {
			this.ext = ext
			this.description = description
		}

		String getExt() {
			return this.ext
		}

		String getDescriptionn() {
			return this.description
		}
	}
}
