package com.example.tugasbesar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {

    private ArrayList<Mahasiswa> listMahasiswa; // daftar data mahasiswa
    private Context context;

    // Konstruktor adapter
    public MahasiswaAdapter(ArrayList<Mahasiswa> listMahasiswa) {
        this.listMahasiswa = listMahasiswa;
    }

    // Kelas ViewHolder untuk menyimpan tampilan tiap item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, nim, jurusan, kelas; // ✅ tambahkan kelas

        public ViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.textNama);
            nim = itemView.findViewById(R.id.textNim);
            jurusan = itemView.findViewById(R.id.textJurusan);
            kelas = itemView.findViewById(R.id.textKelas); // ✅ pastikan ada di layout XML
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext(); // ambil context dari activity
        View view = LayoutInflater.from(context)
                .inflate(R.layout.activity_mahasiswa_adapter, parent, false); // inflate layout item
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaAdapter.ViewHolder holder, int position) {
        // Ambil data mahasiswa sesuai posisi
        Mahasiswa mhs = listMahasiswa.get(position);
        holder.nama.setText(mhs.getNama());
        holder.nim.setText(mhs.getNim());
        holder.jurusan.setText(mhs.getJurusan());
        holder.kelas.setText(mhs.getKelas()); // ✅ tampilkan kelas

        // Ketika item diklik, tampilkan menu popup Edit & Delete
        holder.itemView.setOnClickListener(v -> {
            showPopupMenu(v, mhs, position);
        });
    }

    // Fungsi untuk menampilkan popup menu
    private void showPopupMenu(View view, Mahasiswa mhs, int position) {
        Log.d("PopupMenu", "Menampilkan popup menu...");
        PopupMenu popup = new PopupMenu(context, view); // buat menu
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_opsi, popup.getMenu()); // isi menu dari XML

        // Aksi ketika item menu dipilih
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.edit) {
                editData(mhs); // panggil edit
                return true;
            } else if (itemId == R.id.delete) {
                deleteData(mhs, position); // panggil hapus
                return true;
            }
            return false;
        });

        popup.show(); // tampilkan menu
    }

    // Fungsi untuk berpindah ke halaman TambahMhs (edit data)
    private void editData(Mahasiswa mhs) {
        Intent intent = new Intent(context, TambahMhs.class);
        intent.putExtra("edit", true);
        intent.putExtra("id", mhs.getId());
        intent.putExtra("nama", mhs.getNama());
        intent.putExtra("nim", mhs.getNim());
        intent.putExtra("jurusan", mhs.getJurusan());
        intent.putExtra("kelas", mhs.getKelas()); // ✅ kirim kelas ke intent
        context.startActivity(intent);
    }

    // Fungsi untuk menghapus data mahasiswa
    private void deleteData(Mahasiswa mhs, int position) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String id = mhs.getId(); // ID = key di Firebase

        FirebaseDatabase.getInstance().getReference("mahasiswa")
                .child(uid)
                .child(id)
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Jika berhasil hapus di Firebase, hapus dari tampilan juga
                    Toast.makeText(context, "Data dihapus", Toast.LENGTH_SHORT).show();
                    listMahasiswa.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listMahasiswa.size());
                })
                .addOnFailureListener(e -> {
                    // Jika gagal
                    Toast.makeText(context, "Gagal hapus: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Jumlah item
    @Override
    public int getItemCount() {
        return listMahasiswa.size();
    }
}
