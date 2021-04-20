package com.kazurayam.ks.dataurl

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

// enum of MIME TYPEs maintained, see https://guava.dev/releases/17.0/api/docs/com/google/common/net/MediaType.html
import com.google.common.net.MediaType 

public class DataUriConverter {

	public static void main(String[] args) throws Exception {
		DataUriConverter instance = new DataUriConverter()
		instance.execute()
	}
	
	public DataUriConverter() {}
	
	public void execute() throws Exception {
		
		// create a demo image
		BufferedImage image = createDemoImage()
		
		// convert the image to a dataURL
		String dataURL = DataURL.toPng(image)
		
		String html = "<html><body><img src='" + dataURL + "'></body></html>";

		// write the HTML
		File f = new File("image.html");
		FileWriter fw = new FileWriter(f);
		fw.write(html);
		fw.flush();
		fw.close();

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
	
	static class DataURL {
		static String toPng(BufferedImage image) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			//System.out.println("baos.toByteArray() " + baos.toByteArray());
			//System.out.println("baos.toByteArray().length " + baos.toByteArray().length);
			String dataBody = DatatypeConverter.printBase64Binary(baos.toByteArray());
			String dataURL = "data:" + MediaType.PNG + ";base64," + dataBody;
			return dataURL
		}
	}
	
}
