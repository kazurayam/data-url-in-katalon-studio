package com.kazurayam.ks.dataurlsupport

public class FileExtension {

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