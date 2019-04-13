package com.sparetimeforu.android.sparetimeforu.data;

/**
 * Created by Jin on 2018/11/11.
 */


import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.sparetimeforu.android.sparetimeforu.entity.IdleThing;
import com.sparetimeforu.android.sparetimeforu.entity.SearchThing;
import com.sparetimeforu.android.sparetimeforu.entity.Study;

import java.util.ArrayList;
import java.util.List;


/**
 * 拿出对应的实体
 */
public class DataServer {


    private DataServer() {
    }

    public static List<Errand> getErrandData(int length) {
        List<Errand> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Errand errand = new Errand();

            list.add(errand);
        }
        return list;
    }

    public static List<Study> getStudyData(int length) {
        List<Study> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Study study = new Study();

            list.add(study);
        }
        return list;
    }

    public static List<IdleThing> getIdleThingData(int length) {
        List<IdleThing> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            IdleThing idleThing = new IdleThing();

            list.add(idleThing);
        }
        return list;

    }

    public static List<SearchThing> getSearchThingData(int length) {
        List<SearchThing> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            SearchThing searchThing = new SearchThing();

            list.add(searchThing);
        }
        return list;
    }

}
