package com.kazurayam.ks.util

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4.class)
public class DataURLEnablerKeywordTest {

	@Test
	void test_smoke() {
		DataURLEnablerKeyword.enable()
		String spec = 'data:text/html;charset=utf-8,<h1>Hello, world!</h1>'
		URL url = new URL(spec)
		println "url is \'${url.toString()}\'"
		assertThat(url.toString(), is(spec))
	}
	
	@Test
	void test_imagePngDemo() {
		String spec = DataURLEnablerKeyword.demoImage()
		println spec
		assertThat(spec, startsWith('data:image/png;base64,'))
		assertThat(spec, containsString('iVBORw0'))
	}
}
