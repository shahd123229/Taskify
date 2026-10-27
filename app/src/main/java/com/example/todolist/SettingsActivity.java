package com.example.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchNotifications;
    private RadioGroup radioGroupTheme;
    public static final String PREFS_NAME = "TodoAppPrefs";
    private boolean isUiReady = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchNotifications = findViewById(R.id.switchNotifications);
        radioGroupTheme = findViewById(R.id.radioGroupTheme);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        switchNotifications.setChecked(prefs.getBoolean("notifications_enabled", true));

        String savedTheme = prefs.getString("theme", "light");
        if (savedTheme.equals("dark")) {
            radioGroupTheme.check(R.id.radioDark);
        } else {
            radioGroupTheme.check(R.id.radioLight);
        }

        /////
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("notifications_enabled", isChecked).apply();
            String msg = isChecked ? "Notifications turnned on" : "Notifications Turned oFF";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        // دلوقتى بس نسمح للمستمع انه يشتغل
        isUiReady = true;

        radioGroupTheme.setOnCheckedChangeListener((group, checkedId) -> {
            if (!isUiReady) return;

            String theme = checkedId == R.id.radioDark ? "dark" : "light";
            String currentSaved = prefs.getString("theme", "light");

            if (theme.equals(currentSaved)) return;

            prefs.edit().putString("theme", theme).apply();
            Toast.makeText(this, "Theme selected: " + theme, Toast.LENGTH_SHORT).show();

            if (checkedId == R.id.radioDark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }
}