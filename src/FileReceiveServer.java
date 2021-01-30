import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import javax.swing.JDialog;

public class FileReceiveServer extends Thread{

	SocketAddress add;
	DatagramSocket receive_server;
	ChatUI mychatroom;

	public static String File_add="D:\\";//默认存储位置	
	public static String ip="127.0.0.1";
	public static int port=8888;
	
	
	FileReceiveServer(ChatUI c){
		this.creat_client();
		this.mychatroom=c;
	}
	
	public void creat_client() {
		try {
			add=new InetSocketAddress(ip,port);
			receive_server=new DatagramSocket(add);
		} catch (IOException e) {
			System.out.print("客户端建立失败 ");
		}
	}
	
	public void run() {
		receive_data();
	}
	
	public void receive_data(){
		 while(true){ 
			 byte[] buffer  = new byte[50];
			 DatagramPacket name_packet = new DatagramPacket(buffer, buffer.length);  
			 try {
				receive_server.receive(name_packet);//接收文件名和长度的数据包
				String file_name=switchPacket(buffer)[0];
				int file_len=Integer.parseInt(switchPacket(buffer)[1].trim());
				
				System.out.println("文件名："+file_name+" 长度："+file_len);
				
				byte[] file_data=new byte[file_len];//文件缓存
				DatagramPacket data_packet = new DatagramPacket(file_data, file_data.length);
				receive_server.receive(data_packet);

				File f=new File(File_add+"\\"+file_name);
				FileOutputStream newFile=new FileOutputStream(f);
				newFile.write(file_data);
				newFile.close();
				
				if(f.exists()) {
					JOptionPane.showMessageDialog(null,"成功接收！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
	}
	
	private String[] switchPacket(byte[] buffer){
		try {
			String nameTemp = new String(buffer,"GBK");
			return nameTemp.split(" ");//根据空格分成数组
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
