import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.util.concurrent.*;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

public class VideoChat extends Thread implements ActionListener{
	VideoUI v;
	Webcam camera;
	SendServer send;
	SocketAddress add;
	DatagramSocket receive_server;
	
	public static String ip_add="127.0.0.1";
	public static int port=5475;
	public static int special_effects=0;
	final ExecutorService exec = Executors.newFixedThreadPool(1);  
	
	VideoChat(VideoUI v){
		this.v=v;
		create_client();
		send=new SendServer();
		camera=Webcam.getDefault();
		camera.setViewSize(WebcamResolution.VGA.getSize());
		camera.open();
	}
	
	private void create_client() {
		try {
			add=new InetSocketAddress(ip_add,port);
			receive_server=new DatagramSocket(add);
		} catch (IOException e) {
			System.out.print("客户端建立失败 ");
		}
	}
	
	public void run() {
		display();
	}
	
	public Webcam getCamera() {
		this.send.send_close();
		return this.camera;
	}
	
	 Callable<String> call = new Callable<String>() {
	        public String call() throws Exception {
	        		receive_img(v.getGraphics(),receive_server);
		            return "true";       	
	        }  
	    };  
	
	    public void display() {
		Graphics g=v.getGraphics();
		while(send.sendImgData(camera.getImage())) {//从摄像头获取的图片
			BufferedImage img=camera.getImage();
			if(img!=null) {
				BufferedImage img2=new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);
				switch(special_effects) {
				case 1:{
					g.drawImage(mirror(img), 0, 0,null);
					break;
				}
				case 2:{
					mon(img, img2);
					g.drawImage(img2, 0, 0,null);
					break;
				}
				case 3:{
					mosaic(img,img2);
					g.drawImage(img2, 0, 0,null);
					break;
				}
				default:{
					g.drawImage(img, 0, 0,null);
					break;
				}
				}
				Future<String> future = exec.submit(call);
				try {
					future.get(1,TimeUnit.MILLISECONDS);
				} catch (InterruptedException | ExecutionException | TimeoutException e) {
					continue;
				}
			}
		}
	}
	
	    private void receive_img(Graphics g,DatagramSocket receive_server) {
		try {
			if(receive_server.getSendBufferSize()!=0) {
				byte[] buffer=new byte[receive_server.getSendBufferSize()];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				receive_server.receive(packet);
			
				ByteArrayInputStream bin=new ByteArrayInputStream(buffer);
				BufferedImage recimg=ImageIO.read(bin);
				g.drawImage(recimg, 670, 0, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	    private BufferedImage mirror(BufferedImage img) {
		if(img!=null) {
			int width=img.getWidth();
			int height=img.getHeight();
			BufferedImage img2=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			Graphics g=img2.getGraphics();
			for(int i=0;i<width;i++) {
				for (int j=0;j<height;j++) {
					int rgb=img.getRGB(i, j);
					g.setColor(new Color(rgb));
					g.drawLine(width-i, j, width-i, j);
				}
			}
			return img2;
		}
		return null;
	}
	private void mon(BufferedImage img,BufferedImage img2) {
		if(img!=null) {
			int width=img.getWidth();
			int height=img.getHeight();
			Graphics g2=img2.getGraphics();
			
			for(int i=0;i<width;i++) {
				for(int j=0;j<height;j++) {
					int rgb_c=img.getRGB(i, j);
					Color color=new Color(rgb_c);
					int v = (color.getRed() * 77 + 
							color.getGreen() * 150 + 
							color.getBlue() * 100 + 128)>>8;//灰度处理（保留光线强度）
					g2.setColor(new Color(v));
					g2.drawLine(width-i, j, width-i, j);
				}
			}
		}
	}
	private void mosaic(BufferedImage img,BufferedImage img2) {
		if(img!=null) {
			int width=img.getWidth();
			int height=img.getHeight();
			Graphics g2=img2.getGraphics();
			//像素点处理
			for(int i=0;i<width;i+=20) {
				for(int j=0;j<height;j+=20) {
					Color c=new Color(img.getRGB(i, j));
					g2.setColor(c);
					g2.fillRect(i, j, 20, 20);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button=(JButton) e.getSource();
		switch (button.getText()) {
		case "马赛克":{
			special_effects=3;
			break;
		}
		case "镜像":{
			special_effects=1;
			break;
		}
		case "单色":{
			special_effects=2;
			break;
		}
		default:{
			break;
		}
		}
	}
}

