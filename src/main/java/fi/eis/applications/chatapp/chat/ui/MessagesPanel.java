package fi.eis.applications.chatapp.chat.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MessagesPanel extends JPanel {
    private MessagesPanel(LayoutManager layout) {
        super(layout);
        messagesEditorPane = createEditorPane();
    }
    private MessagesPanel(LayoutManager2 layout) {
        super(layout);
        messagesEditorPane = createEditorPane();
    }
    private JEditorPane messagesEditorPane;
    
    public JEditorPane getEditorPane() {
        return this.messagesEditorPane;
    }
    
    static MessagesPanel createMessagesPanel() {
        MessagesPanel chatPane = new MessagesPanel(new GridLayout(1, 0));

        JScrollPane editorScrollPane = new JScrollPane(chatPane.getEditorPane());
        editorScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        editorScrollPane.setMinimumSize(new Dimension(10, 10));

        chatPane.add(editorScrollPane);
        chatPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Messages"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return chatPane;
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
