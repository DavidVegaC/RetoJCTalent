package com.davega.retojctalent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davega.retojctalent.Util.CustomProgressDialog;
import com.davega.retojctalent.Util.Utils;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import static com.davega.retojctalent.Util.Utils.STORAGE_CODE;
import static com.davega.retojctalent.Util.Utils.message;

public class MainActivity extends AppCompatActivity {

    private ImageView ivTest;
    private Button btnDownload;
    private TextView tvDescription;

    private Typeface typeface =null, typeface2 = null;
    private TextPaint paint = null;

    String permissionStorage[];
    private CustomProgressDialog customProgressDialog;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
    }

    private void initComponent(){
        customProgressDialog=new CustomProgressDialog(this);

        tvDescription= findViewById(R.id.tvDescription);
        ivTest = findViewById(R.id.ivTest);
        btnDownload = findViewById(R.id.btnDownload);

        tvDescription.setText(message);

        permissionStorage= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        typeface = Typeface.createFromAsset(getApplication().getAssets(),"fonts/courier-new.ttf");
        typeface2 = Typeface.create(typeface,Typeface.BOLD);

        paint = new TextPaint();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setAntiAlias(true);

        //paint.setColor();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!storagePermission()){
                    requestStoragePermission();
                }else{
                    new DownloadImages().execute();
                }


            }
        });
    }


    private class DownloadImages extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressDialog.showProgressDialog("Generando Imagen de ticket.");
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            bitmap = Bitmap.createBitmap(300, 400, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            //FileInputStream is;
            FileOutputStream fos=null;

            try {

                File myDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File f = new File (myDir, "ticket.txt");
                BufferedReader reader=null;

                //Si el archivo no existe en la ruta, uso el archivo por defecto que se encuentra en el assets
                if(f.exists()){
                    reader = new BufferedReader(new FileReader(f));
                }else{
                    InputStream is = getApplication().getAssets().open("ticket.txt");
                    InputStreamReader isr =  new InputStreamReader(is);
                    reader = new BufferedReader(isr);
                }

                String line = reader.readLine();

                int x=10, y=0;
                while(line != null){
                    //Log.d("StackOverflow", line);
                    switch (line.substring(0,1)){
                        case "A":
                            paint.setTypeface(typeface);
                            paint.setTextSize(11);
                            y=y+11;
                            canvas.drawText(line.substring(1), 0, y, paint);
                            break;
                        case "B":
                            paint.setTypeface(typeface2);
                            paint.setTextSize(22);
                            y=y+22;
                            canvas.drawText(line.substring(1), 0, y, paint);
                            break;
                        default:
                            break;
                    }

                    line = reader.readLine();

                }


            }catch (Exception e){
                Log.e("ERROR1",e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            ivTest.setImageBitmap(bitmap);
            Utils.SaveImage(getApplicationContext(),bitmap);

            customProgressDialog.dismissProgressDialog();
        }
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, permissionStorage,STORAGE_CODE);
    }

    private boolean storagePermission(){
        boolean permissionStage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return permissionStage;
    }


    private void onRequestPermissionResult(int requestCode, String[] permission, int[] grantResults)
    {
        switch(requestCode)
        {
            case STORAGE_CODE:
                if(grantResults.length>0){
                    boolean storageRequestAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageRequestAccepted)
                        Toast.makeText(this, "Se concedieron los permisos necesarios.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "Es necesario los permisos para continuar.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}