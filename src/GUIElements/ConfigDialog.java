package GUIElements;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;


/**
 * @author Ari Ayvazyan
 * @version 12.02.2014
 * 
 * This jdialog uses the input parameters and lets the user see and change them.
 * ConfigDialog is blocking until the user closes the window!
 * 
 */
public class ConfigDialog extends JDialog {
	/** The content panel. */
	private final JPanel contentPanel = new JPanel();
	
	/** The txt server. */
	public JTextField txtServer;
	
	/** The txt user. */
	public JTextField txtUser;
	
	/** The txt Topic. */
	public JTextField txtTopic;

	
	
	/**
     *  This jdialog uses the input parameters and lets the user see and change them.
     *  ConfigDialog is blocking until the user closes the window!
	 */
	public ConfigDialog(String user,String Topic, String server) {
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 300, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Server", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		JPanel panel_4 = new JPanel();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
						.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(105, Short.MAX_VALUE))
		);
		
		panel_4.setLayout(new GridLayout(0, 2, 0, 0));
		JPanel panel_2 = new JPanel();
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Benutzername", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setLayout(new BorderLayout(0, 0));
		{
			txtUser = new JTextField();
			txtUser.setText(user);
			panel_2.add(txtUser, BorderLayout.CENTER);
			txtUser.setColumns(10);
		}
		JPanel panel_3 = new JPanel();
		panel_4.add(panel_3);
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Topic", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setLayout(new BorderLayout(0, 0));
		{
			txtTopic = new JTextField();
			txtTopic.setText(Topic);
			panel_3.add(txtTopic, BorderLayout.CENTER);
			txtTopic.setColumns(10);
		}
		
		panel.setLayout(new BorderLayout(0, 0));
		
		txtServer = new JTextField();
		panel.add(txtServer);
		txtServer.setText(server);
		txtServer.setColumns(10);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
