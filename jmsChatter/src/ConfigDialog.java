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
 * The Class ConfigDialog.
 */
public class ConfigDialog extends JDialog {
	/** the combo Box to select the Database System*/
	public JComboBox comboBox;
	/** The content panel. */
	private final JPanel contentPanel = new JPanel();
	
	/** The txt server. */
	public JTextField txtServer;
	
	/** The txt user. */
	public JTextField txtUser;
	
	/** The txt Topic. */
	public JTextField txtTopic;


	public static void main (String args[]){
		new ConfigDialog("","","","").setVisible(true);
	}

	
	
	/**
	 * Erstellt einen dialog
	 *
	 */
	public ConfigDialog(String user,String Topic,String datenbank, String server) {
		setResizable(false);
		setBounds(100, 100, 300, 280);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Server", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datenbank", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setLayout(new BorderLayout(0, 0));
		JPanel panel_4 = new JPanel();
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
						.addComponent(panel_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(63, Short.MAX_VALUE))
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
