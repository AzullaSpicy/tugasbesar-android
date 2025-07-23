package com.example.tugasbesar;

public class Mahasiswa {
    private String id;
    private String nama;
    private String nim;
    private String jurusan;
    private String kelas; // ✅ Ubah jadi String

    public Mahasiswa() {
    }

    public Mahasiswa(String id, String nama, String nim, String jurusan, String kelas) {
        this.id = id;
        this.nama = nama;
        this.nim = nim;
        this.jurusan = jurusan;
        this.kelas = kelas;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public String getJurusan() {
        return jurusan;
    }

    public String getKelas() {
        return kelas;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }
}
