package org.manitech.forms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User : Manish K. Gupta
 */

public class IntroForm extends JFrame {
    private JPanel contentPane;
    private JLabel introLbl;
    private JLabel orgLbl;

    public IntroForm(String version) {
        setContentPane(contentPane);
        setSize(300, 250);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setVisible(true);
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new FileLocatorDlg();
            }
        });
        orgLbl.setText(orgLbl.getText() + ", Version:" + version);
        timer.setRepeats(false);
        timer.start();
    }
}
