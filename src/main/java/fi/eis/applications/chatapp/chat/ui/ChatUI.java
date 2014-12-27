package fi.eis.applications.chatapp.chat.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fi.eis.applications.chatapp.chat.actions.ChattingConnection;
import fi.eis.applications.chatapp.chat.actions.ChattingConnectionFactory;
import fi.eis.applications.chatapp.chat.actions.MessageUpdater;
import fi.eis.applications.chatapp.chat.actions.impl.MessageUpdaterImpl;
import fi.eis.applications.chatapp.configuration.Configuration;
import fi.eis.applications.chatapp.login.actions.LoginHandler;
import fi.eis.applications.chatapp.login.actions.RoomsProvider;
import fi.eis.applications.chatapp.login.types.ChatRoom;
import fi.eis.applications.chatapp.login.ui.LoginUI;
import fi.eis.libraries.di.Inject;
import fi.eis.libraries.di.SimpleLogger;
import fi.eis.libraries.di.SimpleLogger.LogLevel;

/**
 * Created with IntelliJ IDEA.
 * Date: 3.11.2014
 * Time: 22:53
 * 
 * @author eis
 */
public class ChatUI extends JFrame {

    private final SimpleLogger logger = new SimpleLogger(this.getClass());
    
    private final UserListPanel userListPanel;
    private final MessagesPanel messagesPanel;
    private final InputPanel inputPanel;

    private final Configuration configuration;
    private final LoginHandler loginHandler;
    private final RoomsProvider roomsProvider;
    private final ChattingConnectionFactory chatConnectionFactory;

    // these are mutable
    private ChattingConnection connectionParameters;
    private MessageUpdater messageUpdater;
    
    @Inject
    public ChatUI(LoginHandler loginHandler,
            RoomsProvider roomsProvider,
            ChattingConnectionFactory chatConnectionFactory,
            Configuration configuration) {

        this.configuration = configuration;
        this.loginHandler = loginHandler;
        this.roomsProvider = roomsProvider;
        this.chatConnectionFactory = chatConnectionFactory;

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 500));

        this.userListPanel = UserListPanel.createUserListPanel();

        this.messagesPanel = MessagesPanel.createMessagesPanel();

        this.inputPanel = InputPanel.createInputPanel();

        JMenuBar menuBar = createMenu();

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
                if (ChatUI.this.connectionParameters != null) {
                    ChatUI.this.connectionParameters.cancel();
                }
                if (ChatUI.this.messageUpdater != null) {
                    ChatUI.this.messageUpdater.close();
                }
                ChatUI.this.configuration.save();
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setJMenuBar(menuBar);
        add(panel);
    }
    
    public void setLogLevel(LogLevel level) {
        this.logger.setLogLevel(level);
    }

    private void connect(ChattingConnection connectionParameters) {
        try {
            this.connectionParameters = connectionParameters;
            // connect to chat
            this.connectionParameters.connect();

            ChatRoom targetRoom = this.roomsProvider.getRoomById(
                    this.connectionParameters.getConnectionParameters().getRoomId());
            this.messageUpdater = new MessageUpdaterImpl(
                    messagesPanel.getEditorPane(),
                    userListPanel.getUserList(),
                    targetRoom.getRoomName().toLowerCase(Locale.US)
                );
            boolean logAutomatically = this.configuration.getLogAutomatically();
            this.messageUpdater.setLogToFile(logAutomatically);

            this.connectionParameters.setUpdater(this.messageUpdater);
            this.inputPanel.setChattingConnection(this.connectionParameters);

            // execute background connections
            this.connectionParameters.execute();
    
        } catch (RuntimeException ex) {
            this.connectionParameters.cancel();
            JOptionPane.showMessageDialog(this, "Exception: " + ex.getMessage());
            throw ex;
        }
    }
    public void display() {
        // pack makes size correspond to content
        pack();
        // Center the window
        setLocationRelativeTo(null);
        // Set focus
        focusOnInput();
        // Show
        setVisible(true);
    }
    private void focusOnInput() {
        inputPanel.focusOnInputField();
    }

    private JMenuBar createMenu() {
        //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();

        // chat menu
        {
            JMenu chatMenu = new JMenu("Chat");
            chatMenu.setMnemonic(KeyEvent.VK_C);
            
            JMenuItem loginItem = new JMenuItem("Login...");
            
            loginItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    final LoginUI loginUI = LoginUI.createGUI(
                            loginHandler,
                            roomsProvider,
                            chatConnectionFactory
                    );

                    //loginUI.display();

                    int result = JOptionPane.showConfirmDialog(ChatUI.this, loginUI, null, JOptionPane.OK_CANCEL_OPTION);
                    //ChattingConnection connection = loginUI.display();
                    if (result == JOptionPane.OK_OPTION) {
                        ChattingConnection connectionParameters = loginUI.getConnectionParameters();
                        ChatUI.this.connect(connectionParameters);
                    }
                }
                
            });
            
            chatMenu.add(loginItem);
            menuBar.add(chatMenu);
        }
        // end chat menu
        // begin messages menu
        {
            JMenu messagesMenu = new JMenu("Messages");
            messagesMenu.setMnemonic(KeyEvent.VK_M);
            JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("Automatic logging");
            boolean logAutomatically = this.configuration.getLogAutomatically();
            cbMenuItem.setState(logAutomatically);
    
            cbMenuItem.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    boolean logAutomatically = (e.getStateChange() == ItemEvent.SELECTED);
                    if (ChatUI.this.messageUpdater != null) {
                        ChatUI.this.messageUpdater.setLogToFile(logAutomatically);
                    }
                    ChatUI.this.configuration.setLogAutomatically(logAutomatically);
                }
            });
    
            messagesMenu.add(cbMenuItem);
            menuBar.add(messagesMenu);
        }
        // end messages menu
        // window menu
        {
            JMenu windowMenu = new JMenu("Window");
            windowMenu.setMnemonic(KeyEvent.VK_W);
    
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
    
            windowMenu.add(cbMenuItem);
            menuBar.add(windowMenu);
        }
        // end window menu
        return menuBar;
    }

}
