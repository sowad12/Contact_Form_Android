package com.example.contactform;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

private EditText name,email,phn1,phn2;
private Button cancel,save,dialogCancel;
private ImageView img;
private FloatingActionButton imgChoice;
private ImageView selectImgBtn,selectGalleryBtn;
private TextView sample;
private String errmsg="";
private String encodeImg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=findViewById(R.id.namebtn);
        email=findViewById(R.id.emailbtn);
        phn1=findViewById(R.id.phonebtn);
        phn2=findViewById(R.id.phoneOfficebtn);
        cancel=findViewById(R.id.cancelbtn);
        save=findViewById(R.id.savebtn);
        img=findViewById(R.id.imgbtn);
        imgChoice=findViewById(R.id.imgChoicebtn);


        imgChoice.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},101);

                }
                else
                {
             AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater=getLayoutInflater();
                    View dailogView=inflater.inflate(R.layout.alert_dialog,null);
                    builder.setCancelable(false);
                    builder.setTitle("Choose\n");

                    builder.setView(dailogView);

                    selectImgBtn=dailogView.findViewById(R.id.selectImgBtn);
                    selectGalleryBtn=dailogView.findViewById(R.id.selectGalleryBtn);
                    dialogCancel=dailogView.findViewById(R.id.dailougeCancel);
                    AlertDialog profilePic=builder.create();
                    profilePic.show();
                    selectImgBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        cameraPic();
                            profilePic.cancel();
                        }


                    });

                    selectGalleryBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //for gallery
                            Intent pickImg=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickImg,1);
                            profilePic.cancel();
                        }
                    });
                    dialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            profilePic.cancel();
                        }
                    });
                }
            }




        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 errmsg="";

                String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
                Pattern pattern=Pattern.compile(regex);
                Matcher matcher= pattern.matcher(email.getText().toString());
                if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty()||
                phn1.getText().toString().isEmpty() ){
                    errmsg+="input field cannot be empty \n";
                }
                else {
                    if (name.getText().toString().length() < 5) {
                        errmsg += "Name is at least 5 character long \n";
                    }

                    if (phn1.getText().toString().length() != 11) {
                        errmsg += "Phone Number is not valid \n";
                    }
                  
                    if (!matcher.matches()) {
                        errmsg += "Email Invalid \n";
                    }
                 if(encodeImg.length()==0){
                     errmsg += "Upload a Image \n";
                 }

                }

                if(errmsg.length()==0){
                    showDialogue("Do you want to save the event info?", "info", "yes", "no");
                }
                else{
                    showDialogue(errmsg,"Error","ok","back");
                }
            }


        });
  cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          finish();
          System.exit(0);
      }
  });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    Uri imgUri=data.getData();
                    img.setImageURI(imgUri);
                    //decoding img to based64
                    try {
                        Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                        byte[] bytes=stream.toByteArray();
                       encodeImg = Base64.encodeToString(bytes,Base64.DEFAULT);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                if(resultCode==RESULT_OK){
              Bundle bundle=data.getExtras();
                    Bitmap bitImg=(Bitmap) bundle.get("data");
                    img.setImageBitmap(bitImg);

                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                    bitImg.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    byte[] bytes=stream.toByteArray();
                    encodeImg = Base64.encodeToString(bytes,Base64.DEFAULT);


                }
                break;
            default:
                break;
        }
    }
    private void cameraPic() {
        Intent takepic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takepic.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takepic,2);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
     cameraPic();
        }
        else{
            Toast.makeText(MainActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialogue(String msg, String title, String btn1, String btn2){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton("Yes",new  DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (errmsg.length() == 0) {
//                        Util.getInstance().deleteByKey(MainActivity.this, key);
                            SharedPreferences sh = getSharedPreferences("contact", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sh.edit();

                            editor.putString("name", name.getText().toString());
                            editor.putString("email", email.getText().toString());
                            editor.putString("phn1", phn1.getText().toString());
                            editor.putString("phn2", phn2.getText().toString());
                            editor.putString("profilePic", encodeImg);
                            editor.apply();
                            Intent res = new Intent(MainActivity.this, result.class);
                            startActivity(res);
//                        loadData();
//                        adapter.notifyDataSetChanged()
                            Toast.makeText(getApplicationContext()," Saved Successful",Toast.LENGTH_LONG).show();
                        }
                        else{
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}