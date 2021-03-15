package com.berrysdu.earthquake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayList<QuakeInfo> quakeInfos = new ArrayList<>();
    ArrayList<QuakeInfo> tmpInfo=new ArrayList<>();
    int minMag=4;
    boolean usedBtn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        initList();

        //onCreate结束后，或者说onResume，才显示到activity的内容。
    }



    void initList(){
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        ArrayAdapter<QuakeInfo> adapter = new QInfoArrayAdapter(this,R.layout.quake_info_layout,quakeInfos);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url=quakeInfos.get(position).getUrl();
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                startActivity(intent);
            }
        });
        initBtns(earthquakeListView);
    }

    void initialize(){
        JsonGetter getter=new JsonGetter();

        if(havePermission()){
            Log.d("Main","havePerm.");
        }

        // Create a fake list of earthquake locations.

        initInfosArray(quakeInfos,getter.getJsonStr());
    }

    void initInfosArray(ArrayList<QuakeInfo> quakeInfos,String jsonStr){
        Log.d("MainArrayIni",jsonStr);
        JSONObject rootObj=null;
        int count=0;
        try{rootObj=new JSONObject(jsonStr);
            count=rootObj.getJSONObject("metadata").getInt("count");
        }catch (JSONException e){
            Toast.makeText(this,"JSONException at "+"root",Toast.LENGTH_SHORT).show();
        }

        if(rootObj!=null&&count!=0){
            for(int i=0;i<=count-1;i++){
                try{
                    JSONObject propObj=rootObj.getJSONArray("features").getJSONObject(i).getJSONObject("properties");
                    String city=propObj.getString("place");
                    long timeStmp=propObj.getLong("time");
                    float level=(float) propObj.getDouble("mag");
                    String url=propObj.getString("url");
                    QuakeInfo info=new QuakeInfo(level,city,getDL(timeStmp,1),getDL(timeStmp,2),url);
                    info.setDateLong(timeStmp);
                    quakeInfos.add(info);
                }catch (JSONException e){
                    Toast.makeText(this,"JSONException at "+i,Toast.LENGTH_SHORT).show();
                }
            }
            Toast.makeText(this,"Found "+count+" earth quakes.",Toast.LENGTH_SHORT).show();
        }else {
            QuakeInfo info=new QuakeInfo(0,"Unable to load..."," "," ","berrysdu.com");
        }
    }

    void initBtns(final ListView listView){
        Button upBtn=(Button)findViewById(R.id.upBtn);
        Button dnBtn=(Button)findViewById(R.id.dnBtn);
        Button magBtn=(Button)findViewById(R.id.magPxBtn);
        Button dateBtn=(Button)findViewById(R.id.datePxBtn);


        magBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<QuakeInfo> thisList;
                if(usedBtn){thisList=tmpInfo;}else {thisList=quakeInfos;}
                for(int i=0;i<=4;i++){
                    thisList=sortQuakeList(thisList,0);
                }
                Toast.makeText(MainActivity.this,"sort by magnitude.",Toast.LENGTH_LONG).show();
                listView.setAdapter(new QInfoArrayAdapter(MainActivity.this,R.id.list,tmpInfo));
            }
        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<QuakeInfo> thisList;
                if(usedBtn){thisList=tmpInfo;}else {thisList=quakeInfos;}
                for(int i=0;i<=4;i++){
                    thisList=sortQuakeList(thisList,1);
                }
                Toast.makeText(MainActivity.this,"sort by date.",Toast.LENGTH_LONG).show();
                listView.setAdapter(new QInfoArrayAdapter(MainActivity.this,R.id.list,tmpInfo));
            }
        });


        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usedBtn=true;
                ArrayList<QuakeInfo> tmpppInfo=tmpInfo;
                tmpInfo.clear();
                minMag++;
                for(QuakeInfo quakeInfo:quakeInfos){
                    if(quakeInfo.getLevel()>=minMag&&quakeInfo.getLevel()<=minMag+1){
                        tmpInfo.add(quakeInfo);
                    }
                }
                if(tmpppInfo.size()==0){
                    listView.setAdapter(new QInfoArrayAdapter(MainActivity.this,R.id.list,tmpppInfo));
                    minMag--;
                    Toast.makeText(MainActivity.this,"no more infomation.",Toast.LENGTH_SHORT).show();
                }else {
                    listView.setAdapter(new QInfoArrayAdapter(MainActivity.this,R.id.list,tmpInfo));
                }

                upDateMinMagView();
            }
        });

        dnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(minMag!=0){
                    usedBtn=true;
                    tmpInfo.clear();
                    minMag--;
                    for(QuakeInfo quakeInfo:quakeInfos){
                        if(quakeInfo.getLevel()>=minMag&&quakeInfo.getLevel()<=minMag+1){
                            tmpInfo.add(quakeInfo);
                        }
                    }
                    listView.setAdapter(new QInfoArrayAdapter(MainActivity.this,R.id.list,tmpInfo));
                    upDateMinMagView();
                }else {
                    Toast.makeText(MainActivity.this,"no more infomation.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView minMagView=findViewById(R.id.minMag);
        minMagView.setText("-");

    }

    void upDateMinMagView(){
        TextView minMagView=findViewById(R.id.minMag);
        minMagView.setText(minMag+"");
    }


    private String getDL(long stamp,int i){
        Log.d("transTime",stamp+"");
        String GSTIME="yyyy-MM-dd HH:mm:ss";
        switch (i){
            case 1:GSTIME="MMM dd";
                break;
            case 2:GSTIME="yyyy";
                break;
        }

        String str;
        SimpleDateFormat unix_time=new SimpleDateFormat(GSTIME);
        str=unix_time.format(new Date(stamp));

        return str;
    }

    private boolean havePermission(){
        int result= ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        return result== PackageManager.PERMISSION_GRANTED;
    }

    private ArrayList<QuakeInfo> sortQuakeList(ArrayList<QuakeInfo> infoList_,int type){
        if(type==0){
            for(int i=0;i<=infoList_.size()-1;i++){
                for(int j=i;j<=infoList_.size()-2;j++){
                    if(infoList_.get(j).getLevel()<=infoList_.get(j+1).getLevel()){
                        QuakeInfo tmpInfo=infoList_.get(j);
                        infoList_.set(j,infoList_.get(j+1));
                        infoList_.set(j+1,tmpInfo);
                    }
                }
            }
            return infoList_;
        }else {
            for(int i=0;i<=infoList_.size()-1;i++){
                for(int j=i;j<=infoList_.size()-2;j++){
                    if(infoList_.get(j).getDateLong()<=infoList_.get(j+1).getDateLong()){
                        QuakeInfo tmpInfo=infoList_.get(j);
                        infoList_.set(j,infoList_.get(j+1));
                        infoList_.set(j+1,tmpInfo);
                    }
                }
            }
            return infoList_;
        }
    }

}