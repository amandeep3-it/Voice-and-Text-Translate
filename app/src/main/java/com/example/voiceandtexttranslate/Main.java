package com.example.voiceandtexttranslate;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    public static class Language {
        public String id, country, name;

        public Language(String id, String country, String name) {
            this.id = id;
            this.country = country;
            this.name = name;
        }
    }

    public static class TranslationData {
        public String text, source_language, target_language;

        public TranslationData(String text, String source_language, String target_language) {
            this.text = text;
            this.source_language = source_language;
            this.target_language = target_language;
        }
    }

    public Language[] languages = { new Language("en", "US", "English (default)"), new Language("ar", "EG", "Arabic"), new Language("bg", "BG", "Bulgarian"), new Language("zh", "CN", "Chinese"), new Language("cs", "CZ", "Czech"), new Language("nl", "BE", "Dutch"), new Language("fi", "FI", "Finnish"), new Language("fr", "FR", "French"), new Language("de", "DE", "Germany"), new Language("el", "GR", "Greek"), new Language("iw", "IL", "Hebrew"), new Language("hi", "IN", "Hindi"), new Language("hu", "HU", "Hungarian"), new Language("in", "ID", "Indonesian"), new Language("it", "IT", "Italian"), new Language("ja", "JP", "Japanese"), new Language("ko", "KR", "Korean"), new Language("pl", "PL", "Polish"), new Language("pt", "PT", "Portuguese"), new Language("ro", "RO", "Romanian"), new Language("ru", "RU", "Russian"), new Language("sr", "RS", "Serbian"), new Language("es", "ES", "Spanish"), new Language("sv", "SE", "Swedish"), new Language("tl", "PH", "Tagalog"), new Language("zh", "TW", "Taiwan"), new Language("th", "TH", "Thai"), new Language("tr", "TR", "Turkish"), new Language("uk", "UA", "Ukrainian"), new Language("vi", "VN", "Vietnamese") };

    public String[] getLanguageName() {
        String[] list = new String[this.languages.length];

        for (int i=0; i<this.languages.length; i++)
            list[i] = this.languages[i].name;

        return list;
    }

    public synchronized String getTranslated(TranslationData data) {
        String _return = null;
        try {
            final String API_KEY = "";

            JSONObject post_data = new JSONObject();
            post_data.put("q", data.text);
            post_data.put("source", data.source_language);
            post_data.put("target", data.target_language);

            HttpURLConnection conn = (HttpURLConnection) (new URL("https://translation.googleapis.com/language/translate/v2?key=" + API_KEY)).openConnection();
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(post_data.toString().getBytes(StandardCharsets.UTF_8));
            os.close();

            InputStream is = new BufferedInputStream(conn.getInputStream());
            JSONArray response = new JSONObject(IOUtils.toString(is, StandardCharsets.UTF_8)).getJSONObject("data").getJSONArray("translations");
            for (int i=0; i<response.length(); i++) {
                _return = response.getJSONObject(i).getString("translatedText");
            }

            is.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _return;
    }
}