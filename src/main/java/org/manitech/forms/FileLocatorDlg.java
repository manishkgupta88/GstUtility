package org.manitech.forms;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.manitech.service.ExcelConsolidator;
import org.manitech.util.Helper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

/**
 * User : Manish K. Gupta
 */

public class FileLocatorDlg extends JFrame {

    private JPanel fileLocatorDlgPane;
    private JLabel folderLbl;
    private JPanel btnPbl;
    private JButton browseBtn;
    private JButton processBtn;
    private JButton clearBtn;
    private JTextField folderPathTxt;
    private JPanel pathPnl;
    private JList fileList;
    private JScrollPane listPnl;
    private JLabel outPathLbl;
    private JTextField outputPathTxt;

    public FileLocatorDlg() {
        setContentPane(fileLocatorDlgPane);
        setTitle("ExcelMerger Folder browser");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        //fileList.setVisible(false);
        fileList.setEnabled(false);
        folderPathTxt.setEnabled(false);
        outputPathTxt.setEnabled(false);
        browseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFolder();
            }
        });
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                folderPathTxt.setText(null);
                outputPathTxt.setText(null);
                folderPathTxt.setEnabled(false);
                fileList.setListData(new Vector<>());
            }
        });
        processBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String folderPath = folderPathTxt.getText();
                if (StringUtils.isEmpty(folderPath)) {
                    JOptionPane.showMessageDialog(null, "Folder path empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                dispose();
//                new CompletedForm(folderPath);
                //Q:\src\intellij\GstUtility\src\resources\gst\files
                try {
                    ExcelConsolidator consolidator = new ExcelConsolidator();
                    consolidator.consolidate(folderPath);
                    JOptionPane.showMessageDialog(null, "File created Successfully", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ExceptionUtils.getStackTrace(ex), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void selectFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a Folder");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Only allow directories
        fileChooser.setAcceptAllFileFilterUsed(false); // Disable "All Files" option

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File folder = fileChooser.getSelectedFile();
            folderPathTxt.setText(folder.getAbsolutePath());
            outputPathTxt.setText(Helper.getOutputPath(folder.getAbsolutePath()));
            fileList.setListData(folder.listFiles());
            fileList.setVisible(true);
        }
    }
}
