/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AP2PCTabPanel.java
 *
 * Created on Jul 12, 2010, 10:30:23 AM
 */
package ap2pc.graphics;

import ap2pc.graphics.contactList.ConversationInviteList;
import ap2pc.graphics.contactList.UserListModel;
import ap2pc.conversation.Conversation;
import ap2pc.conversation.ConversationMessage;
import ap2pc.main.ConversationVisual;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author sharvey
 */
public class TabPanel extends javax.swing.JPanel implements ConversationVisual {

    /** Creates new form AP2PCTabPanel */
    public TabPanel(Frame f, Conversation c) {
        conversation = c;
        model = new UserListModel(c);
        initComponents();
        parent = f;
        displayPane.setEditable(false);
        c.setVisual(this);
        contactListDialog = new JDialog(parent);
        contactList = new ConversationInviteList(contactListDialog, parent.getModel());
        contactListDialog.setContentPane(contactList);
        contactListDialog.pack();
        contactListDialog.setVisible(false);
        contactListDialog.setTitle("AP2PC - Contact List");
        contactList.setTabParent(this);
    }

    public void setUsername(JLabel uf) {
        userField = uf;
    }

    public void updateDisplay(String s) {
        Document d = displayPane.getDocument();
        try {
            d.insertString(d.getLength(), s + "\n", null);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inputField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        userField = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        inviteButton = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        closeButton = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        userList = new JList(model);
        jScrollPane1 = new javax.swing.JScrollPane();
        displayPane = new javax.swing.JEditorPane();

        setPreferredSize(new java.awt.Dimension(480, 320));

        inputField.setText("Enter Text Here");
        inputField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputFieldKeyPressed(evt);
            }
        });

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        userField.setText(System.getProperty("user.name"));

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 32));

        inviteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ap2pc/rsrc/icon/IM.png"))); // NOI18N
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

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ap2pc/rsrc/icon/broadcast_off.png"))); // NOI18N
        jToggleButton1.setToolTipText("Broadcast this conversation");
        jToggleButton1.setDoubleBuffered(true);
        jToggleButton1.setEnabled(false);
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ap2pc/rsrc/icon/broadcast_on.png"))); // NOI18N
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jToggleButton1);

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

        jSplitPane1.setDividerLocation(360);
        jSplitPane1.setResizeWeight(1.0);
        jSplitPane1.setContinuousLayout(true);
        jSplitPane1.setLastDividerLocation(100);
        jSplitPane1.setOneTouchExpandable(true);

        userList.setMaximumSize(new java.awt.Dimension(2000, 80));
        userList.setMinimumSize(new java.awt.Dimension(150, 80));
        userList.setPreferredSize(new java.awt.Dimension(100, 80));
        jScrollPane2.setViewportView(userList);

        jSplitPane1.setRightComponent(jScrollPane2);

        jScrollPane1.setViewportView(displayPane);

        jSplitPane1.setLeftComponent(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputField, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendButton))
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendButton)
                    .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userField)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        String text = inputField.getText();
        inputField.setText("");
        conversation.sendCMsg(text);
    }//GEN-LAST:event_sendButtonActionPerformed

    private void inputFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputFieldKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            String text = inputField.getText();
            inputField.setText("");
            conversation.sendCMsg(text);
        }
    }//GEN-LAST:event_inputFieldKeyPressed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        conversation.close();
        parent.closeConversation(this);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void inviteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inviteButtonActionPerformed
        contactListDialog.setVisible(true);
    }//GEN-LAST:event_inviteButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JEditorPane displayPane;
    private javax.swing.JTextField inputField;
    private javax.swing.JButton inviteButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton sendButton;
    private javax.swing.JLabel userField;
    private javax.swing.JList userList;
    // End of variables declaration//GEN-END:variables
    private Frame parent;
    private Conversation conversation;
    private UserListModel model;
    private JDialog contactListDialog;
    private ConversationInviteList contactList;

    public String getIdentifier() {
        return conversation.getIdentifier();
    }

    public void displayMessage(ConversationMessage message) {
        updateDisplay(message.toString());
    }

    public void displayStatus(String status) {
        updateDisplay(status);
    }

    public void listNotify(int index, CHANGETYPE type) {
        model.listNotify(index, type);
    }

    public Conversation getConversation()
    {
        return conversation;
    }
}
