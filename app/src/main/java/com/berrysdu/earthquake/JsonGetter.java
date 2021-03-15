package com.berrysdu.earthquake;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonGetter {
    private String jsonStr="unable to do so.";

    public String getJsonStr() {
       /* for(int i=0;i<=19;i++){
            if(!jsonStr.equals("unable to do so.")){break;}
            try {
                Thread.sleep(500);
            }catch (Exception e){
                int e2=2;
            }
        }*/
        return jsonStr;
    }

    public JsonGetter(){

        new Thread(new Runnable() {

            @Override
            public void run() {
                jsonStr=iniStr();
            }

        }).start();
    }


    private String getUrl(){
        final String urlStr="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=";
        String result= urlStr+getFrom();
        result=result+"&endtime="+getTo();
        Log.i("getUrl",result);
        return result;
    }

    private String getFrom(){
        long cur= System.currentTimeMillis();
        long last=cur-86400000*7;
        return transTime(last);
    }

    private String getTo(){
        return transTime(System.currentTimeMillis());
    }

    private String transTime(long stamp){
        Log.d("transTime",stamp+"");
        final String GSTIME="yyyy-MM-dd";
        String str;
        SimpleDateFormat unix_time=new SimpleDateFormat(GSTIME);
        str=unix_time.format(new Date(stamp));

        return str;
    }

    String iniStr(){
        String line=null;
        HttpURLConnection connection=null;
        InputStream inputStream=null;
        try {
        URL url=new URL(getUrl());
        connection=(HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
       // connection.setConnectTimeout(15000);
        //connection.setReadTimeout(10000);
        connection.connect();
        inputStream=connection.getInputStream();

        StringBuffer sb=new StringBuffer();        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));

        while ((line = reader.readLine()) != null){
            sb.append(line);
        }
        return sb.toString();
    }catch (MalformedURLException e){

        return "unable to fetch json.";
    }catch (IOException ioe){

        return "IOExcepiton.";
    }finally {
        if(connection!=null){
            connection.disconnect();
        }
        try {
            if(line!=null){
                inputStream.close();
            }
        }catch (IOException e){
            //TODO:handle.
        }

    }

    }

}
