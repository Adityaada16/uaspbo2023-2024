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
import models.Peminjaman;
import models.Anggota;
import models.Inventaris;
import utils.DatabaseConnection;

public class PeminjamanController {

    public void addPeminjaman(Peminjaman peminjaman) {
        String sql = "INSERT INTO peminjaman (id_anggota, id_barang, tanggal_peminjaman, "
                + "tanggal_pengembalian) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, peminjaman.getIdAnggota());
            pstmt.setInt(2, peminjaman.getIdBarang());
            pstmt.setString(3, peminjaman.getTanggalPeminjaman().toString());
            pstmt.setString(4, peminjaman.getTanggalPengembalian().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePeminjaman(Peminjaman peminjaman) {
        String sql = "UPDATE peminjaman SET id_anggota = ?, id_barang = ?, tanggal_peminjaman = ?, "
                + "tanggal_pengembalian = ? WHERE id_peminjaman = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, peminjaman.getIdAnggota());
            pstmt.setInt(2, peminjaman.getIdBarang());
            pstmt.setString(3, peminjaman.getTanggalPeminjaman().toString());
            pstmt.setString(4, peminjaman.getTanggalPengembalian().toString());
            pstmt.setInt(5, peminjaman.getIdPeminjaman());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePeminjaman(int idPeminjaman) {
        String sql = "DELETE FROM peminjaman WHERE id_peminjaman = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPeminjaman);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Peminjaman getPeminjaman(int idPeminjaman) {
        String sql = "SELECT p.id_peminjaman, p.id_anggota, p.id_barang, p.tanggal_peminjaman, p.tanggal_pengembalian, " +
                     "a.nama AS nama_anggota, a.nim, a.prodi, " +
                     "i.nama_barang " +
                     "FROM peminjaman p " +
                     "JOIN anggota a ON p.id_anggota = a.id_anggota " +
                     "JOIN inventaris i ON p.id_barang = i.id_barang " +
                     "WHERE p.id_peminjaman = ?";
        Peminjaman peminjaman = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPeminjaman);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                peminjaman = new Peminjaman(
                    rs.getInt("id_peminjaman"),
                    rs.getInt("id_anggota"),
                    rs.getInt("id_barang"),
                    java.sql.Date.valueOf(rs.getString("tanggal_peminjaman")),
                    java.sql.Date.valueOf(rs.getString("tanggal_pengembalian")),
                    rs.getString("nama_anggota"),
                    rs.getString("nim"),
                    rs.getString("prodi"),
                    rs.getString("nama_barang")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return peminjaman;
    }

    public List<Peminjaman> getAllPeminjaman() {
        List<Peminjaman> peminjamanList = new ArrayList<>();
        String sql = "SELECT p.id_peminjaman, p.id_anggota, p.id_barang, p.tanggal_peminjaman, p.tanggal_pengembalian, " +
                     "a.nama AS nama_anggota, a.nim, a.prodi, " +
                     "i.nama_barang " +
                     "FROM peminjaman p " +
                     "JOIN anggota a ON p.id_anggota = a.id_anggota " +
                     "JOIN inventaris i ON p.id_barang = i.id_barang";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Peminjaman peminjaman = new Peminjaman(
                    rs.getInt("id_peminjaman"),
                    rs.getInt("id_anggota"),
                    rs.getInt("id_barang"),
                    java.sql.Date.valueOf(rs.getString("tanggal_peminjaman")),
                    java.sql.Date.valueOf(rs.getString("tanggal_pengembalian")),
                    rs.getString("nama_anggota"),
                    rs.getString("nim"),
                    rs.getString("prodi"),
                    rs.getString("nama_barang")
                );
                peminjamanList.add(peminjaman);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return peminjamanList;
    }
    
    public void validatePeminjamanData(String tanggalPinjamStr, String tanggalKembaliStr) throws IllegalArgumentException {
        if (tanggalPinjamStr.isEmpty()) {
            throw new IllegalArgumentException("Tanggal peminjaman tidak boleh kosong!");
        }
        if (tanggalKembaliStr.isEmpty()) {
            throw new IllegalArgumentException("Tanggal pengembalian tidak boleh kosong!");
        }

        String dateFormatRegex = "\\d{4}-\\d{2}-\\d{2}";
        if (!tanggalPinjamStr.matches(dateFormatRegex)) {
            throw new IllegalArgumentException("Format tanggal peminjaman tidak valid. Gunakan format yyyy-MM-dd.");
        }
        if (!tanggalKembaliStr.matches(dateFormatRegex)) {
            throw new IllegalArgumentException("Format tanggal pengembalian tidak valid. Gunakan format yyyy-MM-dd.");
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

                document.add(new Paragraph("Data Peminjaman"));
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
