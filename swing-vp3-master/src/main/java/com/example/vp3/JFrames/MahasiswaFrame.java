package com.example.vp3.JFrames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class MahasiswaFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    private Connection connect() {
        String url = "jdbc:mysql://localhost/mahasiswaku?user=root&password=";
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
    }

    public MahasiswaFrame() {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame jFrame = new JFrame("Aplikasi Mahasiswa");
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nama");
        tableModel.addColumn("NIM");

        refreshTableData();

        table = new JTable(tableModel);
        JScrollPane pane = new JScrollPane(table);

        JButton button = new JButton("Create Mahasiswa");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateMahasiswaFrame(MahasiswaFrame.this);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.add(button);
        panel.add(pane);

        jFrame.getContentPane().add(panel, BorderLayout.CENTER);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setBounds(100, 100, 500, 400);
    }

    public void refreshTableData() {
        tableModel.setRowCount(0); 

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM mahasiswa");
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt(1);
                String nama = rs.getString(2);
                String nim = rs.getString(3);
                tableModel.addRow(new Object[] { id, nama, nim });
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}