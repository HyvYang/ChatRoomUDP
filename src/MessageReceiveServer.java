import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


public class MessageReceiveServer extends Thread{
	SocketAddress add;
	DatagramSocket recevie;
	ArrayList<String> messageList;
	ChatUI mychatroom;	

	public static String ip="127.0.0.1";//本地ip地址
	public static int port=7777;//本地接收端口号
	
	MessageReceiveServer(ChatUI chatroom){
		this.creat_client();
		mychatroom=chatroom;
		messageList=new ArrayList<String>();
	}
	
	public void creat_client() {
		try {
			add=new InetSocketAddress(ip,port);
			recevie=new DatagramSocket(add);
		} catch (IOException e) {
			System.out.print("客户端建立失败 ");
		}
	}
		
	public void run() {
		receive_data();
	}
	
	
	public void receive_data(){
		 while(true){ 
			 byte[] buffer  = new byte[100];      
			 //创建接收数据包对象
			 DatagramPacket packet = new DatagramPacket(buffer, buffer.length);        
			 try {
				recevie.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String msg;
			try {
				msg = new String(packet.getData(),"GBK").trim();//转换接收到的数据为字符串        
				messageList.add(msg); //添加到消息列表
				String m="";
				for(String message:messageList) {
					m += packet.getAddress().toString()+"说:"+"\n"+message+"\n";
					mychatroom.textField.setText(m);//在消息列表中显示
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		 }
	}
}
