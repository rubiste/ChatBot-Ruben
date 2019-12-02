package com.example.chatbotpsp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chatbotpsp.API.ChatterBot;
import com.example.chatbotpsp.API.ChatterBotFactory;
import com.example.chatbotpsp.API.ChatterBotSession;
import com.example.chatbotpsp.API.ChatterBotType;
import com.example.chatbotpsp.API.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btEnviar;
    EditText etInput;
    ChatterBot bot;
    ChatterBotSession botSession;
    RecyclerView rvMessages;
    AdapterMultiType adapter;
    String languageFrom = "es", languageTo = "en";
    String cadNoTraducida = "";
    String traduccion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initEvents();
        initBot();
    }

    private void initComponents() {
        btEnviar = findViewById(R.id.btSend);
        etInput = findViewById(R.id.etInput);
        rvMessages = findViewById(R.id.recyclerView);

        adapter = new AdapterMultiType(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(adapter);
    }

    private void initEvents() {
        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadNoTraducida = etInput.getText().toString();
                TraduccirAIngles translateTask = new TraduccirAIngles(cadNoTraducida);
                adapter.mensajes.add(new Mensaje(etInput.getText().toString(), true));
                adapter.notifyDataSetChanged();
                rvMessages.scrollToPosition(adapter.mensajes.size() - 1);
                etInput.setText("");
                languageFrom = "es";
                languageTo = "en";
                translateTask.execute();
            }
        });
    }

    private void initBot() {
        ChatterBotFactory factory = new ChatterBotFactory();

        try {
            bot = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
        } catch (Exception e) {
            e.printStackTrace();
        }

        botSession = bot.createSession();
    }

    // POST
    // https://www.bing.com/ttranslatev3

    // HEADERS
    // HEADER NAME: Content-type / application/x-www-form-urlencoded
    // User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36

    // BODY
    // fromLang=es
    // text=Hola
    // to=en

    public void doTheChat(){
        new Chat().execute();
    }

    private void chat(String msg) {
        try {
            String response = botSession.think(msg);
            new TraduccirAEsp(response).execute();
        }catch(Exception e){
            Log.v("xyz", "Error: " + e.getMessage());
        }
    }

    private void showBotResponse(){
        adapter.mensajes.add(new Mensaje(traduccion, false));
        adapter.notifyDataSetChanged();
        rvMessages.scrollToPosition(adapter.mensajes.size() - 1);
    }


    public String decomposeJson(String json){
        String translationResult = "Could not get";
        try {
            JSONArray arr = new JSONArray(json);
            JSONObject jObj = arr.getJSONObject(0);
            translationResult = jObj.getString("translations");
            JSONArray arr2 = new JSONArray(translationResult);
            JSONObject jObj2 = arr2.getJSONObject(0);
            translationResult = jObj2.getString("text");
        } catch (JSONException e) {
            translationResult = e.getLocalizedMessage();
        }
        return translationResult;
    }

    private class Chat extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            chat(traduccion);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            rvMessages.scrollToPosition(adapter.mensajes.size() - 1);
        }
    }

    private class TraduccirAIngles extends AsyncTask<Void, Void, Void>{

        private final Map<String, String> headers;
        private final Map<String, String> vars;
        String s = "Error";

        private TraduccirAIngles(String message) {
            headers = new LinkedHashMap<String, String>();
            headers.put("Content-type","application/x-www-form-urlencoded");
            headers.put("User-Agent:","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");

            vars = new HashMap<String, String>();
            vars.put("fromLang", "es");
            vars.put("text",message);
            vars.put("to","en");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                s = Utils.performPostCall("https://www.bing.com/ttranslatev3", (HashMap) vars);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("xyz", "Error: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            traduccion = decomposeJson(s);
            doTheChat();
        }
    }

    private class TraduccirAEsp extends AsyncTask<Void, Void, Void>{

        private final Map<String, String> headers;
        private final Map<String, String> vars;
        String s = "Error";

        private TraduccirAEsp(String message) {
            headers = new LinkedHashMap<String, String>();
            headers.put("Content-type","application/x-www-form-urlencoded");
            headers.put("User-Agent:","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");

            vars = new HashMap<String, String>();
            vars.put("fromLang", "en");
            vars.put("text",message);
            vars.put("to","es");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                s = Utils.performPostCall("https://www.bing.com/ttranslatev3", (HashMap) vars);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("xyz", "Error: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            traduccion = decomposeJson(s);
            showBotResponse();
        }
    }

}

