package com.example.tugasbesar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // Komponen input dan tombol
    private EditText usernameField, passwordField;
    private Button btnLogin, btnToSignup;

    // Firebase Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // tampilkan layout login

        // Inisialisasi Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Hubungkan variabel ke komponen di layout
        usernameField = findViewById(R.id.emailField);      // 🔁 ini isinya "username", bukan email asli
        passwordField = findViewById(R.id.passwordField);   // input password
        btnLogin = findViewById(R.id.btnLogin);             // tombol login
        btnToSignup = findViewById(R.id.btnSignup);         // tombol ke halaman signup

        // Aksi ketika tombol LOGIN ditekan
        btnLogin.setOnClickListener(view -> {
            String username = usernameField.getText().toString().trim();   // ambil teks username
            String password = passwordField.getText().toString().trim();   // ambil teks password

            // Validasi input tidak boleh kosong
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase hanya bisa login pakai email, jadi buat "email palsu"
            String fakeEmail = username + "@appdomain.com";

            // Coba login ke Firebase Authentication
            mAuth.signInWithEmailAndPassword(fakeEmail, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Login berhasil
                            Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, DaftarMhs.class)); // pindah ke halaman daftar mahasiswa
                            finish(); // tutup halaman login
                        } else {
                            // Login gagal
                            Toast.makeText(this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Aksi ketika tombol "Belum punya akun? Daftar"
        btnToSignup.setOnClickListener(view -> {
            startActivity(new Intent(this, SingupActivity.class)); // pindah ke halaman signup
        });
    }
}
