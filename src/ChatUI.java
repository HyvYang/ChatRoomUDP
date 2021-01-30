
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JFileChooser;

public class ChatUI extends JFrame implements ActionListener{	
	JPanel contentPane;
	JTextPane textField;
	JTextField TextSend;
    JButton SendButton;
	JToolBar MenuBar;
	JPanel panel;
	JButton FileButton,VideoButton,SetButton,CloseButton;
	
	MessageReceiveServer message_server;
	FileReceiveServer file_server;
	SendServer send;
	
	public ChatUI() {
		//���ô��ڲ���
		setTitle("ChatRoom");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 530, 485);
		
		//���ñ߿�
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);
		setDefaultCloseOperation(3);
		setResizable(false);
		
		//��Ϣ����
		textField = new JTextPane();
		textField.setBounds(10, 10, 400, 350);
		contentPane.add(textField);
		
		//��Ϣ���ʹ���
		TextSend = new JTextField();
		TextSend.setBounds(10, 380, 280, 60);
		contentPane.add(TextSend);
		
		//��Ϣ���Ͱ�ť
		SendButton=new JButton();
		SendButton.setBounds(300, 380, 110, 60);
		contentPane.add(SendButton);
		SendButton.setText("����");
		SendButton.addActionListener(this);
	
		//�˵���
		MenuBar = new JToolBar();
		MenuBar.setBounds(410, 10, 101, 600);
		contentPane.add(MenuBar);
		
		panel = new JPanel();
		MenuBar.add(panel);
		panel.setLayout(null);
		
		FileButton = new JButton("�ļ�");
		FileButton.setBounds(5, 5, 75, 75);
		panel.add(FileButton);
		FileButton.addActionListener(this);
		
		VideoButton = new JButton("��Ƶ");
		VideoButton.setBounds(5, 105, 75, 75);
		panel.add(VideoButton);
		VideoButton.addActionListener(this);
		
		SetButton = new JButton("ѡ��");
		SetButton.setBounds(5, 205, 75, 75);
		panel.add(SetButton);
		SetButton.addActionListener(this);
		
		CloseButton = new JButton("�˳�");
		CloseButton.setBounds(5, 305, 75, 75);
		panel.add(CloseButton);
		CloseButton.addActionListener(this);
				
		receiveStart();	
		
		this.setVisible(true);
		
	}
	
	
	public void receiveStart() {
		message_server=new MessageReceiveServer(this);
		message_server.start();
		
		file_server=new FileReceiveServer(this);
		file_server.start();
	}
	
	
	public void sendStart() {
		send=new SendServer();
	}
	
	public void send_message() {
		sendStart();
		String message=TextSend.getText(); //��ȡ��Ϣ
		try {
			send.sendMessageData(message);
		} catch (IOException e) {
			System.out.println("���ʹ���"+e.getMessage());
		}
	}
	
	public void send_file(String add) {
		sendStart();
		send.sendFileData(add);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(SendButton)) { //������Ϣ
			System.out.println("�������");
			send_message();
		
		}else if(e.getSource().equals(FileButton)){//�����ļ�
			JFileChooser f_choose=new JFileChooser();
			f_choose.showOpenDialog(new JFrame());
			String add=null;
			add=f_choose.getSelectedFile().getAbsolutePath();
			if(add!=null) {
				System.out.println("�ļ�·����"+add);
				send_file(add);}
		
		}else if(e.getSource().equals(VideoButton)) {//������Ƶ
			VideoUI video=new VideoUI();
		
		}else if(e.getSource().equals(CloseButton)) {//�ر�
			System.exit(0);
		}
	}
	
	
	public static void main(String[] args) {
		ChatUI Start = new ChatUI();
	}

}
	

	
