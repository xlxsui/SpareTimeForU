package com.sparetimeforu.android.sparetimeforu.data;

/**
 * Created by Jin on 2018/11/11.
 */


import com.sparetimeforu.android.sparetimeforu.entity.Status;

import java.util.ArrayList;
import java.util.List;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class DataServer {


    private DataServer() {
    }

    public static List<Status> getSampleData(int lenth) {
        List<Status> list = new ArrayList<>();
        for (int i = 0; i < lenth; i++) {
            Status status = new Status();

            list.add(status);
        }
        return list;
    }

}
