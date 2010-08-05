/*
 * AP2PCFrame.java
 * AP2PCFrame is the main user client interaction window.
 * AP2PCFrame extends JFrame and implements AP2PCVisual.java
 *
 * The client interaction window is generated and populated;
 * events are handled by actionHandlers as specified below
 * Created on Jul 12, 2010, 10:22:42 AM
 */
// This is where it lives.  It is happy there.
package ap2pc.graphics;

import ap2pc.graphics.contactList.ContactListModel;
import ap2pc.graphics.contactList.ContactListPanel;
import ap2pc.main.AP2PC;
import ap2pc.main.visual.MainVisual;
import ap2pc.main.conversation.Conversation;
import ap2pc.main.Me;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * Authors: Sarah Harvey and Alan Owens
 * CS 412 Project, Summer 2010
 * Dr. Rahimi
 * Southern Illinois University
 *
 */
public class Frame extends javax.swing.JFrame implements MainVisual {

    /** Creates new form AP2PCFrame and populates with components */
    public Frame() {
        initComponents();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            /** Instead of the application simply terminating with
             * System.exit(int); the client program will notify other
             * clients of its going offline
             */
            public void windowClosing(WindowEvent e) {
                /** The mechanism for this is actually handled by the
                 * quit() method below.
                 */
                quit();
            }
        });

        settingsDialog = new JDialog(this);
        settings = new SettingsPanel(settingsDialog);
        settingsDialog.setContentPane(settings);
        SwingUtilities.updateComponentTreeUI(settingsDialog);
        settingsDialog.repaint();
        settingsDialog.pack();
        settingsDialog.setVisible(false);
        settingsDialog.setTitle("AP2PC - Settings");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    /** We totally modified this code, btw.  We're jerks that don't listen. */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        statusBar = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        contactListButton = new javax.swing.JButton();
        settingsButton = new javax.swing.JButton();
        statusComboBox = new javax.swing.JComboBox();
        tabs = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        status = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        settingsMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        quitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();
        helpMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Awesome P2P Chat - CS412");

        statusBar.setText("Status OK");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setMinimumSize(new java.awt.Dimension(46, 48));
        jToolBar1.setPreferredSize(new java.awt.Dimension(46, 32));

        contactListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ap2pc/rsrc/icon/People.png"))); // NOI18N
        contactListButton.setFocusable(false);
        contactListButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        contactListButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        contactListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactListButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(contactListButton);

        settingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ap2pc/rsrc/icon/Settings.png"))); // NOI18N
        settingsButton.setFocusable(false);
        settingsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        settingsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(settingsButton);

        statusComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Available", "Away", "DND", "XA" }));
        statusComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusComboBoxActionPerformed(evt);
            }
        });

        tabs.setMinimumSize(new java.awt.Dimension(100, 80));

        status.setColumns(20);
        status.setEditable(false);
        status.setRows(5);
        jScrollPane1.setViewportView(status);

        tabs.addTab("status", jScrollPane1);

        jMenu1.setText("AP2PC");

        settingsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        settingsMenuItem.setText("Settings");
        settingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(settingsMenuItem);
        jMenu1.add(jSeparator2);

        quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitMenuItem.setText("Quit");
        quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(quitMenuItem);

        menuBar.add(jMenu1);

        jMenu2.setText("Help");

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(aboutMenuItem);

        helpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpMenuItem.setText("Help");
        helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(helpMenuItem);

        menuBar.add(jMenu2);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** Once users are selected from the userList, the array of desired users
    is passed to the main ap2pc class' conversationCreate() method. */
    /** Handles when 'settings' is selected. */
    private void settingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsMenuItemActionPerformed
        settings();
    }//GEN-LAST:event_settingsMenuItemActionPerformed
    /** Handles when 'quit' is selected.  quit() is the same method called when
     * the jFrame is closed
     */
    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitMenuItemActionPerformed
        quit();
    }//GEN-LAST:event_quitMenuItemActionPerformed
    /** Handles when 'about' is selected. */
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        showAbout();
    }//GEN-LAST:event_aboutMenuItemActionPerformed
    /** Handles when 'help' is selected. */
    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMenuItemActionPerformed
        showHelp();
    }//GEN-LAST:event_helpMenuItemActionPerformed
    /** Handles when a 'status' is selected, by setting the state on the main
     * ap2pc object.
     */
    private void statusComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusComboBoxActionPerformed
        switch (statusComboBox.getSelectedIndex()) {
            case 0:
                ap2pc.getMe().setShow(Me.SHOW.AVAILABLE);
                break;
            case 1:
                ap2pc.getMe().setShow(Me.SHOW.AWAY);
                break;
            case 2:
                ap2pc.getMe().setShow(Me.SHOW.DND);
                break;
            case 3:
                ap2pc.getMe().setShow(Me.SHOW.XA);
                break;
            default:
                ap2pc.getMe().setShow(Me.SHOW.UNKNOWN);
                break;
        }
    }//GEN-LAST:event_statusComboBoxActionPerformed

    private void contactListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactListButtonActionPerformed
        contactListDialog.setVisible(true);
    }//GEN-LAST:event_contactListButtonActionPerformed

    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        settingsDialog.setVisible(true);
    }//GEN-LAST:event_settingsButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    /** If this was the 1970s, this would be my main man.
     * However, it is 2010, and this is simply the main method,
     * where the program begins execution.
     */
    public static void main(String args[]) {
        /** Creates a new frame object */
        Frame f = new Frame();
        /** a new instance of AP2PC for the launching */
        AP2PC a = new AP2PC(f);
        /** Displaying the client to the user */
        f.setVisible(true);
        /** associating the jFrame with the main application */
        f.setAP2PC(a);
        /** G-O */
        a.init();

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton contactListButton;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JButton settingsButton;
    private javax.swing.JMenuItem settingsMenuItem;
    private javax.swing.JTextArea status;
    private javax.swing.JLabel statusBar;
    private javax.swing.JComboBox statusComboBox;
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JLabel userField = new javax.swing.JLabel();
    private AP2PC ap2pc;
    private JDialog settingsDialog;
    private SettingsPanel settings;
    private JDialog contactListDialog;
    private ContactListPanel contactList;

    /** Guilty by association. */
    public void setAP2PC(AP2PC a) {
        ap2pc = a;
        settings.setAP2PC(a);

        contactListDialog = new JDialog(this);
        contactList = new ContactListPanel(contactListDialog, ap2pc);
        contactListDialog.setContentPane(contactList);
        contactListDialog.pack();
        contactListDialog.setVisible(false);
        contactListDialog.setTitle("AP2PC - Contact List");
    }

    public void updateProgramStatus(String programStatus) {
        statusBar.setText(programStatus);
        status.append(programStatus + "\n");
    }

    public void showAbout() {
        JOptionPane.showMessageDialog(this,
                "Awesome P2P Chat\nVersion: 0.01a\nAuthors: \nSarah Harvey\nAlan Owens"
                + "\nSouthern Illinois University - Carbondale, 2010");
    }

    public void showHelp() {
        JOptionPane.showMessageDialog(this, "We really don't know what we're "
                + "doing either, sorry!\nGood luck!");
    }

    public void settings() {
        settingsDialog.setVisible(true);
    }

    public void quit() {
//        JOptionPane.showMessageDialog(this, "Thank you for using AP2PC!\n"
//                + "Tip your waitress!");
        ap2pc.terminate();
    }

    public TabPanel getConversationTab(String identifier) {
        for (int i = 1; i < tabs.getTabCount(); i++) {
            TabPanel p = (TabPanel) tabs.getComponentAt(i);
            if (p.getIdentifier().equals(identifier)) {
                return p;
            }
        }
        return null;
    }

    public void initConversation(Conversation c) {
        TabPanel p = null;
        p = getConversationTab(c.getIdentifier());
        if (p == null) {
            String name = c.getName();
            if (name == null || name.equalsIgnoreCase("")) {
                name = c.getIdentifier().substring(0, 6);
            }
            p = new TabPanel(this, c);
            p.setUsername(userField);
            tabs.add(p);
            tabs.setTitleAt(tabs.getTabCount() - 1, name);
            tabs.setSelectedIndex(tabs.getTabCount() - 1);
        }
    }

    public void closeConversation(TabPanel t)
    {
        tabs.remove(t);
    }

    public void contactListNotify(int index, CHANGETYPE type) {
        contactList.contactListNotify(index, type);
    }

    public void updateUsername(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean requestToJoinConversation(String from, String identifier) {
        int result = JOptionPane.showOptionDialog(this, from+" requests that you join "+identifier+"; confirm?", "AP2PC - Request to join conversation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        switch (result)
        {
            case JOptionPane.YES_OPTION: return true;
            default: return false;
        }
    }

    public ContactListModel getModel()
    {
        return contactList.getModel();
    }
    
}
