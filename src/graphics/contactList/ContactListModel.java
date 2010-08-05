/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ap2pc.graphics.contactList;

import ap2pc.main.AP2PC;
import ap2pc.main.visual.MainVisual.CHANGETYPE;
import javax.swing.AbstractListModel;

/**
 *
 * @author sarah
 */
public class ContactListModel extends AbstractListModel {

    private AP2PC ap2pc;

    public ContactListModel(AP2PC a) {
        ap2pc = a;
    }

    public int getSize() {
        return ap2pc.getUserHashMap().size();
    }

    public Object getElementAt(int index) {
        return ap2pc.getUserHashMap().values().toArray()[index];
    }

    public void contactListNotify(int index, CHANGETYPE type) {
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
