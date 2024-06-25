package controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import models.Keuangan;
import utils.DatabaseConnection;

public class KeuanganController {

    public void addTransaksi(Keuangan transaksi) {
        String sql = "INSERT INTO keuangan (tanggal, deskripsi, tipe, jumlah) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaksi.getTanggal().toString());
            pstmt.setString(2, transaksi.getDeskripsi());
            pstmt.setString(3, transaksi.getTipe());
            pstmt.setDouble(4, transaksi.getJumlah());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTransaksi(Keuangan transaksi) {
        String sql = "UPDATE keuangan SET tanggal = ?, deskripsi = ?, tipe = ?, jumlah = ? WHERE id_transaksi = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaksi.getTanggal().toString());
            pstmt.setString(2, transaksi.getDeskripsi());
            pstmt.setString(3, transaksi.getTipe());
            pstmt.setDouble(4, transaksi.getJumlah());
            pstmt.setInt(5, transaksi.getIdTransaksi());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransaksi(int idTransaksi) {
        String sql = "DELETE FROM keuangan WHERE id_transaksi = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idTransaksi);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Keuangan getTransaksi(int idTransaksi) {
        String sql = "SELECT * FROM keuangan WHERE id_transaksi = ?";
        Keuangan transaksi = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idTransaksi);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                transaksi = new Keuangan(
                    rs.getInt("id_transaksi"),
                    java.sql.Date.valueOf(rs.getString("tanggal")),
                    rs.getString("deskripsi"),
                    rs.getString("tipe"),
                    rs.getDouble("jumlah")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaksi;
    }

    public List<Keuangan> getAllTransaksi() {
        List<Keuangan> transaksiList = new ArrayList<>();
        String sql = "SELECT * FROM keuangan";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Keuangan transaksi = new Keuangan(
                    rs.getInt("id_transaksi"),
                    java.sql.Date.valueOf(rs.getString("tanggal")),
                    rs.getString("deskripsi"),
                    rs.getString("tipe"),
                    rs.getDouble("jumlah")
                );
                transaksiList.add(transaksi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaksiList;
    }
    
    public void validateTransaksiData(String tanggalStr, String desc, String jumlahStr) throws IllegalArgumentException {
        if (tanggalStr.isEmpty()) {
            throw new IllegalArgumentException("Tanggal tidak boleh kosong!");
        }
        
        try {
            java.sql.Date.valueOf(tanggalStr); // Validasi format tanggal
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Format tanggal tidak valid! Gunakan format YYYY-MM-DD.");
        }
        
        if (jumlahStr == null || jumlahStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Jumlah tidak boleh kosong!");
        }
        
        try {
            double jumlah = Double.parseDouble(jumlahStr);
            if (jumlah <= 0) {
                throw new IllegalArgumentException("Jumlah harus lebih dari nol!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Jumlah harus berupa angka!");
        }

        if (desc.isEmpty()) {
            throw new IllegalArgumentException("Deskripsi tidak boleh kosong!");
        }
    }
    
    public void exportToCsv(TableModel tableModel, JFrame parentFrame) {
        int rowCount = tableModel.getRowCount();
        int columnCount = tableModel.getColumnCount();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As CSV");
        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".csv")) {
                filePath += ".csv";
            }

            try (FileWriter fileWriter = new FileWriter(filePath);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

                for (int i = 0; i < columnCount; i++) {
                    bufferedWriter.write(tableModel.getColumnName(i));
                    if (i < columnCount - 1) {
                        bufferedWriter.write(",");
                    }
                }
                bufferedWriter.newLine();

                for (int row = 0; row < rowCount; row++) {
                    for (int col = 0; col < columnCount; col++) {
                        Object cellValue = tableModel.getValueAt(row, col);
                        bufferedWriter.write(cellValue.toString());
                        if (col < columnCount - 1) {
                            bufferedWriter.write(",");
                        }
                    }
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();
                JOptionPane.showMessageDialog(parentFrame, "Data berhasil diekspor ke " + filePath);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Terjadi kesalahan saat menyimpan file: " 
                        + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void exportToPdf(TableModel tableModel, JFrame parentFrame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As PDF");
        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".pdf")) {
                filePath += ".pdf";
            }

            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                document.add(new Paragraph("Data Transaksi"));
                document.add(new Paragraph(" "));

                int columnCount = tableModel.getColumnCount();
                PdfPTable pdfTable = new PdfPTable(columnCount);

                for (int i = 0; i < columnCount; i++) {
                    pdfTable.addCell(tableModel.getColumnName(i));
                }

                int rowCount = tableModel.getRowCount();
                for (int row = 0; row < rowCount; row++) {
                    for (int col = 0; col < columnCount; col++) {
                        Object cellValue = tableModel.getValueAt(row, col);
                        pdfTable.addCell(cellValue.toString());
                    }
                }

                document.add(pdfTable);
                document.close();
                JOptionPane.showMessageDialog(parentFrame, "Data berhasil diekspor ke " + filePath);

            } catch (FileNotFoundException | DocumentException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Terjadi kesalahan saat menyimpan file: " 
                        + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
