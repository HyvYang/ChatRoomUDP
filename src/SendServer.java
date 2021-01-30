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
	
	//�����׽���
	public void creat_client() {
		try {
			localAdd=new InetSocketAddress(local_ip,local_port);
			sendSocket=new DatagramSocket(localAdd);	
		} catch (IOException e) {
			System.out.print("�ͻ��˽���ʧ�� ");
		}
	}
	
	//������Ϣ
	public void sendMessageData(String message) throws IOException {
		SocketAddress MessageTargrt=new InetSocketAddress(targrt_ip,targetport_message);
		byte buffer[]=message.getBytes();
		DatagramPacket packet1=new DatagramPacket(buffer,buffer.length,MessageTargrt);
		sendSocket.send(packet1);
		System.out.println("���ͳɹ�");
		sendSocket.close();
	}
	
	
	public void sendFileData(String add) {
		File f=new File(add);
		BufferedInputStream fileStr;
		byte[] file_data=null;
		byte[] file_name=null;
		try {
			fileStr=new BufferedInputStream(new FileInputStream(f));
			//�����ļ������ļ���С
			String filenameMsg=new String(f.getName().getBytes(),"utf-8")+" "+(int)f.length();
			System.out.println("�����ļ�Ϊ��"+filenameMsg);
			
			file_data=new byte[(int) f.length()];
			file_name=filenameMsg.getBytes();
			fileStr.read(file_data);//��ȡ��������
			
			if(file_data!=null) {
				//����Ŀ��
				SocketAddress FileTo=new InetSocketAddress(targrt_ip,targetport_file);
				
				//�����ļ���
				DatagramPacket name_packet=new DatagramPacket(file_name,file_name.length,FileTo);
				
				//�����ļ�����
				DatagramPacket data_packet=new DatagramPacket(file_data,file_data.length,FileTo);
				
				sendSocket.send(name_packet);
				
				Thread.sleep(15);//����
				
				sendSocket.send(data_packet);
				
				System.out.println("���ͳɹ�");
				sendSocket.close();
			}
		} catch (FileNotFoundException e1) {
			System.out.println("�ļ������ڣ�"+e1.getMessage());
			e1.printStackTrace();
		} catch (IOException e) {
			System.out.println("��ȡ����"+e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("�߳�����ʧ��");
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
				System.out.println("ͼ���ͳɹ�");
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
