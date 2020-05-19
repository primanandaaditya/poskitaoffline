package com.kitadigi.poskita.fragment.report.list;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitadigi.poskita.R;

import java.util.List;

public class DaftarReportAdapter extends BaseAdapter {

    private Context context;
    private ReportFragment reportFragment;
    private LayoutInflater inflater;
    private List<String> daftarReport;


    //untuk apply font
    Typeface fonts, fontsItalic, fontsBold;


    public void initFont(){
        //init fonts
        fonts                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

    }

    public DaftarReportAdapter(Context context,ReportFragment reportFragment, List<String> daftarReport) {
        this.context=context;
        this.reportFragment = reportFragment;
        this.daftarReport = daftarReport;

        initFont();
    }

    @Override
    public int getCount() {
        return daftarReport.size();
    }

    @Override
    public Object getItem(int location) {
        return daftarReport.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) reportFragment.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.listview_report, null);


        //find id
        TextView tv_judul_report = (TextView) convertView.findViewById(R.id.tv_judul_report);

        //apply font ke widget
        tv_judul_report.setTypeface(fonts);

        // getting movie data for the row
        String string = daftarReport.get(position);
        tv_judul_report.setText(string);

        return convertView;
    }
}
