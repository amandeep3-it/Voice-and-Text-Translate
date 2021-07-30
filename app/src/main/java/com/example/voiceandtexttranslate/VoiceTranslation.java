package com.example.voiceandtexttranslate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class VoiceTranslation extends AppCompatActivity {
    private final Main main = new Main();
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private Spinner speech_spinner;
    private TextView speech_textview;
    private Spinner listen_spinner;
    private TextView listen_textview;
    private TextToSpeech text_to_speech;

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.voice_translation);

        this.speech_spinner = findViewById(R.id.speech_spinner);
        this.speech_textview = findViewById(R.id.speech_textview);
        this.listen_spinner = findViewById(R.id.listen_spinner);
        this.listen_textview = findViewById(R.id.listen_textview);

        ArrayAdapter<String> speech_spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.main.getLanguageName());
        speech_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.speech_spinner.setAdapter(speech_spinner_adapter);

        ArrayAdapter<String> listen_spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.main.getLanguageName());
        listen_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.listen_spinner.setAdapter(listen_spinner_adapter);

        this.text_to_speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    text_to_speech.setLanguage(new Locale(main.languages[0].id, main.languages[0].country));
                } else {
                    System.out.println("TTS FAILED: " + status + " = " + TextToSpeech.ERROR);
                }
            }
        });
        this.listen_textview.setText("YO WHATS UP");
//        (int status) -> this.text_to_speech.setLanguage(new Locale(this.main.languages[0].id, this.main.languages[0].country)));
    }

    protected synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == this.REQ_CODE_SPEECH_INPUT) && (resultCode == RESULT_OK) && (data != null)) {
            this.speech_textview.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
        }
    }

    public synchronized void speech_button(View v) {
        int i = this.speech_spinner.getSelectedItemPosition();

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, (this.main.languages[i].id + "_" + this.main.languages[i].country));
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak");

        try {
            this.startActivityForResult(intent, this.REQ_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void listen_button(View v) {
        int i = this.listen_spinner.getSelectedItemPosition();
//        this.text_to_speech.setLanguage(new Locale(this.main.languages[i].id, this.main.languages[i].country));
        this.text_to_speech.setLanguage(new Locale("en", "GB"));
        this.text_to_speech.speak(this.listen_textview.getText().toString(), TextToSpeech.QUEUE_FLUSH,null,null);
    }

    private boolean translate_ALLOW = true;
    public synchronized void translate(View v) {
        if (!this.translate_ALLOW) return;
        this.translate_ALLOW = false;

        int i1 = this.speech_spinner.getSelectedItemPosition(), i2 = this.listen_spinner.getSelectedItemPosition();

        new Thread() {
            @Override
            public void run() {
                super.run();

                String r = main.getTranslated(new Main.TranslationData(
                        speech_textview.getText().toString(),
                        main.languages[i1].id,
                        main.languages[i2].id
                ));
                if (r != null) listen_textview.setText(r);
                translate_ALLOW = true;
            }
        }.start();
    }
}