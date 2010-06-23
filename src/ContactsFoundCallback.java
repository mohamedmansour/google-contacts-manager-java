import java.util.List;

import com.google.gdata.data.contacts.ContactEntry;

/**
 * Entries found callback to stat
 * 
 * @author Mohamed Mansour
 * @since 2010-06-23
 */
public interface ContactsFoundCallback {
  
  /**
   * When a entry has been found it will fire this callback.
   * @param index The number of items it has fetched.
   * @param list The list of new items that were received.
   */
  public void itemsFound(int index, List<ContactEntry> list);
}
