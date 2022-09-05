package com.example.voiceandtexttranslate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);
    }

    public synchronized void onClick(View v) {
        if (v.getId() == R.id.button_1)      startActivity(new Intent(getBaseContext(), VoiceTranslation.class));
        else if (v.getId() == R.id.button_2) startActivity(new Intent(getBaseContext(), TextTranslation.class));
    }
}