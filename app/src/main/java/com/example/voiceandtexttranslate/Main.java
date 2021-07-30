package com.example.voiceandtexttranslate;

import android.os.AsyncTask;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import android.widget.TextView;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

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
            final String API_KEY = "AIzaSyDXV97Bzdj5HcCuEE_O6I4JHdipmKNCScs";

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

//private class ReadTask extends AsyncTask<String[], Void, String> {
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    protected String doInBackground(String[]... params) {
//        String the_actual_response = "Fail";//returning message if task fails
//        try// try method required for URL connections and JSON use
//        {
//            String[] list = params[0];
//            String json = "{'q' : '" + list[1].toString() + "', 'source' : '" + list[2].toString() +"' , 'target' : '" + list[3].toString() + "'}";
//
//            URL url = new URL(list[0]);// Creating a URL variable
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//opening a public connection with the URL address
//            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");//Setting the connection property that it is a JSON request
//            conn.setDoOutput(true);//Assigning that the connection can perform outputs
//            conn.setDoInput(true);//Assigning that the connection can perform outputs
//            conn.setRequestMethod("POST");//Assigning that this is a 'POST' URL request
//
//            OutputStream os = conn.getOutputStream();//opening an input and output of variables streaming with OutputStream
//            os.write(json.getBytes("UTF-8"));//Sending the JSON structured object with 'UTF-8' format and joining with the OutputStream
//            os.close();//closing the streaming
//
//            InputStream in = new BufferedInputStream(conn.getInputStream());//opening an input streaming with Buffer reading
//            String result = IOUtils.toString(in, "UTF-8");//retrieving the received results
//
//            JSONObject myresponse = new JSONObject(result);//creating the received result into JSON object
//            JSONObject data = myresponse.getJSONObject("data");//assigning the object 'data' from the JSON object
//            JSONArray translations = data.getJSONArray("translations");//assigning the array 'translations' from the JSON object
//            String textt = "Fail";//assigning the initial message to 'Fail'
//            for (int i=0;i<translations.length();i++)//reading all the variables which have for loop
//            {
//                JSONObject myobje = translations.getJSONObject(i);//Assigning the element 'TranslatedText' from the JSON array
//                textt = myobje.getString("translatedText");//Assigning the retrieved translated text to the 'textt' variable
//            }
//            the_actual_response = textt.toString();
//            read_text_view.setText(textt.toString());
//
//            in.close();//closing the input streaming
//            conn.disconnect();//closing the connection with the URL
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return the_actual_response;//return final translation
//    }
//}