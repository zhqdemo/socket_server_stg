package u3dserver.manage;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;

import util.PropUtil;

import javax.swing.JScrollPane;
import javax.swing.DropMode;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.Box;
import java.awt.TextArea;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import u3dserver.parse.SocketParse;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.ScrollPane;

import javax.swing.JScrollBar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.awt.Font;
import java.awt.Scrollbar;

@SuppressWarnings("serial")
public class ManageClient extends JFrame implements SocketParse{
	private JTextField textField;
	private JTextArea textArea;
	private ManagerClientSocket client;
	private ScrollPane scrollPane ;
	public ManageClient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 500);
		setTitle("管理员管理界面");
		this.setVisible(true);
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		textField = new JTextField();
		textField.setFont(new Font("宋体", Font.PLAIN, 12));
		panel_1.add(textField);
		textField.setColumns(50);
		
		JButton btnNewButton = new JButton("提交");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.sendMsg(textField.getText());
			}
		});
		panel_1.add(btnNewButton);
		
		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel_3 = new JPanel();
		getContentPane().add(panel_3, BorderLayout.EAST);
		
		JPanel panel_4 = new JPanel();
		getContentPane().add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));
		
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setRows(3);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);		
		//panel_4.add(textArea);
		
		scrollPane = new ScrollPane();
		scrollPane.add(textArea);
		panel_4.add(scrollPane);	
		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("快速填充");
		menuBar.add(menu);
		//向快速填充里面，加入项。通过配置文件中配置的
		List<String> keys = PropUtil.getInstance().getKeyStartWith("quick.actoin.");
		for(String key:keys){
			String quick = PropUtil.getInstance().getValue(key);
			String [] quicks = quick.split("\\|") ;
			String title = quicks[0];
			String content = quicks[1];
			JMenuItem loginMenuItem = new JMenuItem(title);
			loginMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					textField.setText(content);
					//client.sendMsg(content);
				}
			});	
			menu.add(loginMenuItem);
		}		
		client = new ManagerClientSocket(this);
		client.start();
		
		/**
		 * 
		 */
		this.addComponentListener(new ComponentAdapter(){
		    @Override public void componentResized(ComponentEvent e){
		    	scrollPane.setSize(ManageClient.this.getWidth()-40, ManageClient.this.getHeight()-200);
		    	textArea.setSize(scrollPane.getWidth()-20, textArea.getHeight());
		    }
		});
	}
	public static void main(String [] a){
		new ManageClient();
		
	}
	@Override
	public void parse(String msg) {
		this.textArea.append(msg+"\n");
		scrollPane.setScrollPosition(0, textArea.getHeight());
		// TODO Auto-generated method stub
		
	}	
}