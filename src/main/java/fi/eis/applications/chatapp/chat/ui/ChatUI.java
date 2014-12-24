package fi.eis.applications.chatapp.chat.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fi.eis.applications.chatapp.chat.actions.ChattingConnection;
import fi.eis.applications.chatapp.chat.actions.impl.MessageUpdaterImpl;
import fi.eis.applications.chatapp.configuration.Configuration;

/**
 * Created with IntelliJ IDEA.
 * Date: 3.11.2014
 * Time: 22:53
 * 
 * @author eis
 */
public class ChatUI extends JFrame {

    private final UserListPanel userListPanel;
    private final MessagesPanel messagesPanel;
    private final InputPanel inputPanel;

    private final Configuration configuration;
    private final ChattingConnection connection;
    
    private ChatUI(ChattingConnection conn, Configuration configuration) {

        this.configuration = configuration;
        this.connection = conn;
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 500));

        this.userListPanel = UserListPanel.createUserListPanel();

        this.messagesPanel = MessagesPanel.createMessagesPanel();

        this.inputPanel = InputPanel.createInputPanel();

        JMenuBar menuBar = createMenu();

        conn.setUpdater(new MessageUpdaterImpl(
                messagesPanel.getEditorPane(),
                userListPanel.getUserList()
                ));
        
        this.inputPanel.setChattingConnection(conn);

        
        // Put everything on a panel.
        panel.add(this.messagesPanel,
                BorderLayout.CENTER);
        panel.add(this.userListPanel,
                BorderLayout.EAST);
        panel.add(this.inputPanel,
                BorderLayout.PAGE_END);

        // JFrame operations
        
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                ChatUI.this.connection.cancel();
                ChatUI.this.configuration.save();
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setJMenuBar(menuBar);
        add(panel);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     * @param selectedRoomId
     * @param sessionId
     */
    public static ChatReturn createAndShowGUI(ChattingConnection conn, Configuration configuration) {
        //Create and set up the window.
        ChatUI frame = new ChatUI(conn, configuration);

        try {

            // pack makes size correspond to content
            frame.pack();
            // Center the window
            frame.setLocationRelativeTo(null);
            // Set focus
            frame.focusOnInput();
            
            // connect to chat
            conn.connect();
            
            // execute background connections
            conn.execute();
    
            // Show
            frame.setVisible(true);
            return ChatReturn.NORMAL_EXIT;
        } catch (RuntimeException ex) {
            conn.cancel();
            JOptionPane.showMessageDialog(frame, "Exception: " + ex.getMessage());
            frame.dispose();
            return ChatReturn.EXCEPTION;
        }
    }
    
    public enum ChatReturn {
        EXCEPTION, NORMAL_EXIT;
    }

    private void focusOnInput() {
        inputPanel.focusOnInputField();
    }

    private JMenuBar createMenu() {
        //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();

        //Build the first menu.
        JMenu menu = new JMenu("Window");
        menu.setMnemonic(KeyEvent.VK_W);

        JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("User list");

        boolean showUserList = this.configuration.getShowUserList(true);
        cbMenuItem.setState(showUserList);
        userListPanel.setVisible(showUserList);
        cbMenuItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean showUserList = (e.getStateChange() == ItemEvent.SELECTED);
                userListPanel.setVisible(showUserList);
                ChatUI.this.configuration.setShowUserList(showUserList);
            }
        });

        menu.add(cbMenuItem);
        menuBar.add(menu);

        return menuBar;
    }

}
