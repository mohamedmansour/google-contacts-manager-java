package com.mindtechnologies.contactsmanager.ui.async;

import java.util.List;

import javax.swing.SwingWorker;

import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.PhoneNumber;
import com.mindtechnologies.contactsmanager.callbacks.ContactsFoundCallback;
import com.mindtechnologies.contactsmanager.service.GoogleContactsAPI;
import com.mindtechnologies.contactsmanager.ui.ContactsPanel;

/**
 * Asynchronous Worker to fetch entries and update the progress bar.
 * 
 * @author Mohamed Mansour
 * @since 2010-06-23
 */
public class AsyncContactsRetrieval extends SwingWorker<List<ContactEntry>, ContactEntry> {  
    // Keep track of how many contacts we fetched. 
    private int count;
    
    // TODO: Make more callbacks instead of passing in this.
    private ContactsPanel pnlContacts;
    
    public AsyncContactsRetrieval(ContactsPanel pnlContacts) {
      this.pnlContacts = pnlContacts;
    }
    
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
      pnlContacts.setStatusText("Processed " + count + " contacts!");
      
      // Append the entries to the table model.
      for (ContactEntry entry : chunks) {
        pnlContacts.addContact(new String[] {
          entry.getTitle().getPlainText(),
          getEmails(entry.getEmailAddresses()),
          getPhoneNumbers(entry.getPhoneNumbers()),
        });
      }
    }
    
    @Override
    protected void done() {
      pnlContacts.onContactsRetrievalComplete();
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
