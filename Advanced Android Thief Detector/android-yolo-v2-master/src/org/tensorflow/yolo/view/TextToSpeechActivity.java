package org.tensorflow.yolo.view;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.tensorflow.yolo.R;
import org.tensorflow.yolo.model.Recognition;
import org.tensorflow.yolo.soundDetect;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.tensorflow.yolo.Config.LOGGING_TAG;




public abstract class TextToSpeechActivity extends CameraActivity implements TextToSpeech.OnInitListener {
    private TextToSpeech textToSpeech;
    private String lastRecognizedClass = "";
    public String phoneNo = "8167782585";
    public String msg ="Intruders Dtected";
    private Context mContext;

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(LOGGING_TAG, "Text to speech error: This Language is not supported");
            }
        } else {
            Log.e(LOGGING_TAG, "Text to speech: Initilization Failed!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textToSpeech = new TextToSpeech(this, this);


       
    }


    protected void speak(List<Recognition> results) {
        if (!(results.isEmpty() || lastRecognizedClass.equals(results.get(0).getTitle()))) {
            lastRecognizedClass = results.get(0).getTitle();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(lastRecognizedClass, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak(lastRecognizedClass, TextToSpeech.QUEUE_FLUSH, null);
            }

            if (results.get(0).getTitle().equals("person")){
                String smsNumber = "18163289530";// E164 format without '+' sign
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                //  Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Intruder Detected");
                sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
                sendIntent.setPackage("com.whatsapp");

                startActivity(sendIntent);
            }

        }
    }



}






