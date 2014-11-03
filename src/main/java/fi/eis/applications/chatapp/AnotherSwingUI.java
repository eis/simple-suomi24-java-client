package fi.eis.applications.chatapp;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;              //for layout managers and more
import java.awt.event.*;        //for action events

import java.net.URL;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Blackstorm
 * Date: 3.11.2014
 * Time: 22:53
 * To change this template use File | Settings | File Templates.
 */
public class AnotherSwingUI extends JPanel
        implements ActionListener {

        private String newline = "\n";

        public AnotherSwingUI() {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(500,500));

            //Create a regular text field.
            JTextField textField = new JTextField(10);
            textField.addActionListener(this);

            //Lay out the text controls and the labels.
            JPanel textControlsPane = new JPanel();
            GridBagLayout gridbag = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            textControlsPane.setLayout(gridbag);

            JTextField[] textFields = {textField};
            addLabelTextRows(textFields, gridbag, textControlsPane);

            c.gridwidth = GridBagConstraints.REMAINDER; //last
            c.anchor = GridBagConstraints.WEST;
            c.weightx = 1.0;
            //textControlsPane.add(actionLabel, c);
            textControlsPane.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createTitledBorder("Input"),
                            BorderFactory.createEmptyBorder(5,5,5,5)));

            //Create an editor pane.
            JEditorPane editorPane = createEditorPane();
            JScrollPane editorScrollPane = new JScrollPane(editorPane);
            editorScrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            editorScrollPane.setHorizontalScrollBarPolicy(
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            );
            editorScrollPane.setPreferredSize(new Dimension(250, 145));
            editorScrollPane.setMinimumSize(new Dimension(10, 10));


            JPanel rightPane = new JPanel(new GridLayout(1,0));
            rightPane.add(editorScrollPane);
            rightPane.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Chat"),
                    BorderFactory.createEmptyBorder(5,5,5,5)));


            //Put everything together.
            JPanel leftPane = new JPanel(new BorderLayout());
            leftPane.add(rightPane,//areaScrollPane,
                    BorderLayout.CENTER);
            leftPane.add(textControlsPane,
                    BorderLayout.PAGE_END);

            add(leftPane, BorderLayout.CENTER);
            //add(rightPane, BorderLayout.LINE_END);
        }

        private void addLabelTextRows(JTextField[] textFields,
                                      GridBagLayout gridbag,
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

        public void actionPerformed(ActionEvent e) {
            /*
            String prefix = "You typed \"";
            if (textFieldString.equals(e.getActionCommand())) {
                JTextField source = (JTextField)e.getSource();
                actionLabel.setText(prefix + source.getText() + "\"");
            } else if (passwordFieldString.equals(e.getActionCommand())) {
                JPasswordField source = (JPasswordField)e.getSource();
                actionLabel.setText(prefix + new String(source.getPassword())
                        + "\"");
            } else if (buttonString.equals(e.getActionCommand())) {
                Toolkit.getDefaultToolkit().beep();
            }
            */
        }
        private JEditorPane createEditorPane() {
            JEditorPane editorPane = new JEditorPane();
            editorPane.setEditable(false);
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

            return editorPane;
        }

        /** Returns an ImageIcon, or null if the path was invalid. */
        protected static ImageIcon createImageIcon(String path,
                                                   String description) {
            java.net.URL imgURL = AnotherSwingUI.class.getResource(path);
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
         */
        private static void createAndShowGUI() {
            //Create and set up the window.
            JFrame frame = new JFrame("TextSamplerDemo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //Add content to the window.
            frame.add(new AnotherSwingUI());

            // pack makes size correspond to content
            frame.pack();
            // Center the window
            frame.setLocationRelativeTo(null);
            // Show
            frame.setVisible(true);
        }

        public static void main(String[] args) {
            //Schedule a job for the event dispatching thread:
            //creating and showing this application's GUI.
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    //Turn off metal's use of bold fonts
                    UIManager.put("swing.boldMetal", Boolean.FALSE);
                    createAndShowGUI();
                }
            });
        }

}
