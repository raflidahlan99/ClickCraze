package com.example.clickcraze;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextInputEditText usernameInput = findViewById(R.id.username);
        TextInputEditText passInput = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvReg = findViewById(R.id.register);

        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        String USERNAME = sharedPreferences.getString("username", "");
        String PASSWORD = sharedPreferences.getString("password", "");

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passInput.getText().toString();

            if (username.isEmpty()) {
                usernameInput.setError("Please fill this field");
            } else if (password.isEmpty()) {
                passInput.setError("Please fill this field");
            } else if (username.equals(USERNAME) && password.equals(PASSWORD)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Username atau Password salah.", Toast.LENGTH_SHORT).show();
            }
        });

        tvReg.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}