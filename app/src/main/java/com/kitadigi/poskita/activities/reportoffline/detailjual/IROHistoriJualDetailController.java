package com.kitadigi.poskita.activities.reportoffline.detailjual;

import android.content.Context;

import com.kitadigi.poskita.activities.reportoffline.jual.IROHistoriJualRequest;
import com.kitadigi.poskita.activities.reportoffline.jual.IROHistoriJualResult;
import com.kitadigi.poskita.dao.jualdetail.JualDetail;
import com.kitadigi.poskita.dao.jualdetail.JualDetailHelper;
import com.kitadigi.poskita.dao.jualmaster.JualMaster;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;

import java.util.ArrayList;
import java.util.List;

public class IROHistoriJualDetailController implements IROHistoriJualDetailRequest {

    Context context;
    IROHistoriJualDetailResult result;

    JualDetailHelper jualDetailHelper;
    List<JualDetail> jualDetails;
    JualDetail jualDetail;

    List<DetailJualModel> detailJualModels;
    DetailJualModel detailJualModel;

    ItemHelper itemHelper;
    Item item;

    String kode_id;
    Integer qty,price,subtotal;

    public IROHistoriJualDetailController(Context context, IROHistoriJualDetailResult result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public void getReport(String nomor_transaksi) {

        //itemHelpepr buat menemukan nama produk dan gambar
        //soalnya di tabel jual_detail yang disimpan cuma mobile_id aja
        //lihat definisi modelnya di DetailJualModel.java
        itemHelper=new ItemHelper(context);

        //init helper untuk menemukan data penjualan detail
        jualDetailHelper =new JualDetailHelper(context);

        //ini array buat nampunb
        detailJualModels=new ArrayList<>();


        try {

            //dapetin row di penjualan detail berdasarkan nomor transaksi
            jualDetails = jualDetailHelper.getJualDetailByNomor(nomor_transaksi);

            //looping jualDetails, mau ditransfer ke detailJualModel
            for (JualDetail jualDetail: jualDetails){

                //tampung mobile_id
                kode_id = jualDetail.getKode_id();
                qty=jualDetail.getQty();
                price=jualDetail.getPrice();
                subtotal=qty * price;

                detailJualModel = new DetailJualModel();
                detailJualModel.setId(jualDetail.getId());
                detailJualModel.setKode_id(kode_id);
                detailJualModel.setNomor(nomor_transaksi);
                detailJualModel.setPrice(price);
                detailJualModel.setQty(qty);
                detailJualModel.setSubtotal(subtotal);

                //cari nama produk && image
                item = itemHelper.getItemByKodeId(kode_id);
                detailJualModel.setNama_produk(item.getName_product());
                detailJualModel.setGambar(item.getImage());


                //tambahkan ke array
                detailJualModels.add(detailJualModel);
            }


            result.onHistoriJualDetailSuccess(detailJualModels);
        }catch (Exception e){
            result.onHistoriJualDetailError(e.getMessage());
        }
    }

    @Override
    public Integer getTotalItem(List<DetailJualModel> detailJualModels) {
        Integer hasil;
        Integer qty;

        //looping untuk get qty
        if (detailJualModels.size()==0){
            hasil=0;
        }else{
            hasil =0;
            for (DetailJualModel detailJualModel: detailJualModels){
                qty = detailJualModel.getQty();
                hasil = hasil + qty;
            }
        }

        return hasil;
    }

    @Override
    public Integer getGrandTotal(List<DetailJualModel> detailJualModels) {
        Integer hasil;
        Integer total;

        //looping untuk get qty
        if (detailJualModels.size()==0){
            hasil=0;
        }else{
            hasil =0;
            for (DetailJualModel detailJualModel: detailJualModels){
                total = detailJualModel.getSubtotal();
                hasil = hasil + total;
            }
        }

        return hasil;
    }
}
