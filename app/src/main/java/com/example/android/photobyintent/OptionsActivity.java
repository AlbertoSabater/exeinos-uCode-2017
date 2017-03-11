package com.example.android.photobyintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Antonio on 11/3/17.
 */
public class OptionsActivity extends Activity {

    private ListView wordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        ArrayList<String> matches = intent.getStringArrayListExtra(PhotoIntentActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
//        TextView textView = (TextView) findViewById(R.id.optionText);
//        textView.setText(message);

        wordsList = (ListView) findViewById(R.id.list);
        wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
    }
}
