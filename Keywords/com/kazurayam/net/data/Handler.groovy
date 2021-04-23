package com.kazurayam.net.data

/**
 * This class is used by com.kazurayam.ks.util.DataURLEnablerKeyword class.
 *
 * @author kazurayam
 */
public class Handler extends URLStreamHandler {

	/**
	 * we need to implment openConnection() method here in order to make this Handler class instanciate-able.
	 *
	 * But we will not use URL.openConnection() for data: URL in Katalon Studio.
	 * We only use URL.toString() method for data: URL.
	 */
	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		return null;
	}

}
