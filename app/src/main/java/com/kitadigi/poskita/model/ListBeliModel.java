package com.kitadigi.poskita.model;

import java.util.Comparator;
import java.util.List;

public class ListBeliModel {

    List<BeliModel> beliModels;

    public ListBeliModel() {
    }

    public ListBeliModel(List<BeliModel> beliModels) {
        this.beliModels = beliModels;
    }

    public List<BeliModel> getBeliModels() {
        return beliModels;
    }

    public void setBeliModels(List<BeliModel> beliModels) {
        this.beliModels = beliModels;
    }


    public static Comparator<BeliModel> beliModelComparator = new Comparator<BeliModel>() {
        public int compare(BeliModel s1, BeliModel s2) {
            String dt1 = s1.getId().toUpperCase();
            String dt2 = s2.getId().toUpperCase();

            //ascending order
            return dt1.compareTo(dt2);

            //descending order
            //return StudentName2.compareTo(StudentName1);

        }

    };
}
