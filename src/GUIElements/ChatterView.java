package GUIElements;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.border.TitledBorder;

import jms.TextProcesser;

/**
 * @author Ari Ayvazyan
 * This is the main frame, the user has the ability to watch received and to send messages.
 */
public class ChatterView extends JFrame {

	/** The status content pane. */
	private JPanel contentPane;
	
	/** The input text field. */
	private JTextField textField;
	
	/** The output list. */
	private JList list;
	
	/** contains all elements to show to the user. */
	private Vector<String> vec=new Vector<String>();
	/** processes the Text */
	private TextProcesser tp;
	
	/**
	 * Create the frame.
	 */
	public ChatterView(TextProcesser tpi) {
		setTitle("JMS Chatter");
		this.tp=tpi;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				 if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 processUserText();
				 }
			}
		});
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				processUserText();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSend)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSend)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		
		list = new JList();
		list.setSelectionBackground(Color.LIGHT_GRAY);
		scrollPane.setViewportView(list);
		contentPane.setLayout(gl_contentPane);
	}
	
	/**
	 * Adds a list entry.
	 *
	 * @param entry the entry to add
	 */
	synchronized public void addListEntry(String entry){
		this.vec.add(0, entry);
		this.list.setListData(vec);
		}
	/**
	 * Sends the Text from the input box to the topic
	 * or executed the command given by the user
	 * 
	 */
	private void processUserText(){
		String inp=this.textField.getText();
		//clear field now to prevent the user from sending it more than one time if the processText() takes longer
		this.textField.setText("");
		//let the processText handle it.
		this.tp.processText(inp);
	}
}
