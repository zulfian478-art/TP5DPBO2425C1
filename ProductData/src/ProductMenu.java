import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductMenu extends JFrame {

    public static void main(String[] args) {
        // buat object window
        ProductMenu menu = new ProductMenu();

        // atur ukuran window
        menu.setSize(700, 600);

        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua produk
    private ArrayList<Product> listProduct;
    private Database database;

    private DefaultTableModel model;

    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField hargaField;
    private JTable productTable;

    private JButton cancelButton;
    private JComboBox<String> kategoriComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel namaLabel;
    private JLabel hargaLabel;
    private JLabel kategoriLabel;
    private JButton updateButton;
    private JButton addButton;
    private JProgressBar progressBar;
    private JTextField stokField;
    private JLabel stokLabel;

    // constructor
    public ProductMenu() {
        // inisialisasi listProduct
        listProduct = new ArrayList<>();

        //buat objek database
        database = new Database();

        model = new DefaultTableModel(new Object[]{"No", "ID Produk", "Nama", "Harga", "Kategori", "Stok"}, 0);
        productTable.setModel(model);
        setTable();

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        String[] kategotiData = {"???", "Elektronik", "Minuman", "Makanan", "Pakaian", "Alat Tulis"};
        kategoriComboBox.setModel((new DefaultComboBoxModel<>(kategotiData)));

        progressBar.setVisible(false);

        // ✅ Tombol ADD
        addButton.addActionListener(e -> {
            showLoadingBar();
            insertData();
        });

        // ✅ Tombol UPDATE
        updateButton.addActionListener(e -> {
            if (selectedIndex != -1) {
                showLoadingBar();
                updateData();
            } else {
                JOptionPane.showMessageDialog(null, "Pilih data yang ingin di-update terlebih dahulu!");
            }
        });

        // ✅ Tombol DELETE
        deleteButton.addActionListener(e -> {
            if (selectedIndex != -1) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Apakah kamu yakin ingin menghapus data ini?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus terlebih dahulu!");
            }
        });

        cancelButton.addActionListener(e -> clearForm());

        // isi listProduct
        populateList();

        // isi tabel produk
//        productTable.setModel(setTable());

        // ubah styling title
//        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
//        String[] kategotiData = { "???" , "Elektronik", "Minuman", "Makanan", "Pakaian", "Alat Tulis"};
//        kategoriComboBox.setModel((new DefaultComboBoxModel<>(kategotiData)));
//
//        // ====== INISIALISASI PROGRESS BAR ======
//        progressBar.setVisible(false);

        // Tombol ADD
//        addButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                showLoadingBar();
//                insertData();
//            }
//        });
//
//        // Tombol UPDATE
//        updateButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (selectedIndex != -1) {
//                    showLoadingBar();
//                    updateData();
//                } else {
//                    JOptionPane.showMessageDialog(null, "Pilih data yang ingin di-update terlebih dahulu!");
//                }
//            }
//        });
//
//        // saat tombol delete ditekan
//        deleteButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (selectedIndex != -1) {
//                    int confirm = JOptionPane.showConfirmDialog(
//                            null,
//                            "Apakah kamu yakin ingin menghapus data ini?",
//                            "Konfirmasi Hapus",
//                            JOptionPane.YES_NO_OPTION
//                    );
//
//                    if (confirm == JOptionPane.YES_OPTION) {
//                        deleteData();
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus terlebih dahulu!");
//                }
//            }
//        });

        // saat tombol cancel ditekan
//        cancelButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                clearForm();
//            }
//        });

        // saat salah satu baris tabel ditekan
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik

                selectedIndex = productTable.getSelectedRow();
                if (selectedIndex == -1) return;

                // simpan value textfield dan combo box
                String curId = productTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = productTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curHarga = productTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = productTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curStok = productTable.getModel().getValueAt(selectedIndex, 5).toString();

                // ubah isi textfield dan combo box
                idField.setText(curId);
                namaField.setText(curNama);
                hargaField.setText(curHarga);
                kategoriComboBox.setSelectedItem(curKategori);
                stokField.setText(curStok);

                // tampilkan button update dan delete
                updateButton.setVisible(true);
                deleteButton.setVisible(true);
            }
        });
    }

    // ====== FUNGSI PROGRESS BAR ======
    private void showLoadingBar() {
        progressBar.setVisible(true);
        progressBar.setValue(0);

        Thread thread = new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    progressBar.setValue(i);
                    Thread.sleep(10); // kecepatan animasi
                }
                Thread.sleep(300);
                progressBar.setVisible(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    // ====== TABEL ======
    public final DefaultTableModel setTable() {
        Object[] cols = { "No", "ID Produk", "Nama", "Harga", "Kategori", "stok"};
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM product");

            //isi tabel dengan hasil query
            int i = 0;
            while (resultSet != null && resultSet.next()){
                Object[] row = new Object[6];
                row[0] = i + 1;
                row[1] = resultSet.getString("id");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getString("harga");
                row[4] = resultSet.getString("kategori");
                row[5] = resultSet.getString("stok");

                tmp.addRow(row);
                i++;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        productTable.setModel(tmp);
        return tmp;
    }


    // ====== CRUD ======
    public void insertData() {
        try {
            String id = idField.getText();
            String nama = namaField.getText();
            double harga = Double.parseDouble(hargaField.getText());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int stok = Integer.parseInt(stokField.getText());

            // Validasi: Pastikan semua field terisi dan kategori bukan "???"
            if (id.isEmpty() || nama.isEmpty() || kategori.equals("???")) {
                JOptionPane.showMessageDialog(null, "Semua field harus diisi!");
                return;
            }

            //tambahkan data ke dalam database
            String sql = "INSERT INTO product (id, nama, harga, kategori, stok) VALUES ('"
                    + id + "', '" + nama + "', '" + harga + "', '" + kategori + "', '" + stok + "')";
            database.InsertUpdateDelete(sql);


            listProduct.add(new Product(id, nama, harga, kategori, stok));
            productTable.setModel(setTable());
            clearForm();


            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Harga dan stok harus berupa angka!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        try {
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            String hargaText = hargaField.getText().trim();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String stokText = stokField.getText().trim();

            if (id.isEmpty() || nama.isEmpty() || hargaText.isEmpty() || stokText.isEmpty() || kategori.equals("???")) {
                JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double harga = Double.parseDouble(hargaText);
            int stok = Integer.parseInt(stokText);

            if (harga < 0 || stok < 0) {
                JOptionPane.showMessageDialog(null, "Harga dan stok harus bernilai positif!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 🔧 Update data ke database
            String sql = "UPDATE product SET nama='" + nama + "', harga='" + harga + "', kategori='"
                    + kategori + "', stok='" + stok + "' WHERE id='" + id + "'";
            database.InsertUpdateDelete(sql);


            productTable.setModel(setTable());
            clearForm();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Harga dan stok harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String sql = "DELETE FROM product WHERE id='" + id + "'";
            database.InsertUpdateDelete(sql);


            productTable.setModel(setTable());
            clearForm();

            JOptionPane.showMessageDialog(null, "Data berhasil dihapus dari database!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus data.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }






    public void clearForm() {
        idField.setText("");
        namaField.setText("");
        hargaField.setText("");
        stokField.setText("");
        kategoriComboBox.setSelectedIndex(0);

        updateButton.setVisible(false);
        deleteButton.setVisible(false);
        selectedIndex = -1;
    }

    // ====== ISI LIST DUMMY ======
    private void populateList() {
        listProduct.add(new Product("P001", "Laptop Asus", 8500000.0, "Elektronik", 20));
        listProduct.add(new Product("P002", "Mouse Logitech", 350000.0, "Elektronik", 10));
        listProduct.add(new Product("P003", "Keyboard Mechanical", 750000.0, "Elektronik", 12));
        listProduct.add(new Product("P004", "Roti Tawar", 15000.0, "Makanan", 30));
        listProduct.add(new Product("P005", "Susu UHT", 12000.0, "Minuman", 17));
        listProduct.add(new Product("P006", "Kemeja Putih", 125000.0, "Pakaian", 20));
        listProduct.add(new Product("P007", "Celana Jeans", 200000.0, "Pakaian", 9));
        listProduct.add(new Product("P008", "Pensil 2B", 3000.0, "Alat Tulis", 8));
        listProduct.add(new Product("P009", "Buku Tulis", 8000.0, "Alat Tulis", 16));
        listProduct.add(new Product("P010", "Air Mineral", 5000.0, "Minuman", 39));
        listProduct.add(new Product("P011", "Smartphone Samsung", 4500000.0, "Elektronik", 14));
        listProduct.add(new Product("P012", "Kue Brownies", 25000.0, "Makanan", 25));
        listProduct.add(new Product("P013", "Jaket Hoodie", 180000.0, "Pakaian", 29));
        listProduct.add(new Product("P014", "Pulpen Gel", 5000.0, "Alat Tulis", 39));
        listProduct.add(new Product("P015", "Teh Botol", 8000.0, "Minuman", 11));
    }
}
