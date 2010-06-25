package com.mindtechnologies.contactsmanager.ui;
import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * Main Frame of the Applicaiton, contains a bunch of Card Layouts so we can
 * have swappable views.
 * 
 * @author Mohamed Mansour
 * @since 2010-06-23
 */
public class MainFrame extends JFrame {
  private JPanel cards;
  private LoginPanel pnlLogin;
  private ContactsPanel pnlContacts;
  
  enum CardType { LOGIN, CONTACTS }
  
  public MainFrame() {
    initComponents();
  }

  private void initComponents() {
    setLayout(new BorderLayout());
    cards = new JPanel();
    cards.setLayout(new CardLayout());
    pnlLogin = new LoginPanel(this);
    cards.add(pnlLogin, CardType.LOGIN.toString());
    pnlContacts = new ContactsPanel(this);
    cards.add(pnlContacts, CardType.CONTACTS.toString());
    add(cards, BorderLayout.CENTER);
  }
  
  /**
   * Show an information dialog message box.
   * @param msg The message to show.
   */
  public void showInfoMessage(String msg) {
   JOptionPane.showMessageDialog(this, msg);
  }
  
  /**
   * Switch to the view.
   * @param view The view to show.
   */
  public void showView(CardType view) {
    CardLayout cl = (CardLayout)(cards.getLayout());
    cl.show(cards, view.toString());
  }
}
