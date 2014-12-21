package fi.eis.applications.chatapp.chat.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import fi.eis.applications.chatapp.chat.actions.ChattingConnection;
import fi.eis.applications.chatapp.chat.actions.impl.MessageUpdaterImpl;

/**
 * Created with IntelliJ IDEA.
 * Date: 3.11.2014
 * Time: 22:53
 * 
 * @author eis
 */
public class ChatUI extends JFrame {

    private final JPanel userListPanel;
    private final MessagesPanel messagesPanel;
    private final InputPanel inputPanel;

    private ChatUI(ChattingConnection conn) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 500));

        this.userListPanel = createUserListPanel();

        this.messagesPanel = MessagesPanel.createMessagesPanel();

        this.inputPanel = InputPanel.createInputPanel();

        JMenuBar menuBar = createMenu();

        conn.setUpdater(new MessageUpdaterImpl(messagesPanel.getEditorPane()));

        
        // Put everything on a panel.
        panel.add(this.messagesPanel,
                BorderLayout.CENTER);
        panel.add(this.userListPanel,
                BorderLayout.EAST);
        panel.add(this.inputPanel,
                BorderLayout.PAGE_END);

        // JFrame operations
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
    public static void createAndShowGUI(ChattingConnection conn) {
        //Create and set up the window.
        ChatUI frame = new ChatUI(conn);

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
    }

    private void focusOnInput() {
        getInputField().requestFocusInWindow();
    }

    private JMenuBar createMenu() {
        //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();

        //Build the first menu.
        JMenu menu = new JMenu("Window");
        menu.setMnemonic(KeyEvent.VK_W);

        JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("User list");

        cbMenuItem.setState(true);
        cbMenuItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                userListPanel.setVisible(e.getStateChange() == ItemEvent.SELECTED);
            }
        });

        menu.add(cbMenuItem);
        menuBar.add(menu);

        return menuBar;
    }

    private JTextField getInputField() {
        return inputPanel.getInputField();
    }

    private static JPanel createUserListPanel() {
        JEditorPane userListPane = createEditorPane();
        JScrollPane userListScrollPane = new JScrollPane(userListPane);
        userListScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        userListScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        userListScrollPane.setMinimumSize(new Dimension(10, 10));


        JPanel userListPanel = new JPanel(new GridLayout(1, 0));
        userListPanel.add(userListScrollPane);
        userListPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("User list"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return userListPanel;
    }
    private static JEditorPane createEditorPane() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        return editorPane;
    }
}
