package com.example.android.photobyintent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static com.example.android.photobyintent.PhotoIntentActivity.EXTRA_MESSAGE;

public class ResultActivity extends Activity implements TextToSpeech.OnInitListener {

    private static final int SPEAK_REQUEST_CODE = 1234;
    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private final int MY_DATA_CHECK_CODE = 0;

    private AuxMethods auxM;
    ImageView imageView;
    ListView listView;
    String url="http://2.bp.blogspot.com/-oPQ_xsVNzVI/Ts8YhbVekCI/AAAAAAAAAqg/mJSLV3HI2Do/s1600/adidas%20superstar%20(2).jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        auxM=new AuxMethods(this);
        listView = (ListView) findViewById(R.id.list);
        imageView= (ImageView) findViewById(R.id.imageViewResult);
        new RetrieveFeedTask(this).execute(url);

        Button speakButton = (Button) findViewById(R.id.speakButton);
        Log.d("speak",speakButton.toString());
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakButtonClicked(view);
            }
        });

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);


    }

    //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SPEAK_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(ResultActivity.this, OptionsActivity.class);
                    //based on item add info to intent
                    ArrayList<String> heard = data.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);

                    Log.i(TAG, "heard: " + heard.size());
                    ArrayList<String> matches = auxM.getWords(heard);
                    listView.setAdapter(new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, matches));
                    Log.i(TAG, "matches: " + matches.size());

                    //intent.putExtra(EXTRA_MESSAGE, matches);

                    /// startActivity(intent);
                }
                break;
            }
            case MY_DATA_CHECK_CODE: {
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    //the user has the necessary data - create the TTS
                    myTTS = new TextToSpeech(this, this);
                }
                else {
                    //no data - install it now
                    Intent installTTSIntent = new Intent();
                    installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installTTSIntent);
                }
            }
            break;

        } // switch
    }

    //speak the user text
    public void speakWords(String speech) {

        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void putImage(Bitmap bmp){
        imageView.setImageBitmap(bmp);
    }

    /**
     * Handle the action of the button being clicked
     */
    public void speakButtonClicked(View v)
    {
        startVoiceRecognitionActivity();
    }

    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
        startActivityForResult(intent, SPEAK_REQUEST_CODE);
    }


    //setup TTS
    public void onInit(int initStatus) {

        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            Locale locSpanish = new Locale("spa", "MEX");
            if(myTTS.isLanguageAvailable(locSpanish)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(locSpanish);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}

