package com.kazurayam.net

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.google.common.net.MediaType

@RunWith(JUnit4.class)

public class MediaTypeTest {

	@Test
	void test_toString() {
		assertThat(MediaType.ANY_TEXT_TYPE.toString(), 		is('text/*'))
		assertThat(MediaType.HTML_UTF_8.toString(), 		is('text/html; charset=utf-8'))
		assertThat(MediaType.PLAIN_TEXT_UTF_8.toString(),	is('text/plain; charset=utf-8'))
	}

	@Test
	void test_parse_html_withCharset() {
		MediaType mt = MediaType.parse('text/html; charset=utf-8')
		assertThat(mt.type, is('text'))
		assertThat(mt.subtype, is('html'))
		assertThat(mt.charset().isPresent(), is(true))
		assertThat(mt.charset().get().toString().toLowerCase(), is('utf-8'))
	}
	
	@Test
	void test_parse_text_noCharset() {
		MediaType mt = MediaType.parse("text/plain")
		assertThat(mt.type, is('text'))
		assertThat(mt.subtype, is('plain'))
		assertThat(mt.charset().isPresent(), is(false))
	}
}
