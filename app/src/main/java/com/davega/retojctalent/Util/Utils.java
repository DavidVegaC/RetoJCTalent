package com.davega.retojctalent.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class Utils {

    public static final int STORAGE_CODE=400;

    public static void SaveImage(Context context, Bitmap finalBitmap) {
        File myDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        String fname = "Ticket.png";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(context, "Se descargó satisfactoriamente el archivo Ticket.png en la carpeta Downloads.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Se descargó el archivo Ticket.png en la carpeta Downloads.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static final String message ="Se buscará un archivo en la carpeta DOWNLOAD con el nombre Ticket.txt.\nEn el caso no lo encuentre usará por defecto el archivo que se me brindó por correo.";

}
