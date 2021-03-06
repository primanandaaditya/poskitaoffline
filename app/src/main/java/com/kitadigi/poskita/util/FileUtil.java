package com.kitadigi.poskita.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FileUtil {


    //fungsi ini untuk mendownload image ke dalam folder tertentu
    //dipakai di BarangController.java
    //saat get list barang dari API
    //setiap url gambar dimasukkan dalam fungsi ini dan akan di-download ke folder yang ditentukan
    public static void downloadFile(Context context,String uRl) {

        //tentukan folder tujuan download
        File myDir = new File(Environment.getExternalStorageDirectory(), Constants.poskita + "/");

        //jika belum ada, buat dulu
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        //init downloadmanager class
        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        //parse uri dari string url (ada di api get barang)
        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        Log.d("hasil download", myDir.getPath() + "/" + Constants.poskita + "/");

        //cek dulu apakah file gambar sudah ada di hp user
        //bisa saja user sync lebih dari 1 kali
        File gambar = new File(Environment.getExternalStorageDirectory(), Constants.poskita + "/" + StringUtil.getFileNameFromURL(uRl));

        //jika gambar sudah ada
        //hapus, supaya gambar tidak duplikat -> hemat storage

        if (gambar.exists()){
            try {
                gambar.delete();
            }catch (Exception e){

            }

        }


        //tentukan parameter untuk download
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setTitle("Poskita - " + "sedang mengunduh " + uRl)
                .setVisibleInDownloadsUi(true)
                .setDestinationInExternalPublicDir(Constants.poskita + "/", StringUtil.getFileNameFromURL(uRl));

        //download gambar mulai
        mgr.enqueue(request);


    }


    public static String getFotoProduk(Context context){
//        ContextWrapper cw = new ContextWrapper(context);
//        final File directory = cw.getDir(Constants.poskita, Context.MODE_PRIVATE);
////        return directory.getAbsolutePath().toString() +  "/" + Constants.poskita + "/";
//

        return Environment.getExternalStorageDirectory() + "/" + Constants.poskita + "/";

    }



    //target to save
    public static Target getTarget(final String namaFile){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + namaFile);
                        Log.d("lokasi baru", file.getPath());

                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    //fungsi ini untuk menyimpan gambar
    //dari list item yang ada di PrimaItemListAdapter.java
    public static Target picassoImageTarget(Context context,  final String imageName) {

        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(Constants.poskita, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            Log.d("gagal target",e.getMessage());
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                Log.d("gagal target",e.getMessage());
                            }
                        }
                        Log.i("gambar", "gambar tersimpan di >>>" + myImageFile.getAbsolutePath());

                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("gagal picasso simpan", errorDrawable.toString());
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }

    public static void writeToFile(String data,String nama_file,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(nama_file, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public static String readFromFile(Context context,String nama_file) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(nama_file);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
