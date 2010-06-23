import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.util.AuthenticationException;

/**
 * Google Contacts API as a singleton.
 * 
 * @author Mohamed Mansour
 * @since 2010-06-23
 */
public class GoogleContactsAPI {
  private static GoogleContactsAPI obj;
  private final ContactsService service;
  private static final String DEFAULT_FEED = "http://www.google.com/m8/feeds/contacts/";
  private URL feedUrl;
  
  /**
   * Single instance.
   * @return This object.
   */
  public static GoogleContactsAPI getInstance() {
    if (obj == null) {
      obj = new GoogleContactsAPI();
    }
    return obj;
  }
  
  private GoogleContactsAPI() {
    service = new ContactsService("Google-Contacts-Management");
  }
  
  /**
   * Login into Google Accounts.
   * @param username Your Google email address.
   * @param password Your Google password.
   * @return
   */
  public boolean Login(String username, String password) {
    try {
      service.setUserCredentials(username, password);
      String url = DEFAULT_FEED + username + "/full";
      feedUrl = new URL(url);
      return true;
    } catch (AuthenticationException e) {
      return false;
    } catch (MalformedURLException e) {
      return false;
    }
  }
  
  /**
   * Get the total contact entries.
   * @return number of entries.
   */
  public int getTotalEntries() {
    Query query = new Query(feedUrl);
    try {
      ContactFeed feed = service.query(query, ContactFeed.class);
      return feed.getTotalResults();
    } catch (Exception e) {
      return 0;
    }
  }
  
  /**
   * Fetch all contacts. 
   * @param callback Callback that gets fired everytime a request has been recieved.
   * @return List of contacts.
   * @throws Exception In case an exception occurred.
   */
  public List<ContactEntry> getEntries(ContactsFoundCallback callback) throws Exception {
    ContactFeed feed;
    Query query = new Query(feedUrl);
    List<ContactEntry> googleContacts = new ArrayList<ContactEntry>();
    do {
      feed = service.query(query, ContactFeed.class);
      googleContacts.addAll(feed.getEntries());
      query.setStartIndex(feed.getEntries().size() + query.getStartIndex());
      callback.itemsFound(query.getStartIndex(), feed.getEntries());
    } while (feed.getTotalResults() > query.getStartIndex());
    
    return googleContacts;
  }
}
