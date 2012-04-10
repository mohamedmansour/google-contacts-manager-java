package com.mindtechnologies.contactsmanager.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mindtechnologies.contactsmanager.service.GoogleContactsAPI;
import com.mindtechnologies.contactsmanager.ui.MainFrame.CardType;

/**
 * Login Panel to Google Accounts.
 * 
 * @author Mohamed Mansour
 * @since 2010-06-23
 */
public class LoginPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 6637062075802872730L;
    private final MainFrame frame;
    private JTextField txtUser;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    LoginPanel() {
        this(null);
    }
    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        // Google Logo and others
        // TODO: add some reusability to this.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        JLabel lblLogo = new JLabel();
        try {
            lblLogo.setIcon(new ImageIcon(new URL("http://www.google.com/images/logo_sm.gif")));
            add(lblLogo, gbc);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Top Panel that contains Progress and Status Label.
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(2, 2, 2, 2);
        JLabel label = new JLabel("Username: ");
        add(label, gbc);

        txtUser = new JTextField();
        txtUser.setPreferredSize(new Dimension(200, 25));
        txtUser.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(2, 2, 2, 2);
        add(txtUser, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(2, 2, 2, 2);
        add(new JLabel("Password: "), gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(200, 25));
        txtPassword.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(2, 2, 2, 2);
        add(txtPassword, gbc);

        btnLogin = new JButton("Login");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.EAST;
        add(btnLogin, gbc);
        btnLogin.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Once we log in, transfer the view to the Contacts Card.
        if (GoogleContactsAPI.getInstance().Login(txtUser.getText(), new String(txtPassword.getPassword()))) {
            frame.showView(CardType.CONTACTS);
            Dimension dimension = new Dimension(500, 500);
            frame.setMinimumSize(dimension);
            frame.setSize(dimension);
        } else {
            frame.showInfoMessage("Error Logging in!");
        }
    }
}
