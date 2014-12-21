package fi.eis.applications.chatapp.chat.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager2;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import fi.eis.applications.chatapp.chat.actions.ChattingConnection;
import fi.eis.libraries.di.SimpleLogger;

public class InputPanel extends JPanel {
    private final SimpleLogger logger = new SimpleLogger(this.getClass());
    
    private InputPanel(LayoutManager2 layout) {
        super(layout);
        this.inputField = createInputField();
    }
    private JTextField createInputField() {
        JTextField textField = new JTextField(10);
        textField.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    submit();
                }
            }

            @Override
            public void keyReleased(KeyEvent arg0) {}

            @Override
            public void keyTyped(KeyEvent arg0) {}
        });
        return textField;
    }
    protected void submit() {
        final String text = this.inputField.getText();
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                logger.debug("doInBackGround: "+ text);
                connection.sendMessage(text);
                return null;
            }
            @Override
            public void done() {
                logger.debug("done");
            }
            
        };
        worker.execute();

        this.inputField.setText("");
    }
    private final JTextField inputField;
    private ChattingConnection connection;

    static InputPanel createInputPanel() {

        InputPanel tempInputPanel = new InputPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        JTextField[] textFields = { tempInputPanel.getInputField() };
        addLabelTextRows(textFields, tempInputPanel);

        c.gridwidth = GridBagConstraints.REMAINDER; // last
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;

        tempInputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Input"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        

        return tempInputPanel;
    }

    private JTextField getInputField() {
        return this.inputField;
    }
    private static void addLabelTextRows(JTextField[] textFields,
            Container container) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        int numLabels = textFields.length;

        for (int i = 0; i < numLabels; i++) {
            c.gridwidth = GridBagConstraints.REMAINDER; // end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            container.add(textFields[i], c);
        }
    }

    public void setChattingConnection(ChattingConnection conn) {
        this.connection = conn;
    }

    public void focusOnInputField() {
        this.getInputField().requestFocusInWindow();
    }

}
