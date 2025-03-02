package org.manitech.forms;

import org.manitech.service.ExcelConsolidator;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * User : Manish K. Gupta
 */

public class CompletedForm extends JFrame {
    private JPanel completedFormPnl;
    private JTextArea logTxt;
    private JButton retryBtn;
    private JButton clsBtn;

    public CompletedForm(String folderPath) {
        setContentPane(completedFormPnl);
        setTitle("ExcelMerger: Completion");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        redirectLogs();
        //Q:\src\intellij\GstUtility\src\resources\gst\files
        ExcelConsolidator consolidator = new ExcelConsolidator();
        consolidator.consolidate(folderPath);
    }

    private void redirectLogs() {
        PrintStream printStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                logTxt.append(String.valueOf((char) b));
                logTxt.setCaretPosition(logTxt.getDocument().getLength()); // Auto-scroll
            }
        });

        // Redirect standard output and error streams
        System.setOut(printStream);
        System.setErr(printStream);
    }
}
