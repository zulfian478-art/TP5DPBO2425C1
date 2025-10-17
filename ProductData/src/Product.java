import java.io.Serializable;

public class Product {
    private String id;
    private String nama;
    private double harga;
    private String kategori;
    private int stok;

    public Product(String id, String nama, double harga, String kategori, int stok) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
        this.stok = stok;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setStok(int stok) { this.stok = stok;}



    public String getId() {
        return this.id;
    }

    public String getNama() {
        return this.nama;
    }

    public double getHarga() {
        return this.harga;
    }

    public String getKategori() {
        return this.kategori;
    }

    public int getStok() {return  this.stok;}

    // === Untuk debugging (opsional tapi berguna) ===
    @Override
    public String toString() {
        return String.format("[%s] %s (%.0f) - %s, stok: %d", id, nama, harga, kategori, stok);
    }

}