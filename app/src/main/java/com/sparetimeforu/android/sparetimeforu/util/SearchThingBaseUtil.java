package com.sparetimeforu.android.sparetimeforu.util;

import com.sparetimeforu.android.sparetimeforu.entity.SearchThing;

import org.litepal.LitePal;

import java.util.List;

/**
 * Created by HQY on 2019/10/2.
 */

public class SearchThingBaseUtil {
    //将任务存到任务数据库中
    public static boolean save_SearchThing_data(List<SearchThing> SearchThings){
        for(int i=0;i<SearchThings.size();i++){
            SearchThing searchThing=SearchThings.get(i);
            List<SearchThing> found_SearchThing= LitePal.where("search_thing_id="+searchThing.getSearch_thing_id()).find(SearchThing.class);//找到相应的任务
            if(found_SearchThing.size()<1){//没有找到该任务，说明没有存过
                searchThing.save();
            }else{
                searchThing.update(found_SearchThing.get(0).getId());//更新数据
            }
        }
        List<SearchThing> SearchThings1=LitePal.findAll(SearchThing.class);
        return true;
    }
    //取适当个数的任务返回
    public static List getSearchThing_data(int origin){//找到数据库中第origin条任务，以此为起点返回6个任务数据
        if(origin<1)
            origin=1;
        List <SearchThing> SearchThings;

        SearchThings = LitePal
                .order("search_thing_id desc")
                .limit(6)
                .offset(origin - 1)
                .find(SearchThing.class);
        List<SearchThing> SearchThings2=LitePal.findAll(SearchThing.class);
        return SearchThings;
    }
    //更新数据
    public static boolean update_SearchThing_data(List<SearchThing> SearchThings){
        for (int i=0;i<SearchThings.size();i++){
            SearchThing searchThing=SearchThings.get(i);
            List<SearchThing> found_SearchThing=LitePal.where("search_thing_id="+searchThing.getSearch_thing_id()).find(SearchThing.class);//找到相应的任务
            if(found_SearchThing.size()!=0){
            }
        }
        return true;
    }
    public static int Get_biggest_SearchThing_id(){//获取最大的任务id
        List<SearchThing> SearchThings=LitePal.order("search_Thing_id desc").find(SearchThing.class);
        if(SearchThings.size()==0){//数据库中没有任务，返回-1
            return 0;
        }else{
            return SearchThings.get(0).getSearch_thing_id();
        }
    }
    public static boolean delete_All_Data(){//删除所有数据
        LitePal.deleteAll(SearchThing.class);
        return true;
    }
}
