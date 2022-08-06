package com.example.contactform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Base64;

public class result extends AppCompatActivity {
  private   TextView name,email,phn1,phn2;
   private ImageView resultImg;
  SharedPreferences shp;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
       name=findViewById(R.id.NameText);
        email=findViewById(R.id.EmailText);
        phn1=findViewById(R.id.phn1Text);
        phn2=findViewById(R.id.ph2Text);
        resultImg=findViewById(R.id.imgTxt);
       shp= getSharedPreferences("contact",MODE_PRIVATE);
        name.setText(shp.getString("name",null));
        email.setText(shp.getString("email",null));
        phn1.setText(shp.getString("phn1",null));
        phn2.setText(shp.getString("phn2",null));
        //decode image
        byte[] b = Base64.decode(shp.getString("profilePic", null), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        resultImg.setImageBitmap(bitmap);
    }


}