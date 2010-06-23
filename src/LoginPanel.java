import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Login Panel to Google Accounts.
 * 
 * @author Mohamed Mansour
 * @since 2010-06-23
 */
public class LoginPanel extends JPanel implements ActionListener {
  private MainFrame frame;
  private JTextField txtUser;
  private JTextField txtPassword;
  private JButton btnLogin;
  
  public LoginPanel(MainFrame frame) {
    this.frame = frame;
    initComponents();
  }

  private void initComponents() {
    setLayout(new GridBagLayout());
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(2, 2, 2, 2);
    add(new JLabel("Username: "), gbc);
    
    txtUser = new JTextField();
    txtUser.setPreferredSize(new Dimension(200, 25));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(2, 2, 2, 2);
    add(txtUser, gbc);
    
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(2, 2, 2, 2);
    add(new JLabel("Password: "), gbc);
    
    txtPassword = new JTextField();
    txtPassword.setPreferredSize(new Dimension(200, 25));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.insets = new Insets(2, 2, 2, 2);
    add(txtPassword, gbc);
    
    btnLogin = new JButton("Login");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.insets = new Insets(2, 2, 2, 2);
    add(btnLogin, gbc);
    btnLogin.addActionListener(this);
    
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // Once we log in, transfer the view to the Contacts Card.
    if (GoogleContactsAPI.getInstance().Login(txtUser.getText(), txtPassword.getText())) {
      frame.showView(MainFrame.CONTACTS);
    } else {
      frame.showInfoMessage("Error Logging in!");
    }
  }
}
