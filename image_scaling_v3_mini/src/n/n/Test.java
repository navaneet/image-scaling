package n.n;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;

public class Test {
	static File f, desto;
	static BufferedImage temp;
	static ResampleOp op;
	static JPEGImageWriteParam param2;
	static java.util.Iterator<ImageWriter> its;
	static ImageWriter writer3;

	public static void main(String[] args) throws IOException {
         
		f = new File(
				"C:\\Users\\ad min\\Pictures\\20110213_jean-guihen-queyras-2.jpg");

		temp = Thumbnails.of(f).size(200, 200).outputFormat("JPEG")
				.antialiasing(Antialiasing.ON).keepAspectRatio(true)
				.asBufferedImage();
		op = new ResampleOp(temp.getWidth(), temp.getHeight());
		op.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Soft);
		temp = op.filter(temp, null);

		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

		// checks if image is jpeg or not
		if (getFormatName(f).equalsIgnoreCase("JPEG") == false) {
			System.out
					.println("file format is not jpeg so applying alternatives");
			// http://www.mkyong.com/java/convert-png-to-jpeg-image-file-in-java/
			// http://stackoverflow.com/questions/464825/converting-transparent-gif-png-to-jpeg-using-java
			BufferedImage bufferedImage = new BufferedImage(
					temp.getWidth(null), temp.getHeight(null),
					BufferedImage.TYPE_INT_RGB);
			bufferedImage.createGraphics().drawImage(temp, 0, 0,
					bufferedImage.getWidth(), bufferedImage.getHeight(),
					Color.WHITE, null);
			// Color.WHITE sets the background to white. You can use any other
			// color

			temp = bufferedImage;

		}

		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		// http://stackoverflow.com/questions/464825/converting-transparent-gif-png-to-jpeg-using-java
		param2 = new JPEGImageWriteParam(null);
		param2.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
		// param.setCompressionQuality((float) 0.85);
		param2.setCompressionQuality(1);// highest quality
		its = ImageIO.getImageWritersBySuffix("jpg");
		writer3 = its.next();
		desto = new File(
				"C:\\Users\\ad min\\Pictures\\thumbnail.jpg");
		desto.delete();
		desto.getParentFile().mkdirs();
		writer3.setOutput(new FileImageOutputStream(desto));
		writer3.write(null, new IIOImage(temp, null, null), param2);
		temp = null;
		writer3.dispose();

		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	}

	// Returns the format name of the image in the object 'o'.
	// 'o' can be either a File or InputStream object.
	// Returns null if the format is not known.
	private static String getFormatName(Object o) {
		try {
			// Create an image input stream on the image
			ImageInputStream iis = ImageIO.createImageInputStream(o);

			// Find all image readers that recognize the image format
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				// No readers found
				return null;
			}

			// Use the first reader
			ImageReader reader = (ImageReader) iter.next();

			// Close stream
			iis.close();

			// Return the format name
			return reader.getFormatName();
		} catch (IOException e) {
		}
		// The image could not be read
		return null;
	}
}
