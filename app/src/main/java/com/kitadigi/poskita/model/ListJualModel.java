package com.kitadigi.poskita.model;

import java.util.Comparator;
import java.util.List;

public class ListJualModel {

    List<JualModel> jualModels;


    public ListJualModel() {
    }

    public ListJualModel(List<JualModel> jualModels) {
        this.jualModels = jualModels;
    }

    public List<JualModel> getJualModels() {
        return jualModels;
    }

    public void setJualModels(List<JualModel> jualModels) {
        this.jualModels = jualModels;
    }


    public static Comparator<JualModel> jualModelComparator = new Comparator<JualModel>() {
        public int compare(JualModel s1, JualModel s2) {
            String dt1 = s1.getId().toUpperCase();
            String dt2 = s2.getId().toUpperCase();

            //ascending order
            return dt1.compareTo(dt2);

            //descending order
            //return StudentName2.compareTo(StudentName1);

        }

    };
}


