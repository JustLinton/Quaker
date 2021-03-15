package com.berrysdu.earthquake;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class QInfoArrayAdapter extends ArrayAdapter<QuakeInfo> {

    private ArrayList<QuakeInfo> infoList;

    public QInfoArrayAdapter(@NonNull Context context, int resource, ArrayList<QuakeInfo> infoList_) {
        super(context, 0,infoList_);
        infoList=infoList_;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listContentView=convertView;
        if(convertView==null){
            listContentView = LayoutInflater.from(getContext()).inflate(R.layout.quake_info_layout,parent,false);
        }

        ((TextView)listContentView.findViewById(R.id.dateMD)).setText(infoList.get(position).getDateLine1());
        ((TextView)listContentView.findViewById(R.id.dateYR)).setText(infoList.get(position).getDateLine2());

        GradientDrawable drawable=  (GradientDrawable) listContentView.findViewById(R.id.circleMag).getBackground();
        drawable.setColor(getColor(infoList.get(position).getLevel()));

        ((TextView)listContentView.findViewById(R.id.levelTextView)).setText(infoList.get(position).getLevel()+"");

        ((TextView)listContentView.findViewById(R.id.cityName)).setText(getCityLine1(infoList.get(position).getCity()));
        ((TextView)listContentView.findViewById(R.id.cityDetail)).setText(getCityLine2(infoList.get(position).getCity()));

        return listContentView;
    }

    String getCityLine1(String str){
        if(str.contains("of")){
            int from= str.indexOf("of");
            String subStr=str.substring(from);
            subStr= subStr.replace(" of","");
            return subStr.replace("of ","");
        }
        return str;
    }

    String getCityLine2(String str){
        if(str.contains("of")){
            int from= str.indexOf("of");
            String subStr=str.substring(0,from);
            String[] kmAndOri=subStr.split(" ");
            if(kmAndOri.length==2){
                return kmAndOri[1]+", "+kmAndOri[0]+" away.";
            }else if(kmAndOri.length==3){
                return kmAndOri[2]+", "+kmAndOri[0]+"km away.";
            }else {return subStr;}

        }
        return "";
    }

    int getColor(float mag_){
        int mag=(int)Math.floor(mag_);
        switch (mag){
            case 0:
            case 1:
                return R.color.magnitude1;
            case 2:
                return R.color.magnitude2;
            case 3:
                return R.color.magnitude3;
            case 4:
                return R.color.magnitude4;
            case 5:
                return R.color.magnitude5;
            case 6:
                return R.color.magnitude6;
            case 7:
                return R.color.magnitude7;
            case 8:
                return R.color.magnitude8;
            case 9:
                return R.color.magnitude9;
            default:
                return R.color.magnitude9;
        }
    }
}
