package com.kitadigi.poskita.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.model.TransactionDates;
import com.kitadigi.poskita.model.TransactionsList;
import com.kitadigi.poskita.util.DividerItemDecoration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class TransactionReportAdapter extends RecyclerView.Adapter<TransactionReportAdapter.MyViewHolder> {

    //private List<Movie> moviesList;

    private TransactionsList transactionsList;
    private Context mContext;

    Calendar calendar;
    SimpleDateFormat today;
    String now;


    public TransactionReportAdapter(Context mContext, TransactionsList transactionsList) {
        this.transactionsList = transactionsList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parent_recycler, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TimeZone tz                 = TimeZone.getTimeZone("GMT+0700");
        calendar                    = Calendar.getInstance(tz);

        today                       = new SimpleDateFormat("yyyy-MM-dd");
        now                         = today.format(calendar.getTime());

        TransactionDates eventDates = transactionsList.getTransactionDatesArrayList().get(position);

        if (eventDates.getDate().equals(now)){
            holder.tv_title_header.setText("Hari Ini");
        }else{
            holder.tv_title_header.setText(eventDates.getDate());
        }


        holder.rv_child.setHasFixedSize(true);
        holder.rv_child.setItemViewCacheSize(20);
        holder.rv_child.setDrawingCacheEnabled(true);
        holder.rv_child.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        holder.rv_child.setLayoutManager(mLayoutManager);
        holder.rv_child.setItemAnimator(new DefaultItemAnimator());
        holder.rv_child.addItemDecoration(new DividerItemDecoration(LinearLayoutManager.VERTICAL, ContextCompat.getDrawable(mContext, R.drawable.item_decorator)));

        TransactionReportListAdapter transactionReportListAdapter = new TransactionReportListAdapter(mContext,transactionsList.getTransactionDatesArrayList().get(position).getTransactionsDetailArrayList());
        holder.rv_child.setAdapter(transactionReportListAdapter);

    }

    @Override
    public int getItemCount() {
        return transactionsList.getTransactionDatesArrayList().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title_header;
        public RecyclerView rv_child;

        public MyViewHolder(View view) {
            super(view);
            tv_title_header     = view.findViewById(R.id.tv_title_header);
            rv_child            = view.findViewById(R.id.rv_child);
        }
    }
}