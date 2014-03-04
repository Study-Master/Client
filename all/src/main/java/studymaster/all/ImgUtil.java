package studymaster.all;

public class ImgUtil {

	public static javafx.scene.image.Image createImage(java.awt.Image image) {
		try {
			if (!(image instanceof java.awt.image.RenderedImage)) {
				java.awt.image.BufferedImage bufferedImage = new java.awt.image.BufferedImage(image.getWidth(null), image.getHeight(null), java.awt.image.BufferedImage.TYPE_INT_ARGB);
				java.awt.Graphics g = bufferedImage.createGraphics();
				g.drawImage(image, 0, 0, null);
				g.dispose();

				image = bufferedImage;
			}

			java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
			javax.imageio.ImageIO.write((java.awt.image.RenderedImage) image, "png", out);
			out.flush();
			java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(out.toByteArray());
			return new javafx.scene.image.Image(in);
		} catch (java.io.IOException e) {
			System.err.println("[err] (ImgUtil createImage) Error when createImage.");
			return null;
		}
	}

	public static byte[] toByte(java.awt.image.BufferedImage image) {
		try {
			java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();  
        	boolean flag = javax.imageio.ImageIO.write(image, "gif", out);  
        	return out.toByteArray();
		} catch (Exception e) {
			System.err.println("[err] (ImgUtil toByte) Error when toByte.");
			return null;
		}
	}

	public static javafx.scene.image.Image byteToImage(byte[] imgByte) {
		try {
        	java.io.InputStream in = new java.io.ByteArrayInputStream(imgByte);
        	return createImage(javax.imageio.ImageIO.read(in));
    	} catch (Exception e) {
    		
    		return null;
    	}
	}
}