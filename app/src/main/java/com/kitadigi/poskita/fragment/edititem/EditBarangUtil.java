package com.kitadigi.poskita.fragment.edititem;

import com.kitadigi.poskita.retrofit.RetrofitClient;
import com.kitadigi.poskita.util.Url;

public class EditBarangUtil {

        private EditBarangUtil() {}

        public static final String BASE_URL = Url.DIKI_URL;

        public static IEditBarang getInterface() {

            return RetrofitClient.getClient(BASE_URL).create(IEditBarang.class);
        }
}
