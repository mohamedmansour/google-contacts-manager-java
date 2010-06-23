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
  
  // TODO(mohamed): Make this an enum.
  public static String LOGIN = "login";
  public static String CONTACTS = "contacts";
  
  public MainFrame() {
    initComponents();
  }

  private void initComponents() {
    setLayout(new BorderLayout(10,10));
    cards = new JPanel();
    cards.setLayout(new CardLayout(5,5));
    pnlLogin = new LoginPanel(this);
    cards.add(pnlLogin, LOGIN);
    pnlContacts = new ContactsPanel(this);
    cards.add(pnlContacts, CONTACTS);
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
  public void showView(String view) {
    CardLayout cl = (CardLayout)(cards.getLayout());
    cl.show(cards, view);
  }
}
