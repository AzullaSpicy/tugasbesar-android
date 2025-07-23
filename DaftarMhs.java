package com.example.tugasbesar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DaftarMhs extends AppCompatActivity {

    // Deklarasi komponen tampilan dan data
    private RecyclerView recyclerView; // untuk menampilkan daftar mahasiswa
    private MahasiswaAdapter adapter;  // adapter untuk menghubungkan data ke RecyclerView
    private ArrayList<Mahasiswa> listMahasiswa; // daftar data mahasiswa
    private FloatingActionButton fab; // tombol tambah data
    private Button btnLogout;         // tombol logout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_mhs);

        // Ambil komponen dari layout
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        btnLogout = findViewById(R.id.btnLogout);

        // Siapkan RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listMahasiswa = new ArrayList<>();
        adapter = new MahasiswaAdapter(listMahasiswa);
        recyclerView.setAdapter(adapter);

        // 🔽 Ketika tombol tambah ditekan, buka halaman TambahMhs
        fab.setOnClickListener(view -> {
            startActivity(new Intent(DaftarMhs.this, TambahMhs.class));
        });

        // 🔽 Ketika tombol logout ditekan, logout dan kembali ke login screen
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // keluar akun
            startActivity(new Intent(DaftarMhs.this, MainActivity.class));
            finish(); // tutup halaman ini
        });
    }

    // 🔄 Fungsi ini otomatis jalan setiap kali user kembali ke halaman ini
    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromFirebase(); // ambil data terbaru dari Firebase
    }

    // 📦 Fungsi untuk ambil data dari Firebase Realtime Database
    private void loadDataFromFirebase() {
        // Ambil UID user yang sedang login
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Ambil data dari node "mahasiswa/{uid}"
        FirebaseDatabase.getInstance().getReference("mahasiswa")
                .child(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    listMahasiswa.clear(); // kosongkan data lama
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Mahasiswa mhs = data.getValue(Mahasiswa.class); // ambil data jadi objek Mahasiswa
                        if (mhs != null) {
                            mhs.setId(data.getKey()); // simpan key Firebase ke ID
                            listMahasiswa.add(mhs);  // tambahkan ke list
                        }
                    }
                    adapter.notifyDataSetChanged(); // refresh tampilan RecyclerView
                })
                .addOnFailureListener(e -> {
                    // Jika gagal ambil data
                    Toast.makeText(this, "Gagal ambil data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
