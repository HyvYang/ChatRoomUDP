
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
		//设置窗口参数
		setTitle("ChatRoom");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 530, 485);
		
		//设置边框
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);
		setDefaultCloseOperation(3);
		setResizable(false);
		
		//消息窗口
		textField = new JTextPane();
		textField.setBounds(10, 10, 400, 350);
		contentPane.add(textField);
		
		//消息发送窗口
		TextSend = new JTextField();
		TextSend.setBounds(10, 380, 280, 60);
		contentPane.add(TextSend);
		
		//消息发送按钮
		SendButton=new JButton();
		SendButton.setBounds(300, 380, 110, 60);
		contentPane.add(SendButton);
		SendButton.setText("发送");
		SendButton.addActionListener(this);
	
		//菜单栏
		MenuBar = new JToolBar();
		MenuBar.setBounds(410, 10, 101, 600);
		contentPane.add(MenuBar);
		
		panel = new JPanel();
		MenuBar.add(panel);
		panel.setLayout(null);
		
		FileButton = new JButton("文件");
		FileButton.setBounds(5, 5, 75, 75);
		panel.add(FileButton);
		FileButton.addActionListener(this);
		
		VideoButton = new JButton("视频");
		VideoButton.setBounds(5, 105, 75, 75);
		panel.add(VideoButton);
		VideoButton.addActionListener(this);
		
		SetButton = new JButton("选项");
		SetButton.setBounds(5, 205, 75, 75);
		panel.add(SetButton);
		SetButton.addActionListener(this);
		
		CloseButton = new JButton("退出");
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
		String message=TextSend.getText(); //获取消息
		try {
			send.sendMessageData(message);
		} catch (IOException e) {
			System.out.println("发送错误"+e.getMessage());
		}
	}
	
	public void send_file(String add) {
		sendStart();
		send.sendFileData(add);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(SendButton)) { //发送消息
			System.out.println("点击发送");
			send_message();
		
		}else if(e.getSource().equals(FileButton)){//发送文件
			JFileChooser f_choose=new JFileChooser();
			f_choose.showOpenDialog(new JFrame());
			String add=null;
			add=f_choose.getSelectedFile().getAbsolutePath();
			if(add!=null) {
				System.out.println("文件路径："+add);
				send_file(add);}
		
		}else if(e.getSource().equals(VideoButton)) {//发送视频
			VideoUI video=new VideoUI();
		
		}else if(e.getSource().equals(CloseButton)) {//关闭
			System.exit(0);
		}
	}
	
	
	public static void main(String[] args) {
		ChatUI Start = new ChatUI();
	}

}
	

	
