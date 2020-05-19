package com.kitadigi.poskita.printer;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.printer.PrinterHelper;


public class RV_bt extends RecyclerView.Adapter<RV_bt.RV_btViewHolder> {

    private List<BTDeviceModel> btDeviceModelList;
    private int rowLayout;
    private Context context;
    private static String nama, uuid, status;


    //untuk apply font
    Typeface fonts, fontsItalic, fontsBold;


    public void initFont(){
        //init fonts
        fonts                           = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        fontsItalic                     = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        fontsBold                       = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");

    }


    public static class RV_btViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvNama, tvUUID, tvStatus, tvTipe, tvAlamat;
        List<BTDeviceModel> btDeviceModelList = new ArrayList<BTDeviceModel>();
        Context context;
        BluetoothDevice bluetoothDevice;
        ImageButton ib_connect,ib_disconnect;

        //untuk nyimpan nama printer ke SQLite
        PrinterHelper printerHelper;



        public RV_btViewHolder(View v, final Context context, List<BTDeviceModel> btDeviceModelList) {

            super(v);
            this.btDeviceModelList = btDeviceModelList;
            this.context = context;
            v.setOnClickListener(this);

            tvUUID=(TextView)v.findViewById(R.id.tvUUID);
            tvNama=(TextView)v.findViewById(R.id.tvNama);
            tvStatus=(TextView)v.findViewById(R.id.tvStatus);
            tvAlamat=(TextView)v.findViewById(R.id.tvAlamat);
            tvTipe=(TextView)v.findViewById(R.id.tvTipe);

            ib_connect=(ImageButton)v.findViewById(R.id.ib_connect_bt);
            ib_disconnect=(ImageButton)v.findViewById(R.id.ib_disconnect_bt);

            //init sqlite untuk nyimpan nama printer
            printerHelper = new PrinterHelper(context);


        }



        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            BTDeviceModel btDeviceModel = this.btDeviceModelList.get(position);
            nama= btDeviceModel.getName();
            uuid = btDeviceModel.getEXTRAUUID();
            status = String.valueOf(btDeviceModel.getBONDBONDED());
        }

    }



    public RV_bt(List<BTDeviceModel> btDeviceModelList, int rowLayout, Context context) {
        this.btDeviceModelList = btDeviceModelList;
        this.rowLayout = rowLayout;
        this.context = context;
    }



    @Override

    public RV_bt.RV_btViewHolder onCreateViewHolder(final ViewGroup parent,
                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new RV_btViewHolder(view, context, btDeviceModelList);

    }



    @Override

    public void onBindViewHolder(final RV_btViewHolder holder, final int position) {

        initFont();
        holder.setIsRecyclable(false);

        //setting font
        holder.tvNama.setTypeface(fonts);
        holder.tvUUID.setTypeface(fonts);
        holder.tvTipe.setTypeface(fonts);
        holder.tvAlamat.setTypeface(fonts);
        holder.tvStatus.setTypeface(fonts);

        //setting nilai
        holder.tvNama.setText(btDeviceModelList.get(position).getName());
        holder.tvUUID.setText(btDeviceModelList.get(position).getUUID());
        holder.tvTipe.setText(btDeviceModelList.get(position).getTipe());
        holder.tvAlamat.setText(btDeviceModelList.get(position).getAddress());
        holder.tvStatus.setText(String.valueOf(btDeviceModelList.get(position).getBONDBONDED()));

        holder.bluetoothDevice = btDeviceModelList.get(position).getBluetoothDevice();

        //tombol untuk konek ke BT
        holder.ib_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.bluetoothDevice.createBond();

                if (holder.bluetoothDevice.createBond()==true){
                    //simpan nama printer di sqlite
                    holder.printerHelper.simpanNamaPrinter(btDeviceModelList.get(position).getName(),btDeviceModelList.get(position).getAddress());
                }else{
                    //jika sudah ter-pair
                    Toast.makeText(context,context.getResources().getString(R.string.sudah_tersambung) + " : " + btDeviceModelList.get(position).getName(),Toast.LENGTH_SHORT).show();
                    holder.printerHelper.simpanNamaPrinter(btDeviceModelList.get(position).getName(),btDeviceModelList.get(position).getAddress());
                }


            }
        });

        //tombol untuk diskonek BT
        holder.ib_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //kode untuk men-disconnect bluetooth
                    Method method = holder.bluetoothDevice.getClass().getMethod("removeBond", (Class[]) null);
                    method.invoke(holder.bluetoothDevice, (Object[]) null);

                    Toast.makeText(context,context.getResources().getString(R.string.bluetooth_diputuskan),Toast.LENGTH_SHORT).show();

                    //hapus nama printer di sqlite
                    holder.printerHelper.hapusSemuaPrinter();

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return btDeviceModelList.size();
    }

}