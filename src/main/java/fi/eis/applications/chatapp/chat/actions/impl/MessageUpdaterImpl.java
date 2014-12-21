package fi.eis.applications.chatapp.chat.actions.impl;

import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import fi.eis.applications.chatapp.chat.actions.MessageUpdater;

public class MessageUpdaterImpl implements MessageUpdater {

    private final JEditorPane target;
    public MessageUpdaterImpl(JEditorPane messagesTarget) {
        if (messagesTarget == null) {
            throw new IllegalArgumentException("target must be defined");
        }
        this.target = messagesTarget;
    }
    
    // http://www.java2s.com/Code/JavaAPI/javax.swing.text/DocumentinsertStringintoffsetStringstrAttributeSeta.htm
    // http://stackoverflow.com/questions/5133240/add-html-content-to-document-associated-with-jtextpane
    @Override
    public void publishMessage(String message) {
        HTMLDocument doc = (HTMLDocument)target.getDocument();
        HTMLEditorKit kit = (HTMLEditorKit)target.getEditorKit();
        try {
            kit.insertHTML(doc, doc.getLength(), message, 0, 0, null);
        } catch (BadLocationException| IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
