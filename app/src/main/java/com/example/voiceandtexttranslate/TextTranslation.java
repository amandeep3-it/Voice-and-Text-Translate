package com.example.voiceandtexttranslate;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TextTranslation extends Activity {
    private final Main main = new Main();
    private EditText write_edittext;
    private Spinner write_spinner;
    private TextView read_textview;
    private Spinner read_spinner;

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.text_translation);

        this.write_edittext = findViewById(R.id.write_edittext);
        this.write_spinner = findViewById(R.id.write_spinner);
        this.read_textview = findViewById(R.id.read_textview);
        this.read_spinner = findViewById(R.id.read_spinner);

        ArrayAdapter<String> write_spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.main.getLanguageName());
        write_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.write_spinner.setAdapter(write_spinner_adapter);

        ArrayAdapter<String> read_spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.main.getLanguageName());
        read_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.read_spinner.setAdapter(read_spinner_adapter);
    }

    private boolean translate_ALLOW = true;
    public synchronized void translate(View v) {
        if (!this.translate_ALLOW) return;
        this.translate_ALLOW = false;

        int i1 = this.write_spinner.getSelectedItemPosition(), i2 = this.read_spinner.getSelectedItemPosition();

        new Thread() {
            @Override
            public void run() {
                super.run();

                String r = main.getTranslated(new Main.TranslationData(
                        write_edittext.getText().toString(),
                        main.languages[i1].id,
                        main.languages[i2].id
                ));
                if (r != null) read_textview.setText(r);
                translate_ALLOW = true;
            }
        }.start();
    }

    @Override
    public void finish() {
        super.finish();
    }
}