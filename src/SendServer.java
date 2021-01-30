import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SendServer{
	public static String local_ip="127.0.0.1";
	public static int local_port=6666;
	public static String targrt_ip="127.0.0.1";
	public static int targetport_message=7777;
	public static int targetport_file=8888;
	public static int targrtport_img=9999;
	
	SocketAddress localAdd;
	DatagramSocket sendSocket;
	
	SendServer(){
		creat_client();
	}
	
	//建立套接字
	public void creat_client() {
		try {
			localAdd=new InetSocketAddress(local_ip,local_port);
			sendSocket=new DatagramSocket(localAdd);	
		} catch (IOException e) {
			System.out.print("客户端建立失败 ");
		}
	}
	
	//传输消息
	public void sendMessageData(String message) throws IOException {
		SocketAddress MessageTargrt=new InetSocketAddress(targrt_ip,targetport_message);
		byte buffer[]=message.getBytes();
		DatagramPacket packet1=new DatagramPacket(buffer,buffer.length,MessageTargrt);
		sendSocket.send(packet1);
		System.out.println("发送成功");
		sendSocket.close();
	}
	
	
	public void sendFileData(String add) {
		File f=new File(add);
		BufferedInputStream fileStr;
		byte[] file_data=null;
		byte[] file_name=null;
		try {
			fileStr=new BufferedInputStream(new FileInputStream(f));
			//发送文件名及文件大小
			String filenameMsg=new String(f.getName().getBytes(),"utf-8")+" "+(int)f.length();
			System.out.println("发送文件为："+filenameMsg);
			
			file_data=new byte[(int) f.length()];
			file_name=filenameMsg.getBytes();
			fileStr.read(file_data);//读取数据内容
			
			if(file_data!=null) {
				//发送目标
				SocketAddress FileTo=new InetSocketAddress(targrt_ip,targetport_file);
				
				//发送文件名
				DatagramPacket name_packet=new DatagramPacket(file_name,file_name.length,FileTo);
				
				//发送文件内容
				DatagramPacket data_packet=new DatagramPacket(file_data,file_data.length,FileTo);
				
				sendSocket.send(name_packet);
				
				Thread.sleep(15);//休眠
				
				sendSocket.send(data_packet);
				
				System.out.println("发送成功");
				sendSocket.close();
			}
		} catch (FileNotFoundException e1) {
			System.out.println("文件不存在！"+e1.getMessage());
			e1.printStackTrace();
		} catch (IOException e) {
			System.out.println("读取错误！"+e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("线程休眠失败");
			e.printStackTrace();
		}	
	}
	
	public boolean sendImgData(BufferedImage img) {
		if(img!= null) {
			SocketAddress addimg=new InetSocketAddress(targrt_ip,targrtport_img);
			try {
				ByteArrayOutputStream bos=new ByteArrayOutputStream();
				ImageIO.write(img, "jpg", bos);
				byte[] img_bytes=bos.toByteArray();
				DatagramPacket packet1=new DatagramPacket(img_bytes,img_bytes.length,addimg);
				sendSocket.setSendBufferSize(img_bytes.length);
				sendSocket.send(packet1);
				System.out.println("图像发送成功");
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public void send_close() {
		sendSocket.close();
	}
}
