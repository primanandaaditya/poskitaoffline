package com.kitadigi.poskita.printer;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitadigi.poskita.R;

import java.util.List;

public class PairedBTAdapter extends BaseAdapter {


    //untuk apply font
    Typeface fonts, fontsItalic, fontsBold;


    public void initFont(){
        //init fonts
        fonts                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

    }

    List<PairedBTModel> pairedBTModels;
    Context context;
    private LayoutInflater inflater;

    public PairedBTAdapter(List<PairedBTModel> pairedBTModels, Context context) {
        this.pairedBTModels = pairedBTModels;
        this.context = context;

        initFont();
    }

    @Override
    public int getCount() {
        return pairedBTModels.size();
    }

    @Override
    public Object getItem(int position) {
        return pairedBTModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_paired_bluetooth, null);



        TextView tv_nama_bluetooth = (TextView) convertView.findViewById(R.id.tv_nama_bluetooth);
        TextView tv_address = (TextView) convertView.findViewById(R.id.tv_address);


        // getting movie data for the row
        final PairedBTModel pairedBTModel= pairedBTModels.get(position);

        //aply font
        tv_nama_bluetooth.setTypeface(fonts);
        tv_address.setTypeface(fonts);



        tv_nama_bluetooth.setText( pairedBTModel.getNama_bluetooth());
        tv_address.setText(pairedBTModel.getAddress());

        return convertView;
    }
}
