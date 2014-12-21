package fi.eis.applications.chatapp.chat.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager2;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputPanel extends JPanel {
    private InputPanel(LayoutManager2 layout) {
        super(layout);
        this.inputField = new JTextField(10);
    }
    private final JTextField inputField;

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

    public JTextField getInputField() {
        return this.inputField;
    }

}
