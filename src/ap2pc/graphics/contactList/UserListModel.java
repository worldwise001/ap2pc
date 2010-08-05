/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ap2pc.graphics.contactList;

import ap2pc.conversation.Conversation;
import ap2pc.main.ConversationVisual.CHANGETYPE;
import javax.swing.AbstractListModel;

/**
 *
 * @author sarah
 */
public class UserListModel extends AbstractListModel {

    private Conversation conversation;

    public UserListModel(Conversation c) {
        conversation = c;
    }

    public int getSize() {
        return conversation.getUsers().size();
    }

    public Object getElementAt(int index) {
        return conversation.getUsers().get(index);
    }

    public void listNotify(int index, CHANGETYPE type) {
        switch (type) {
            case MODIFY:
                fireContentsChanged(this, index, index);
                break;
            case ADD:
                fireIntervalAdded(this, index, index);
                break;
            case REMOVE:
                fireIntervalRemoved(this, index, index);
                break;
        }
    }
}
