package com.kazurayam.net.data

/**
 * This class is used by com.kazurayam.ks.util.DataURLEnablerKeyword class.
 *
 * @author kazurayam
 */
public class Handler extends URLStreamHandler {

	/**
	 * we need to implment URL.openConnection() method here.
	 * but will not use it in Katalon Studio.
	 * We only use URL.toString() method for data: URL.
	 */
	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		return null;
	}

}
