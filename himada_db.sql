-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 16, 2024 at 12:18 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

-- Hapus atau komentari perintah-perintah berikut
-- SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
-- START TRANSACTION;
-- SET time_zone = "+00:00";

-- Database: `himada_db`

-- Table structure for table `anggota`
CREATE TABLE `anggota` (
  `id_anggota` INTEGER PRIMARY KEY AUTOINCREMENT,
  `nama` varchar(100) NOT NULL,
  `nim` varchar(20) NOT NULL UNIQUE,
  `prodi` varchar(50) NOT NULL,
  `jenis_kelamin` TEXT CHECK( `jenis_kelamin` IN ('L','P') ) NOT NULL,
  `alamat_kos` text DEFAULT NULL,
  `alamat_asal` text DEFAULT NULL,
  `angkatan` int DEFAULT NULL
);

-- Dumping data for table `anggota`
INSERT INTO `anggota` (`id_anggota`, `nama`, `nim`, `prodi`, `jenis_kelamin`, `alamat_kos`, `alamat_asal`, `angkatan`) VALUES
(1, 'Nazwa Aditya Daniswara', '222212788', 'DIV Komputasi Statistik', 'L', 'Bidaca Cina', 'Bantul', 64),
(3, 'Aulia Zahra Rahmah', '112212524', 'DIII Statistika', 'P', 'Bidara Cina', 'Bantul', 64),
(5, 'Altytan Marzherega Asmoro', '212212487', 'DIV Statistika', 'L', 'Bidara Cina', 'Kota Yogyakarta', 64),
(7, 'Nadya Noor Hastin', '212212781', 'DIV Statistika', 'P', 'Bidara Cina', 'Sleman', 64),
(8, 'Fauzan Faris Sagita', '212212606', 'DIV Statistika', 'L', 'Bidara Cina', 'Sleman', 64),
(11, 'Very Dwi Prasetya', '222212907', 'DIV Komputasi Statistik', 'L', 'Bidara Cina', 'Sleman', 64),
(12, 'Okky Nurul Fadhilah', '212212811', 'DIV Statistika', 'P', 'Bidara Cina', 'Kulon Progo', 64),
(13, 'Iben Sebastian', '212212655', 'DIV Statistika', 'L', 'Bidara Cina', 'Bantul', 64),
(14, 'Lutvia Rahma Kumala', '222212708', 'DIV Komputasi Statistik', 'P', 'Bidara Cina', 'Bantul', 64),
(15, 'Puji Laila Maharni', '212212818', 'DIV Statistika', 'P', 'Bidara Cina', 'Kulon Progo', 64),
(16, 'Imam Fathoni Arufi', '212212662', 'DIV Statistika', 'L', 'Bidara Cina', 'Sleman', 64),
(17, 'Fakhrizal Akbar Muhammad', '222212595', 'DIV Komputasi Statistik', 'L', 'Bidara Cina', 'Sleman', 64),
(18, 'Arista Ika Cahyarani', '212212516', 'DIV Statistika', 'P', 'Bidara Cina', 'Sleman', 64),
(19, 'Nadia Lutfi Meilawati', '212212779', 'DIV Statistika', 'P', 'Bidara Cina', 'Bantul', 64),
(20, 'Antonius Bagas Sunu Wiguna Ardy', '222212509', 'DIV Komputasi Statistik', 'L', 'Bidara Cina', 'Sleman', 64),
(21, 'Aulia Zulfa Kurniawan', '222212525', 'DIV Komputasi Statistik', 'P', 'Bidara Cina', 'Purworejo', 64);

-- Table structure for table `inventaris`
CREATE TABLE `inventaris` (
  `id_barang` INTEGER PRIMARY KEY AUTOINCREMENT,
  `nama_barang` varchar(100) NOT NULL,
  `quantity` int NOT NULL,
  `keterangan` text DEFAULT NULL
);

-- Dumping data for table `inventaris`
INSERT INTO `inventaris` (`id_barang`, `nama_barang`, `quantity`, `keterangan`) VALUES
(1, 'Jarik', 5, 'Warna coklat'),
(2, 'Gunting', 4, ''),
(3, 'Staples', 3, 'ukuran sedang'),
(4, 'Kebaya', 2, 'Size XL dan L'),
(5, 'Surjan', 1, 'Size XL'),
(6, 'Cutter', 3, ''),
(7, 'Selop', 2, 'Size 38 dan 41');

-- Table structure for table `keuangan`
CREATE TABLE `keuangan` (
  `id_transaksi` INTEGER PRIMARY KEY AUTOINCREMENT,
  `tanggal` date NOT NULL,
  `deskripsi` text DEFAULT NULL,
  `tipe` text CHECK( `tipe` IN ('pemasukan','pengeluaran') ) NOT NULL,
  `jumlah` decimal(15,2) NOT NULL
);

-- Dumping data for table `keuangan`
INSERT INTO `keuangan` (`id_transaksi`, `tanggal`, `deskripsi`, `tipe`, `jumlah`) VALUES
(1, '2024-06-13', 'Saldo awal', 'pemasukan', 5000000.00),
(3, '2024-06-13', 'Kas', 'pemasukan', 800000.00),
(4, '2024-06-14', 'Pisbut', 'pengeluaran', 2000000.00),
(5, '2024-06-13', 'Iuran', 'pemasukan', 1000000.00),
(6, '2024-06-16', 'Dies Natalies', 'pengeluaran', 3000000.00);

-- Table structure for table `peminjaman`
CREATE TABLE `peminjaman` (
  `id_peminjaman` INTEGER PRIMARY KEY AUTOINCREMENT,
  `id_anggota` int NOT NULL,
  `id_barang` int NOT NULL,
  `tanggal_peminjaman` date NOT NULL,
  `tanggal_pengembalian` date DEFAULT NULL,
  FOREIGN KEY (`id_anggota`) REFERENCES `anggota` (`id_anggota`) ON DELETE CASCADE,
  FOREIGN KEY (`id_barang`) REFERENCES `inventaris` (`id_barang`) ON DELETE CASCADE
);

-- Dumping data for table `peminjaman`
INSERT INTO `peminjaman` (`id_peminjaman`, `id_anggota`, `id_barang`, `tanggal_peminjaman`, `tanggal_pengembalian`) VALUES
(2, 1, 3, '2024-06-13', '2024-06-16'),
(3, 20, 2, '2024-06-13', '2024-06-17'),
(4, 13, 1, '2024-06-13', '2024-06-14'),
(5, 15, 1, '2024-06-13', '2024-06-14'),
(6, 15, 4, '2024-06-13', '2024-06-14'),
(7, 12, 6, '2024-06-16', '2024-06-17'),
(8, 12, 2, '2024-06-16', '2024-06-17');

-- Table structure for table `users`
CREATE TABLE `users` (
  `user_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `name` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL
);

-- Dumping data for table `users`
INSERT INTO `users` (`user_id`, `name`, `username`, `password`) VALUES
(1, 'admin', 'admin', '21232f297a57a5a743894a0e4a801fc3'),
(2, 'Nazwa Aditya Daniswara', 'Aditya', 'e1b64ce40f59dd357805ffd4aef5feac'),
(3, 'tess', 'tes', 'b93939873fd4923043b9dec975811f66');

-- Constraints for table `peminjaman`
-- FOREIGN KEY constraints already added during table creation

-- No need for COMMIT as SQLite auto-commits
