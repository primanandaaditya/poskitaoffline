package com.kitadigi.poskita.activities.reportoffline.detailbeli;

import android.content.Context;

import com.kitadigi.poskita.activities.reportoffline.detailjual.DetailJualModel;
import com.kitadigi.poskita.activities.reportoffline.detailjual.IROHistoriJualDetailRequest;
import com.kitadigi.poskita.activities.reportoffline.detailjual.IROHistoriJualDetailResult;
import com.kitadigi.poskita.dao.belidetail.BeliDetail;
import com.kitadigi.poskita.dao.belidetail.BeliDetailHelper;
import com.kitadigi.poskita.dao.jualdetail.JualDetail;
import com.kitadigi.poskita.dao.jualdetail.JualDetailHelper;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;

import java.util.ArrayList;
import java.util.List;

public class IROHistoriBeliDetailController implements IROHistoriBeliDetailRequest {

    Context context;
    IROHistoriBeliDetailResult result;

    BeliDetailHelper beliDetailHelper;
    List<BeliDetail> beliDetails;

    List<DetailBeliModel> detailBeliModels;
    DetailBeliModel detailBeliModel;

    ItemHelper itemHelper;
    Item item;

    String kode_id;
    Integer qty,price,subtotal;

    public IROHistoriBeliDetailController(Context context, IROHistoriBeliDetailResult result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public void getReport(String nomor_transaksi) {

        //itemHelpepr buat menemukan nama produk dan gambar
        //soalnya di tabel belidetail yang disimpan cuma mobile_id aja
        //lihat definisi modelnya di DetailBeliModel.java
        itemHelper=new ItemHelper(context);

        //init helper untuk menemukan data pembelian detail
        beliDetailHelper =new BeliDetailHelper(context);

        //ini array buat nampunb
        detailBeliModels=new ArrayList<>();


        try {

            //dapetin row di pembelian detail berdasarkan nomor transaksi
            beliDetails = beliDetailHelper.getBeliByNomor(nomor_transaksi);

            //looping belidetails, mau ditransfer ke detailbeliModel
            for (BeliDetail beliDetail:beliDetails){

                //tampung mobile_id
                kode_id = beliDetail.getKode_id_produk();
                qty=Integer.parseInt(beliDetail.getQty());
                price=Integer.parseInt(beliDetail.getPrice());
                subtotal=qty * price;

                detailBeliModel = new DetailBeliModel();
                detailBeliModel.setId(detailBeliModel.getId());
                detailBeliModel.setKode_id(kode_id);
                detailBeliModel.setNomor(nomor_transaksi);
                detailBeliModel.setPrice(price);
                detailBeliModel.setQty(qty);
                detailBeliModel.setSubtotal(subtotal);

                //cari nama produk && image
                item = itemHelper.getItemByKodeId(kode_id);
                detailBeliModel.setNama_produk(item.getName_product());
                detailBeliModel.setGambar(item.getImage());


                //tambahkan ke array
                detailBeliModels.add(detailBeliModel);
            }


            result.onHistoriBeliDetailSuccess(detailBeliModels);
        }catch (Exception e){
            result.onHistoriBeliDetailError(e.getMessage());
        }
    }

    @Override
    public Integer getTotalItem(List<DetailBeliModel> detailBeliModels) {
        Integer hasil;
        Integer qty;

        //looping untuk get qty
        if (detailBeliModels.size()==0){
            hasil=0;
        }else{
            hasil =0;
            for (DetailBeliModel detailBeliModel:detailBeliModels){
                qty = detailBeliModel.getQty();
                hasil = hasil + qty;
            }
        }

        return hasil;
    }

    @Override
    public Integer getGrandTotal(List<DetailBeliModel> detailBeliModels) {
        Integer hasil;
        Integer total;

        //looping untuk get qty
        if (detailBeliModels.size()==0){
            hasil=0;
        }else{
            hasil =0;
            for (DetailBeliModel detailBeliModel: detailBeliModels){
                total = detailBeliModel.getSubtotal();
                hasil = hasil + total;
            }
        }

        return hasil;
    }
}
