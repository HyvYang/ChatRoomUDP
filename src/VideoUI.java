
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JMenuBar;

public class VideoUI extends JFrame {

	private JPanel contentPane;

	VideoUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setDefaultCloseOperation(2);
		setVisible(true);
		
		VideoChat dis=new VideoChat(this);
		
		
		
		JButton exitButton = new JButton("退出");
		exitButton.setBounds(440, 530, 70, 23);
		contentPane.add(exitButton);
		
		JButton  mirrorButton= new JButton("镜像");
		mirrorButton.setBounds(120, 490, 100, 30);
		mirrorButton.addActionListener(dis);
		contentPane.add(mirrorButton);
		
		JButton mosaicButton= new JButton("马赛克");
		mosaicButton.setBounds(240, 490, 100, 30);
		mosaicButton.addActionListener(dis);
		contentPane.add(mosaicButton);
		
		JButton monochromaticButton = new JButton("单色");
		monochromaticButton.setBounds(360, 490, 100, 30);
		monochromaticButton.addActionListener(dis);
		contentPane.add(monochromaticButton);
		
	    JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setBackground(Color.BLACK);
		lblNewLabel.setBounds(642, 10, 11, 445);
		contentPane.add(lblNewLabel);
		
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton button=(JButton)e.getSource();
				if(button!=null) {
					dis.getCamera().close();
					dis.stop();
					dispose();
				}
			}
		});
		dis.start();
	}
}

