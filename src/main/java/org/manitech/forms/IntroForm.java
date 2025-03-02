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

    public IntroForm() {
        setContentPane(contentPane);
        setSize(300, 250);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setVisible(true);
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new FileLocatorDlg();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
