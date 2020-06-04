package com.kitadigi.poskita.activities.reportoffline.terlaris;

import android.content.Context;

import com.kitadigi.poskita.dao.jualdetail.JualDetailHelper;
import com.kitadigi.poskita.dao.jualmaster.JualMasterHelper;
import com.kitadigi.poskita.dao.models.SumQtyJualDetail;
import com.kitadigi.poskita.dao.produk.Item;
import com.kitadigi.poskita.dao.produk.ItemHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ROTerlarisController implements IROTerlarisRequest {

    Context context;
    IROTerlarisResult iroTerlarisResult;
    ROTerlarisModel roTerlarisModel;
    List<ROTerlarisModel> roTerlarisModels;

    ItemHelper itemHelper;
    JualMasterHelper jualMasterHelper;
    JualDetailHelper jualDetailHelper;

    //var untuk menemukan qty dari kode_id
    SumQtyJualDetail sumQtyJualDetail;

    public ROTerlarisController(Context context, IROTerlarisResult iroTerlarisResult) {
        this.context = context;
        this.iroTerlarisResult = iroTerlarisResult;
    }



    @Override
    public void getReport() {

        try {


            //init sqlite
            itemHelper = new ItemHelper(context);

            //buat array item baru
            //mau diisi dengan item dari sqlite
            roTerlarisModels = new ArrayList<>();

            //get list item
            List<Item> items = itemHelper.getAllItem();

            //looping item
            //untuk mengisi array terlaris
            for (Item item: items){

                //buat obj baru
                roTerlarisModel = new ROTerlarisModel();

                //set tiap properti
                roTerlarisModel.setAdditional(item.getAdditional());
                roTerlarisModel.setBrand_id(item.getBrand_id());
                roTerlarisModel.setBrand_name(item.getBrand_name());
                roTerlarisModel.setCategory_id(item.getCategory_id());
                roTerlarisModel.setCategory_name(item.getCategory_name());
                roTerlarisModel.setCode_product(item.getCode_product());
                roTerlarisModel.setId(item.getId());
                roTerlarisModel.setId_users(item.getId_users());
                roTerlarisModel.setImage(item.getImage());
                roTerlarisModel.setKode_id(item.getKode_id());
                roTerlarisModel.setName_product(item.getName_product());
                roTerlarisModel.setPurchase_price(item.getPurchase_price());
                roTerlarisModel.setQty_minimum(item.getQty_minimum());
                roTerlarisModel.setQty_stock(item.getQty_stock());
                roTerlarisModel.setSell_price(item.getSell_price());
                roTerlarisModel.setTypes(item.getTypes());
                roTerlarisModel.setUnit_id(item.getUnit_id());
                roTerlarisModel.setUnit_name(item.getUnit_name());
                roTerlarisModel.setJumlah_terjual(0);
                roTerlarisModel.setPersentase(0);

                //tambahkan ke array
                roTerlarisModels.add(roTerlarisModel);


            }

            cariTotalQty();

            iroTerlarisResult.onTerlarisSuccess(roTerlarisModels);

        }catch (Exception e){
            iroTerlarisResult.onTerlarisError(e.getMessage());
        }

    }


    void cariTotalQty(){

        //var untuk nampung kode_id waktu looping
        String kode_id;

        Integer total_qty;

        //init sqlite
        jualDetailHelper = new JualDetailHelper(context);

        //nampung query group by kode_id dan sum(qty) dari tabel jualdetail
        List<SumQtyJualDetail> sumQtyJualDetails = jualDetailHelper.getSumQtyJualDetail();


        //looping roterlaris
        for (ROTerlarisModel roTerlarisModel: roTerlarisModels){

            //cari jumlah qty berdasarkan kode_id
            kode_id = roTerlarisModel.getKode_id();
            sumQtyJualDetail = findUsingIterator(kode_id, sumQtyJualDetails);

            //jika tidak ada, kasih nilai '0'
            if (sumQtyJualDetail==null){
                roTerlarisModel.setJumlah_terjual(0);
            }else{
                total_qty = sumQtyJualDetail.getTotal_qty();
                roTerlarisModel.setJumlah_terjual(total_qty);
            }


        }

    }

    public SumQtyJualDetail findUsingIterator(
            String kode_id, List<SumQtyJualDetail> sumQtyJualDetails) {

        Iterator<SumQtyJualDetail> iterator = sumQtyJualDetails.iterator();

        while (iterator.hasNext()) {
            SumQtyJualDetail sumQtyJualDetail = iterator.next();
            if (sumQtyJualDetail.getKode_id().equals(kode_id)) {
                return sumQtyJualDetail;
            }
        }

        return null;
    }
}
