package fi.eis.applications.chatapp.ui;

import javax.swing.*;

import fi.eis.applications.chatapp.actions.impl.ChattingConnection;
import fi.eis.applications.chatapp.actions.impl.MessageUpdater;

import java.awt.*;
import java.awt.event.*;

/**
 * Created with IntelliJ IDEA.
 * User: eis
 * Date: 3.11.2014
 * Time: 22:53
 */
public class ChatUI extends JFrame {

    private JTextField inputField;
    private JPanel userListPanel;
    private JPanel messagesPanel;
    private JPanel inputPanel;

    private ChattingConnection conn;

    private ChatUI()  {
        // no-op
    }
    private ChatUI(ChattingConnection conn) {
        this.conn = conn;

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 500));

        this.userListPanel = createUserListPanel();

        this.messagesPanel = createMessagesPanel();

        this.inputPanel = createInputPanel();

        JMenuBar menuBar = createMenu();

        conn.setUpdater(new MessageUpdater(this.messagesPanel));

        
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
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = ChatUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
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
        
        conn.connect();

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
        return inputField;
    }

    private JPanel createInputPanel() {
        inputField = new JTextField(10);

        JPanel tempInputPanel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        JTextField[] textFields = {inputField};
        addLabelTextRows(textFields, tempInputPanel);

        c.gridwidth = GridBagConstraints.REMAINDER; //last
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;

        tempInputPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Input"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return tempInputPanel;
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

    private static JPanel createMessagesPanel() {
        JEditorPane editorPane = createEditorPane();
        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        editorScrollPane.setMinimumSize(new Dimension(10, 10));


        JPanel chatPane = new JPanel(new GridLayout(1, 0));
        chatPane.add(editorScrollPane);
        chatPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Messages"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return chatPane;
    }

    private static void addLabelTextRows(JTextField[] textFields,
                                  Container container) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        int numLabels = textFields.length;

        for (int i = 0; i < numLabels; i++) {
            c.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            container.add(textFields[i], c);
        }
    }

    private static JEditorPane createEditorPane() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        /*
        java.net.URL helpURL = this.getClass().getResource(
                "TextSamplerDemoHelp.html");
        if (helpURL != null) {
            try {
                editorPane.setPage(helpURL);
            } catch (IOException e) {
                System.err.println("Attempted to read a bad URL: " + helpURL);
            }
        } else {
            System.err.println("Couldn't find file: TextSampleDemoHelp.html");
        }
        */

        return editorPane;
    }

}
