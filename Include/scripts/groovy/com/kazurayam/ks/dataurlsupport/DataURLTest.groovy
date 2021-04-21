package com.kazurayam.ks.dataurlsupport

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.google.common.net.MediaType

@RunWith(JUnit4.class)
class DataURLTest {

	@Test
	void test_parse_html_utf8() {
		String input = "data:text/html;charset=utf-8,<div>Hello, World!</div>"
		DataURL dataurl = DataURL.parse(input)
	    MediaType expectedMediatype = MediaType.parse('text/html; charset=utf-8')
		assertThat(dataurl.toString(), dataurl.getMediaType(), is(expectedMediatype))
		assertThat(dataurl.toString(), dataurl.getMediaType().charset().isPresent(), is(true))
		assertThat(dataurl.toString(), dataurl.getMediaType().charset().get().toString(), is('UTF-8'))
		assertThat(dataurl.toString(), dataurl.isBase64encoded(), is(false))
		String expectedData = '<div>Hello, World!</div>'
		assertThat(dataurl.toString(), dataurl.getData(), is(expectedData))
	}

	@Test
	void test_parse_text_plain_encoded() {
		String input = "data:text/plain;base64,SGVsbG8sIFdvcmxkIQ=="
		DataURL dataurl = DataURL.parse(input)
		MediaType expectedMediatype = MediaType.parse("text/plain")
		assertThat(dataurl.toString(), dataurl.getMediaType(), is(expectedMediatype))
		assertThat(dataurl.toString(), dataurl.getMediaType().charset().isPresent(), is(false))
		assertThat(dataurl.toString(), dataurl.isBase64encoded(), is(true))
		String expectedData = 'SGVsbG8sIFdvcmxkIQ=='
		assertThat(dataurl.toString(), dataurl.getData(), is(expectedData))
	}

	@Test
	void test_toString() {
		String input = "data:text/html;charset=utf-8,<div>Hello, World!</div>"
		DataURL dataurl = DataURL.parse(input)
		//println dataurl.toString()
		assertThat(dataurl.toString(), is('{\"mediatype\":\"text/html; charset=utf-8\",\"isBase64encoded\":false,\"data\":\"<div>Hello, World!</div>\"}'))
	}

	@Test
	void test_equals() {
		String inputA = "data:text/html;charset=utf-8,<div>Hello, World!</div>"
		DataURL dataurlA1 = DataURL.parse(inputA)
		DataURL dataurlA2 = DataURL.parse(inputA)
		assertThat(dataurlA2, is(dataurlA1))
		//
		String inputB = "data:text/plain;base64,SGVsbG8sIFdvcmxkIQ=="
		DataURL dataurlB1 = DataURL.parse(inputB)
		DataURL dataurlB2 = DataURL.parse(inputB)
		assertThat(dataurlB2, is(dataurlB1))
		//
		assertThat(dataurlA1, is(not(dataurlB1)))
	}

	@Test
	void test_hashCode() {
		String inputA = "data:text/html;charset=utf-8,<div>Hello, World!</div>"
		DataURL dataurlA1 = DataURL.parse(inputA)
		DataURL dataurlA2 = DataURL.parse(inputA)
		assertThat(dataurlA2.hashCode(), is(dataurlA1.hashCode()))
		//
		String inputB = "data:text/plain;base64,SGVsbG8sIFdvcmxkIQ=="
		DataURL dataurlB1 = DataURL.parse(inputB)
		DataURL dataurlB2 = DataURL.parse(inputB)
		assertThat(dataurlB2.hashCode(), is(dataurlB1.hashCode()))
		//
		assertThat(dataurlA1.hashCode(), is(not(dataurlB1.hashCode())))
	}
}
