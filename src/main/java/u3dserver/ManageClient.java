package u3dserver;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.DropMode;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class ManageClient extends JFrame{
	private JTextField textField;
	public ManageClient() {
		setTitle("管理员管理界面");
		this.setVisible(true);
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		textField = new JTextField();
		textField.setText("123123123");
		panel_1.add(textField);
		textField.setColumns(50);
		
		JButton btnNewButton = new JButton("提交");
		panel_1.add(btnNewButton);
		
		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.WEST);
		
		JPanel panel_3 = new JPanel();
		getContentPane().add(panel_3, BorderLayout.EAST);
		
		JPanel panel_4 = new JPanel();
		getContentPane().add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		panel_4.add(textArea);
		textArea.append("12312312312312312\n");
		new TextA(textArea).start();
	}
	public static void main(String [] a){
		new ManageClient();
		
	}
	
}
class TextA extends Thread{
	JTextArea textArea;
	public TextA(JTextArea textArea){
		this.textArea = textArea;
	}
	public void run(){
		for(;;){
			textArea.append("12312312312312312\n");
			System.out.println("123123123");
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
