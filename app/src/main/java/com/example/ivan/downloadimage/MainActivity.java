package com.example.ivan.downloadimage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.app.WallpaperManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.helper.Validate;

import java.nio.charset.Charset;
import java.util.Arrays;


import static android.R.attr.bitmap;
import static android.R.attr.dialogMessage;

public class MainActivity extends Activity {
    String URLA = "http://icdn3.digitaltrends.com/image/4-5-million-lamborghini-veneno-970x647-c.jpg";
    String testUrl = "http://yourshot.nationalgeographic.com/u/fQYSUbVfts-T7odkrFJckdiFeHvab0GWOfzhj7tYdC0uglagsDq0z7wA2G9Yz4QoAxwop3UAbRRPPZ-AC-H5DymXlNP0d_zujGWo4TP01GxniBvT3AGG7p9btZPmHp7C3FIzgfaBRh0kwSTFH0YZZlWhLO4iY1hKavmewEyCjC6fuHGmMoWnPfm76UnxQQIEJzDqpAJaHXY13SZAWlZfI5ri3UMKPMY/ ";
    ImageView image;
    Button btn, buttonSetWallpaper;
    ProgressDialog progDig;
    Bitmap bitmap;
    RelativeLayout layout;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Locate the ImageView in activity_main.xml
        image = (ImageView) findViewById(R.id.image); //preview
        btn = (Button) findViewById(R.id.button);
         buttonSetWallpaper = (Button) findViewById(R.id.set);
        layout = (RelativeLayout) findViewById(R.id.activity_main);



        //Capture button click
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //Execute DownloadImage AsyncTask
                new DownloadImage().execute(URLA);

            }
        });

        buttonSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new SetImageAsWallpaper().execute(URLA);
            }
        });




    }



    //DownloadImage AsyncTask
    private class DownloadImage extends AsyncTask<String, Void, Bitmap>
    {

        public  JSONObject readJsonFromUrl(String url) throws IOException, JSONException {    //static?
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                is.close();
            }
        }

        private  String readAll(Reader rd) throws IOException { // static??
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }




        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Create a progressfialog
            progDig = new ProgressDialog(MainActivity.this);
            //Set progressdialog title
            progDig.setTitle("Getting image for preview..");
            //Set progressdialog message
            progDig.setMessage("Loading...");
            progDig.setIndeterminate(false);
            //Show progressdialog
            progDig.show();
        }
        @Override
        protected Bitmap doInBackground(String... URL)
        {
            String imageURL = URL[0];
            Bitmap bitmap = null;

            try{


                JSONObject jobject = readJsonFromUrl(
                        "http://www.nationalgeographic.com/photography/photo-of-the-day/_jcr_content/.gallery.json");
                JSONObject article = jobject.getJSONArray("items").getJSONObject(0);
                //String urlJson = article.getString("url") + article.getString("originalUrl");
                String urlJson = article.getString("url") + article.getJSONObject("sizes").getString("2048");

                //Download Image from URL
                InputStream input = new java.net.URL(urlJson).openStream(); //imgSrc
                //Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);


            }
            catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap result)
        {
            //Set the bitmap into ImageView
            image.setImageBitmap(result);

            //Close progressdialog
            progDig.dismiss();
        }
    }

    private class SetImageAsWallpaper extends AsyncTask<String, Void, Bitmap>
    {

        public  JSONObject readJsonFromUrl(String url) throws IOException, JSONException {    //static?
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                is.close();
            }
        }

        private  String readAll(Reader rd) throws IOException { // static??
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Create a progressfialog
            progDig = new ProgressDialog(MainActivity.this);
            //Set progressdialog title
            progDig.setTitle("Setting Image as Wallpaper");
            //Set progressdialog message
            progDig.setMessage("Loading...");
            progDig.setIndeterminate(false);
            //Show progressdialog
            progDig.show();
        }
        @Override
        protected Bitmap doInBackground(String... URL)
        {
            String imageURL = URL[0];
            Bitmap bitmap = null;

            WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try{


                JSONObject jobject = readJsonFromUrl(
                        "http://www.nationalgeographic.com/photography/photo-of-the-day/_jcr_content/.gallery.json");
                JSONObject article = jobject.getJSONArray("items").getJSONObject(0);
                //String urlJson = article.getString("url") + article.getString("originalUrl");
                String urlJson = article.getString("url") + article.getJSONObject("sizes").getString("2048");

                //Download Image from URL
                InputStream input = new java.net.URL(urlJson).openStream(); //imgSrc
                //Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
                myWallpaperManager.setBitmap(bitmap); //ja stava slikata kako background
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap result)
        {
            //Set the bitmap into ImageView
            image.setImageBitmap(result);








            //Close progressdialog
            progDig.dismiss();
        }
    }

    }

