package com.kazurayam.ks.util

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4.class)
public class DataURLEnablerTest {

	@Test
	void test_new_toString() {
		DataURLEnabler.enable()
		String spec = 'data:text/html;charset=utf-8,<h1>Hello, world!</h1>'
		URL url = new URL(spec)
		println "url is \'${url.toString()}\'"
		assertThat(url.toString(), is(spec))
	}

	@Test
	void test_demoImage() {
		String spec = DataURLEnabler.demoImage()
		println spec
		URL url = new URL(spec)
		assertThat(url.toString(), startsWith('data:image/png;base64,'))
		assertThat(url.toString(), containsString('iVBORw0'))
	}
}
