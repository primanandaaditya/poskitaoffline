package com.kitadigi.poskita.offline;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.kitadigi.poskita.R;
import com.kitadigi.poskita.dao.database.Db;
import com.kitadigi.poskita.dao.kategori.Kategori;
import com.kitadigi.poskita.dao.kategori.KategoriDAO;
import com.kitadigi.poskita.sinkron.kategori.InsertSync;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KategoriActivity extends AppCompatActivity {

    ListView lv;
    Button btnSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);

        lv=(ListView)findViewById(R.id.lv);
        btnSync=(Button)findViewById(R.id.btnSync);

        //init room
        Db database = Room.databaseBuilder(this, Db.class, "mydb")
                .allowMainThreadQueries()
                .build();


        KategoriDAO kategoriDAO = database.getKategoriDAO();
//        Kategori kategori = new Kategori();
//        kategori.setName_category(StringUtil.timeMilis());
//        kategori.setCode_category("");
//        kategori.setSync_insert(0);
//        kategoriDAO.insert(kategori);



//        kategoriDAO.hapusSemuaKategori();


        List<Kategori> items = kategoriDAO.getKategori();
        KategoriAdapter kategoriAdapter=new KategoriAdapter(KategoriActivity.this, items);

        lv.setAdapter(kategoriAdapter);
//        Toast.makeText(KategoriActivity.this,String.valueOf(items.size()),Toast.LENGTH_SHORT).show();

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Future<String> future = new InsertSync(KategoriActivity.this).insert();

                if (future.isDone()){

                    try {
                        String result = future.get();
                        Log.d("hasil future", "Sudah selesai");
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else{

                }


//                InsertKategori insertKategori = new InsertKategori(KategoriActivity.this);
//                insertKategori.syncKategoriInsert();
//                insertKategori.syncKategoriSelect();
//                insertKategori.syncKategoriDelete();
//                insertKategori.syncLastSelect();

            }
        });
    }
}
