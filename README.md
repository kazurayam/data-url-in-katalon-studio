Enabling Data URL in Katalon Studio
========

- date APRIL,2021
- author kazurayam

This project develops and provides a Plugin for [Katalon Studio](https://www.katalon.com/katalon-studio/).

You can download the project's zip file at the [Releases](https://github.com/kazurayam/katalon-studio-data-url/releases/) page, unzip it, open it with Katalon Studio on your PC.

This project was developed using Katalon Studio version 7.9.1, but it is not KS-version-dependent, should work on any versions above 7.0.

# Problem to solve

In the Katalon Forum, there are a few posts that claims for Data URL support in Katalon Studio.

- https://forum.katalon.com/t/urls-supplied-to-katalon-apis-must-support-data-uris/12203/
- https://forum.katalon.com/t/katalon-does-not-support-data-urls-rfc2397/23672/

They want to write a Test Case script like this:

```
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

WebUI.openBrowser("data:text/html,<h1>Hello, world!</h1>")
```

Here the code fragment "`data:text/html,<h1>Hello, world!</h1>`" is called [Data URLs](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs) as described in [RFC2397](https://tools.ietf.org/html/rfc2397). 

They expect to see a view in browser like this:

![1_want_to_see_this](docs/images/1_want_to_see_this.png)

But when they execute the above test case script in Katalon Studio v7.9.1, they will see following result, which is obviously disappointing:

![2_actually_seen](docs/images/2_actually_seen.png)

Katalon Studio's `WebUI.openBrowser(String url)` and `WebUI.navigateToUrl(String url)` do not support Data URLs.

# Solution

As I described in a [post](https://forum.katalon.com/t/katalon-does-not-support-data-urls-rfc2397/23672/13), I studied the source code and found that Katalon Studio has a problem. 

I have found out a way to enable Katalon Studio to support Data URL. I have developed a plugin module which is available as a jar file named `katalon-studio-data-url-x.x.x.jar`.

# Description

## How to install the plugin

- visit [Releases](https://github.com/kazurayam/katalon-studio-data-url/releases/) page, download the most up-to-date version of `katalon-studio-data-urlo`
- create your Katalon Studio project, which has `Plugins` directory under the project directory.
- locate the downloaded jar file in the `Plugins` directory.
- close/reopen your project to let Katalon Studio acknowledge the jar.

You are done.

## How to write your Test Case

One line of `import` statement, and one line of `dataurl.enable()`. That's all. The WebUI keyword will be enabled for data URLs.

### WebUI.openBrowser() is enabled to open 'data:text/html'

[`Test Cases/research/3_webui_enabledTo_navigateTo_data_html`](Scripts/research/3_webui_enabledTo_navigateTo_data_html/Script1619174550245.groovy)
```
import com.kazurayam.ks.util.DataURLEnabler as dataurl
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// enable WebUI.openBrowser() and WebUI.navigateToURL() to accept 'data:' URI
dataurl.enable()

WebUI.openBrowser("data:text/html,<h1>Hello, world!</h1>")

WebUI.delay(5)
WebUI.closeBrowser()
```

### WebUI.navigateToUrl() enabled to open 'data:image/png'

[`Test Cases/research/4_webui_enabledTo_navigateTo_data_png`](Scripts/research/4_webui_enabledTo_navigateTo_data_png/Script1619174581290.groovy)
```
import com.kazurayam.ks.util.DataURLEnabler as dataurl
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// create a "data:image/png;base64,xxxxxxxxxxxxxxx" for demo
String pngdataurl = dataurl.demoImage()
//println pngdataurl

// enable WebUI.openBrowser() and WebUI.navigateToURL() to accept 'data:' URI
dataurl.enable()

WebUI.openBrowser('')
WebUI.navigateToUrl(pngdataurl)

WebUI.delay(5)
WebUI.closeBrowser()

```

When I ran this script, I got the follwing display result:

![4_demo_png.png](docs/images/4_demo_png.png)



## Research: Why Katalon Studio mis-interets a `data:` URL to be a `file:` URL?

You can read the source of Katalon Studio Keyword:

- [`WebUI.navigateToUrl`](https://github.com/katalon-studio/katalon-studio-testing-framework/blob/master/Include/scripts/groovy/com/kms/katalon/core/webui/keyword/builtin/NavigateToUrlKeyword.groovy)

In here you will find an interesting portion:
```
    public void navigateToUrl(String rawUrl, FailureHandling flowControl) throws StepFailedException {
        WebUIKeywordMain.runKeyword({
            ...
            URL url = PathUtil.getUrl(rawUrl, "http")
            ...
            WebDriver webDriver = DriverFactory.getWebDriver()
            webDriver.navigate().to(url.toString())
            ...
```
By research I found that:

When the `rawUrl` argument is given with value of `"data:text/html,<h1>Hello, world!</h1>"`, `PathUtil.getUrl(rawUrl, ...)` returns an instance of `java.net.URL` with String value of `file:///Users/username/projectdir/data:text/html,%3Ch1%3EHello,%20world!%3C/h1%3E`.

This means that `com.kms.katalon.core.util.internal.PathUtil` is mis-interpreting it. 
What `PathUtil.getUrl(String url, ...)` is doing?

- [`PathUtil`](https://github.com/katalon-studio/katalon-studio-testing-framework/blob/master/Include/scripts/groovy/com/kms/katalon/core/util/internal/PathUtil.java)

It is just constructing an instance of `java.net.URL` class:
```
    public static URL getUrl(String rawUrl, String defaultProtocol) throws MalformedURLException, URISyntaxException {
        URL url = null;
        try {
            url = new URL(rawUrl);
        } catch (MalformedURLException e) {
            ...
```

### Dialogue

Me: Does the default implementation of [`java.net.URL`](https://docs.oracle.com/javase/7/docs/api/java/net/URL.html#URL) support Data URL? Can I instanciate it with `new URL("data:text/html,Hello world!")`?

Guru: No, it doesn't. As the javadoc clearly states, the default implementation supports only http, https, ftp, file, and jar.

Me: Is it possible to let `java.net.URL` instanciated with with `data:` URL somehow?

Guru: Yes, it is possible. The [javadoc of the constructor of `java.net.URL`](https://docs.oracle.com/javase/7/docs/api/java/net/URL.html#URL(java.lang.String,%20java.lang.String,%20int,%20java.lang.String)) clearly describes how to.

## How the plugin is implemented

The plugin is implemented with just 2 `.groovy` files.

- [`com.kazurayam.ks.util.DataURLEnabler`](Keywords/com/kazurayam/ks/util/DataURLEnabler.groovy)
- [`com.kazurayam.net.data.Handler`](Keywords/com/kazurayam/net/data/Handler.groovy)

Please read the source to find out how it is implemented.
