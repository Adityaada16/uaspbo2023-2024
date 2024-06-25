package controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Inventaris;
import utils.DatabaseConnection;

public class InventarisController {

    public void addBarang(Inventaris barang) {
        String sql = "INSERT INTO inventaris (nama_barang, quantity, keterangan) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, barang.getNamaBarang());
            pstmt.setInt(2, barang.getQuantity());
            pstmt.setString(3, barang.getKeterangan());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBarang(Inventaris barang) {
        String sql = "UPDATE inventaris SET nama_barang = ?, quantity = ?, keterangan = ? WHERE id_barang = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, barang.getNamaBarang());
            pstmt.setInt(2, barang.getQuantity());
            pstmt.setString(3, barang.getKeterangan());
            pstmt.setInt(4, barang.getIdBarang());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBarang(int idBarang) {
        String sql = "DELETE FROM inventaris WHERE id_barang = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idBarang);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Inventaris getBarang(int idBarang) {
        String sql = "SELECT * FROM inventaris WHERE id_barang = ?";
        Inventaris barang = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idBarang);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                barang = new Inventaris(
                    rs.getInt("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("quantity"),
                    rs.getString("keterangan")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return barang;
    }

    public List<Inventaris> getAllBarang() {
        List<Inventaris> barangList = new ArrayList<>();
        String sql = "SELECT * FROM inventaris";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Inventaris barang = new Inventaris(
                    rs.getInt("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("quantity"),
                    rs.getString("keterangan")
                );
                barangList.add(barang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return barangList;
    }
    
     public List<Inventaris> searchByName(String keyword) {
        List<Inventaris> barangList = new ArrayList<>();
        String sql = "SELECT * FROM inventaris WHERE nama_barang LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Mengatur parameter untuk pencarian
            String searchKeyword = "%" + keyword + "%";
            pstmt.setString(1, searchKeyword);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Inventaris barang = new Inventaris(
                    rs.getInt("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("quantity"),
                    rs.getString("keterangan")
                );
                barangList.add(barang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return barangList;
    }
     
     public boolean validateInventarisData(String namaBarang, String quantityStr) throws IllegalArgumentException {
        if (namaBarang.isEmpty()) {
            throw new IllegalArgumentException("Nama barang tidak boleh kosong!");
        }

        if (quantityStr.isEmpty()) {
            throw new IllegalArgumentException("Quantity tidak boleh kosong!");
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity harus lebih dari nol!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Quantity harus berupa angka!");
        }

        return true;
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

                document.add(new Paragraph("Data Inventaris"));
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