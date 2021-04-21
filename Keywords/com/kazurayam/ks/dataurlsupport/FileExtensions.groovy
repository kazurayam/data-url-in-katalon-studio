package com.kazurayam.ks.dataurlsupport

import com.google.common.net.MediaType

public class FileExtensions {

	private static Map<MediaType, FileExtension> fileExtensions

	static {
		// https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
		fileExtensions.put(MediaType.HTML_UTF_8,		new FileExtension('html',	"HyperText Markup Language (HTML)"))
		fileExtensions.put(MediaType.JSON_UTF_8,		new FileExtension('json',	"JSON format"))
		fileExtensions.put(MediaType.PLAIN_TEXT_UTF_8,	new FileExtension('txt',	"Plain text"))
		fileExtensions.put(MediaType.PNG,				new FileExtension('png',	"Portable Network Graphics"))
		fileExtensions.put(MediaType.XML_UTF_8,			new FileExtension('xml',	"XML"))
		fileExtensions.put(MediaType.ZIP,				new FileExtension('zip',	"ZIP archive"))
	}

	static get(MediaType mediaType) {
		return fileExtensions.get(mediaType)
	}

	static Set<MediaType> supportedMediaTypes() {
		return fileExtensions.keySet()
	}
}
