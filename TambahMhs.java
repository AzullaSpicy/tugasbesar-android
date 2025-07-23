package com.example.tugasbesar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class TambahMhs extends AppCompatActivity {

    private EditText editNama, editNim, editJurusan, editKelas;
    private Button btnSave, btnClear, btnCancel;

    private boolean isEdit = false;
    private String editId = null;

    private RadioGroup radioGroupGender;
    private RadioButton radioMale, radioFemale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_mhs);

        editNama = findViewById(R.id.editNama);
        editNim = findViewById(R.id.editNim);
        editJurusan = findViewById(R.id.editJurusan);
        editKelas = findViewById(R.id.editKelas); // ✅ tambahkan ini

        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);
        btnCancel = findViewById(R.id.btnCancel);

        // 🔁 MODE EDIT: Isi data dari intent jika edit
        if (getIntent().getBooleanExtra("edit", false)) {
            isEdit = true;
            editId = getIntent().getStringExtra("id");
            editNama.setText(getIntent().getStringExtra("nama"));
            editNim.setText(getIntent().getStringExtra("nim"));
            editJurusan.setText(getIntent().getStringExtra("jurusan"));
            editKelas.setText(getIntent().getStringExtra("kelas"));
        }

        btnSave.setOnClickListener(v -> {
            String nama = editNama.getText().toString().trim();
            String nim = editNim.getText().toString().trim();
            String jurusan = editJurusan.getText().toString().trim();
            String kelas = editKelas.getText().toString().trim();

            if (nama.isEmpty() || nim.isEmpty() || jurusan.isEmpty() || kelas.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String key = isEdit ? editId : nim;

            Mahasiswa mhs = new Mahasiswa(key, nama, nim, jurusan, kelas);

            FirebaseDatabase.getInstance().getReference("mahasiswa")
                    .child(uid)
                    .child(key)
                    .setValue(mhs)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, isEdit ? "Data diperbarui" : "Data disimpan", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        btnClear.setOnClickListener(v -> {
            editNama.setText("");
            editNim.setText("");
            editJurusan.setText("");
            editKelas.setText("");
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}
