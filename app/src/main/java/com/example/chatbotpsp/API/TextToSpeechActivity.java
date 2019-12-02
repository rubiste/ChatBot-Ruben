package com.example.chatbotpsp.API;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TextToSpeechActivity extends Activity implements TextToSpeech.OnInitListener {
    private static final String TAG = "TextToSpeechDemo";
    private Context context;
    private String mensaje;
    private TextToSpeech mTts;

    public TextToSpeechActivity(String mensaje, Context context, Float pitch, Float rate) {
        mTts = new TextToSpeech(context, this);
        mTts.setPitch(pitch);
        mTts.setSpeechRate(rate);
        this.mensaje = mensaje;
    }

    @Override
    public void onDestroy() {

        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            Locale spanish = new Locale("es", "ES");
            int result = mTts.setLanguage(spanish);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language is not available.");
            } else {
                sayHello();
            }
        } else {
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }

    private void sayHello() {
        mTts.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null);
    }
}