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

	public static String ip="127.0.0.1";//����ip��ַ
	public static int port=7777;//���ؽ��ն˿ں�
	
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
			System.out.print("�ͻ��˽���ʧ�� ");
		}
	}
		
	public void run() {
		receive_data();
	}
	
	
	public void receive_data(){
		 while(true){ 
			 byte[] buffer  = new byte[100];      
			 //�����������ݰ�����
			 DatagramPacket packet = new DatagramPacket(buffer, buffer.length);        
			 try {
				recevie.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String msg;
			try {
				msg = new String(packet.getData(),"GBK").trim();//ת�����յ�������Ϊ�ַ���        
				messageList.add(msg); //��ӵ���Ϣ�б�
				String m="";
				for(String message:messageList) {
					m += packet.getAddress().toString()+"˵:"+"\n"+message+"\n";
					mychatroom.textField.setText(m);//����Ϣ�б�����ʾ
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		 }
	}
}
