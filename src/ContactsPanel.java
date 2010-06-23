import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.PhoneNumber;


/**
 * Contacts Panel Card View.
 * 
 * @author Mohamed Mansour
 * @since 2010-06-23
 */
public class ContactsPanel extends JPanel implements ActionListener {

  private MainFrame frame;
  private JProgressBar progressBar;
  private JButton btnFetch;
  private FetchEntries entriesTask;
  private JLabel lblStatus;
  private DefaultTableModel model;
  
  public ContactsPanel(MainFrame frame) {
    this.frame = frame;
    initComponents();
    setupEntriesTask();
  }


  private void initComponents() {
    setLayout(new BorderLayout(5,5));
    
    // Top Panel that contains Progress and Status Label.
    JPanel topPanel = new JPanel(new BorderLayout(5,5));
    lblStatus = new JLabel("Press Fetch below to get all Entries.");
    topPanel.add(lblStatus, BorderLayout.WEST);
    progressBar = new JProgressBar();
    topPanel.add(progressBar, BorderLayout.CENTER);
    progressBar.setVisible(false);
    add(topPanel, BorderLayout.NORTH);
    
    // Center Panel that contains the table.
    JTable tableEntries = new JTable();
    model = new DefaultTableModel(new Object[][]{}, new String[] {
      "Name", "E-Mail", "Phone"
    });
    tableEntries.setModel(model);
    add(new JScrollPane(tableEntries), BorderLayout.CENTER);
    
    // Bottom Panel that contains the buttons.
    btnFetch = new JButton("Fetch");
    btnFetch.addActionListener(this);
    add(btnFetch, BorderLayout.SOUTH);
  }

  /**
   * Setup the Contact Entries task.
   */
  private void setupEntriesTask() {
    entriesTask = new FetchEntries();
    
    // Add a change listener so that we can update the progress bar.
    entriesTask.addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
          progressBar.setValue((Integer)evt.getNewValue());
        }
      }
    });
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    // For now until we figure out what to do with optional buttons if we need it.
    if (btnFetch.getText().equals("Fetch")) {
      progressBar.setVisible(true);
      btnFetch.setVisible(false);
      entriesTask.execute();
    }
  }
  
  /**
   * Asynchronous Worker to fetch entries and update the progress bar.
   * 
   * @author Mohamed Mansour
   * @since 2010-06-23
   */
  class FetchEntries extends SwingWorker<List<ContactEntry>, ContactEntry> {
    // Keep track of how many contacts we fetched. 
    private int count;
    
    @Override
    protected List<ContactEntry> doInBackground() throws Exception {
      count = 0;
      final int totalEntries = GoogleContactsAPI.getInstance().getTotalEntries();
      List<ContactEntry> r = GoogleContactsAPI.getInstance().getEntries(new ContactsFoundCallback() {
        @Override
        public void itemsFound(int index, List<ContactEntry> entries) {
          // Publish the results to the table model.
          publish(entries.toArray(new ContactEntry[0]));
          
          // Make use of the SwingWorkers progress feature.
          setProgress(100 * index / totalEntries);
        }
      });
      return r;
    }
    
    @Override
    protected void process(List<ContactEntry> chunks) {
      count += chunks.size();
      
      // Change the status to state how many contacts we fetched.
      lblStatus.setText("Processed " + count + " contacts!");
      
      // Append the entries to the table model.
      for (ContactEntry entry : chunks) {
        model.addRow(new String[] {
          entry.getTitle().getPlainText(),
          getEmails(entry.getEmailAddresses()),
          getPhoneNumbers(entry.getPhoneNumbers()),
        });
      }
    }
    
    @Override
    protected void done() {
      progressBar.setVisible(false);
      btnFetch.setText("Fetch");
    }
  }
  
  /**
   * Helper method to stringify the phonenumber listings.
   * @param phonenumbers New phonenumbers to convert.
   * @return String version of the phonenumbers.
   */
  private String getPhoneNumbers(List<PhoneNumber> phonenumbers) {
    StringBuilder builder = new StringBuilder();
    for (PhoneNumber phonenumber : phonenumbers) {
      builder.append(phonenumber.getPhoneNumber() + ", ");
    }
    return builder.toString();
  }
  
  /**
   * Helper method to stringify the email listings.
   * @param emails New emails to convert.
   * @return String version of the emails.
   */
  private String getEmails(List<Email> emails) {
    StringBuilder builder = new StringBuilder();
    for (Email email : emails) {
      builder.append(email.getAddress() + ", ");
    }
    return builder.toString();
  }
}
