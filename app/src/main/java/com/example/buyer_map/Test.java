package com.example.buyer_map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



class Code3 {
    public static int requestCode3 = 300;
    public static int resultCode3 = 3;
}

public class Test extends AppCompatActivity {

    private TextView textView_crop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView_crop = (TextView)findViewById(R.id.textView_crop);
    }

    public void btnComplete(View view)
    {
        Intent intent3 = new Intent(this, Crop2.class);
        startActivityForResult(intent3, Code3.requestCode3);
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
            super.onActivityResult(requestCode, resultCode, resultIntent);
            if (requestCode == Code3.requestCode3 && resultCode == Code3.resultCode3) {
                textView_crop.setText(resultIntent.getStringExtra("crop"));
            }

        }

    }





