package com.kazurayam.ks.dataurlsupport

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

// enum of MIME TYPEs maintained, see https://guava.dev/releases/17.0/api/docs/com/google/common/net/MediaType.html
import com.google.common.net.MediaType

public class UsageExample {

	public static void main(String[] args) throws Exception {
		UsageExample instance = new UsageExample()
		instance.execute()
	}

	public UsageExample() {}

	public void execute() throws Exception {
		// create a demo image
		BufferedImage image = createDemoImage()
		// convert the image to a dataURL
		DataURL dataURL = DataURLFactory.toImageDataURL(MediaType.PNG, image)

		// write the HTML which renders the dataURL of PNG
		/*
		String html = "<html><body><img src='" + dataURL.toString() + "'></body></html>"
		File f = new File("image.html")
		FileWriter fw = new FileWriter(f)
		fw.write(html)
		fw.flush()
		fw.close()
		 */
		File f = dataURL.toTempFile()
		
		// display the HTML
		Desktop.getDesktop().open(f);
	}

	BufferedImage createDemoImage() {
		int sz = 200;
		BufferedImage image = new BufferedImage(
				sz, sz, BufferedImage.TYPE_INT_ARGB);

		// paint the image..
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLUE);
		for (int ii = 0; ii < sz; ii += 5) {
			g.drawOval(ii, ii, sz - ii, sz - ii);
		}
		g.dispose();
		return image
	}

}
