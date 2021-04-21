package com.kazurayam.ks.dataurlsupport

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
	void test_parse() {
		MediaType mt = MediaType.parse('text/html; charset=utf-8')
		assertThat(mt.type, is('text'))
		assertThat(mt.subtype, is('html'))
		assertThat(mt.charset().isPresent(), is(true))
		assertThat(mt.charset().get(), is('utf-8'))
	}
}
