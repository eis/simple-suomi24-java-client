package fi.eis.applications.chatapp.chat.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class MessagesPanel extends JPanel {
    private MessagesPanel(LayoutManager layout) {
        super(layout);
        messagesEditorPane = createHTMLEditorPane();
    }
    private MessagesPanel(LayoutManager2 layout) {
        super(layout);
        messagesEditorPane = createHTMLEditorPane();
    }
    private JEditorPane messagesEditorPane;

    public JEditorPane getEditorPane() {
        return messagesEditorPane;
    }
    public HTMLEditorKit getEditorKit() {
        return (HTMLEditorKit)getEditorPane().getEditorKit();
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


    private static JEditorPane createHTMLEditorPane() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        HTMLEditorKit kit = new HTMLEditorKit();
        HTMLDocument doc = new HTMLDocument();
        // this is to avoid CharacterSetChangedException on new data
        doc.putProperty("IgnoreCharsetDirective",new Boolean(true));
        editorPane.setEditorKit(kit);
        editorPane.setDocument(doc);
        return editorPane;
    }

}
