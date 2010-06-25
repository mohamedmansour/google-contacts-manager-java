package com.mindtechnologies.contactsmanager.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.PhoneNumber;
import com.mindtechnologies.contactsmanager.callbacks.ContactsFoundCallback;
import com.mindtechnologies.contactsmanager.service.GoogleContactsAPI;
import com.mindtechnologies.contactsmanager.ui.async.AsyncContactsRetrieval;


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
  private AsyncContactsRetrieval entriesTask;
  private JLabel lblStatus;
  private DefaultTableModel model;
  private TableRowSorter<TableModel> sorter;
  private TextField txtSearch;
  private JButton btnExport;
  
  public ContactsPanel(MainFrame frame) {
    this.frame = frame;
    initComponents();
    setupEntriesTask();
  }


  private void initComponents() {
    setLayout(new BorderLayout());
    setBackground(Color.WHITE);
    
    // Top Panel that contains Progress and Status Label.
    JPanel topPanel = new JPanel(new BorderLayout(5,5));
    topPanel.setOpaque(false);
    JLabel lblLogo = new JLabel();
    try {
      lblLogo.setIcon(new ImageIcon(new URL("http://www.google.com/images/logo_sm.gif")));
      topPanel.add(lblLogo, BorderLayout.NORTH);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    lblStatus = new JLabel("Press Fetch below to get all Entries.");
    topPanel.add(lblStatus, BorderLayout.WEST);
    progressBar = new JProgressBar();
    progressBar.setVisible(false);
    topPanel.add(progressBar, BorderLayout.CENTER);
    
    // Controls.
    JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pnlControls.add(new JLabel("Search: "));
    txtSearch = new TextField();
    txtSearch.setColumns(40);
    txtSearch.addActionListener(this);
    pnlControls.add(txtSearch);
    btnExport = new JButton("Export");
    btnExport.addActionListener(this);
    pnlControls.add(btnExport);
    topPanel.add(pnlControls, BorderLayout.SOUTH);
    add(topPanel, BorderLayout.NORTH);
    
    // Center Panel that contains the table.
    JTable tableEntries = new JTable();
    model = new DefaultTableModel(new Object[][]{}, new String[] {
      "Name", "E-Mail", "Phone"
    });
    sorter = new TableRowSorter<TableModel>(model);
    tableEntries.setRowSorter(sorter);
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
    entriesTask = new AsyncContactsRetrieval(this);
    
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
    Object source = e.getSource();
    if (btnFetch == source) {
      progressBar.setVisible(true);
      btnFetch.setVisible(false);
      entriesTask.execute();
    }
    else if (txtSearch == source) {
      String expr = txtSearch.getText();
      sorter.setRowFilter(RowFilter.regexFilter(expr));
      sorter.setSortKeys(null);
    }
    else if (btnExport == source) {
      try {
        CSVWriter writer = new CSVWriter(new FileWriter("contacts.csv"));
        int rowCount = model.getRowCount();
        for (int i = 0; i < rowCount; i++) {
          Vector<String> vec = (Vector<String>) model.getDataVector().elementAt(i);
          writer.writeNext(vec.toArray(new String[0]));
        }
        writer.close();
      } catch (IOException ex) {
        frame.showInfoMessage("Error while writing CSV: " + ex.getMessage());
      }
    }
  }

  /**
   * Add a new contact to the model.
   * @param entries The list of entries to add.
   */
  public void addContact(String[] entries) {
    model.addRow(entries);
  }

  /**
   * Set the status text for the contact view.
   * @param msg The message to set.
   */
  public void setStatusText(String msg) {
    lblStatus.setText(msg);
  }

  /**
   * When all the contacts are retrieved and complete.
   */
  public void onContactsRetrievalComplete() {
    progressBar.setVisible(false);
    btnFetch.setText("Fetch");
  }
}
