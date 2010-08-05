/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ContactListPanel.java
 *
 * Created on Aug 3, 2010, 8:53:09 AM
 */

package ap2pc.graphics.contactList;

import ap2pc.graphics.TabPanel;
import ap2pc.main.AP2PC;
import ap2pc.main.User;
import ap2pc.main.MainVisual.CHANGETYPE;
import javax.swing.JDialog;
import javax.swing.JList;

/**
 *
 * @author sharvey
 */
public class ConversationInviteList extends javax.swing.JPanel {

    /** Creates new form ContactListPanel */
    public ConversationInviteList(JDialog j, ContactListModel m) {
        parent = j;
        model = m;
        initComponents();
        userList.setCellRenderer(new UserEntryPanel());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        userList = new JList(model);
        jToolBar1 = new javax.swing.JToolBar();
        inviteButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        jScrollPane1.setViewportView(userList);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 32));

        inviteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ap2pc/rsrc/icon/SMS.png"))); // NOI18N
        inviteButton.setToolTipText("Invite people to this conversation");
        inviteButton.setFocusable(false);
        inviteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        inviteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        inviteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inviteButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(inviteButton);

        closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ap2pc/rsrc/icon/close.png"))); // NOI18N
        closeButton.setFocusable(false);
        closeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        closeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(closeButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        parent.setVisible(false);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void inviteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inviteButtonActionPerformed

        Object[] obj = userList.getSelectedValues();
        if (obj.length <= 0) return;

        for (int i = 0; i < obj.length; i++) {
            tabParent.getConversation().invite((User)obj[i]);
        }
//        /** See?  ap2pc.conversationCreate(users);
//         * Toldya.
//         */
//        if (tabs.getSelectedIndex() == 0) {
//            ap2pc.conversationCreate(users);
//        } else {
//            TabPanel p = (TabPanel) tabs.getSelectedComponent();
//            String id = p.getIdentifier();
//            ap2pc.conversationInvite(id, users);
//        }
    }//GEN-LAST:event_inviteButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JButton inviteButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JList userList;
    // End of variables declaration//GEN-END:variables
    private JDialog parent;
    private AP2PC ap2pc;
    private ContactListModel model;
    private TabPanel tabParent;

    public void contactListNotify(int index, CHANGETYPE type) {
        model.contactListNotify(index, type);
    }

    public void setTabParent(TabPanel p)
    {
        tabParent = p;
    }

}
