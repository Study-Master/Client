package studymaster.socket;

import java.util.Map;
import java.util.HashMap;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

class ImgUtil {

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
        	boolean flag = javax.imageio.ImageIO.write(image, "png", out);  
        	return out.toByteArray();
		} catch (Exception e) {
			System.err.println("[err] (ImgUtil toByte) Error when toByte.");
			return null;
		}
	}

	public static javafx.scene.image.Image byteToImage(byte[] imgByte) throws Exception{
        java.io.InputStream in = new java.io.ByteArrayInputStream(imgByte);
        return createImage(javax.imageio.ImageIO.read(in));
	}
}

public class VideoSS extends WebSocketServer {

	@FXML protected ImageView imgView;

	private static VideoSS instance;
	private Map<String, WebSocket> clients;

	public static VideoSS getInstance() {
		if(instance == null) {
			String localhost = "0.0.0.0";
			int port = 8088;
			InetSocketAddress address = new InetSocketAddress(localhost, port);
			instance = new VideoSS(address);
		}
		return instance;
	}

	private VideoSS(InetSocketAddress address) {
        super(address);
        clients = new HashMap<String, WebSocket>();
        System.out.println("Start server on " + address.getAddress() + " port " + address.getPort());
    }

    public void setImgView(ImageView imgView) {
    	this.imgView = imgView;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("new connection to " + conn.getRemoteSocketAddress());

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {}

    @Override
    public void onMessage( WebSocket conn, ByteBuffer message ) {
    	System.out.println("Receive img: " + message.toString());
    	try {
    		javafx.scene.image.Image i = ImgUtil.byteToImage(message.array());
    		if(i==null)
    			System.out.println("fuck");
    		imgView.setImage(i);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occured on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }
}