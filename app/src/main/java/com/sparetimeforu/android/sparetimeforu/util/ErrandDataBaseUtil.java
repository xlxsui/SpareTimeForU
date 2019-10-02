package com.sparetimeforu.android.sparetimeforu.util;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;

import org.litepal.LitePal;

import java.util.List;


/**
 * Created by HQY on 2019/6/28.
 */

//数据库操作类
public class ErrandDataBaseUtil {

    //将任务存到任务数据库中
    public static boolean save_errand_data(List<Errand> errands){
        for(int i=0;i<errands.size();i++){
            Errand errand=errands.get(i);
            List<Errand> found_errand=LitePal.where("errand_id="+errand.getErrand_id()).find(Errand.class);//找到相应的任务
            if(found_errand.size()<1){//没有找到该任务，说明没有存过
                errand.save();
            }else{
                errand.update(found_errand.get(0).getId());//更新数据
            }
        }
        List<Errand> errands1=LitePal.findAll(Errand.class);
        return true;
    }
    //取适当个数的任务返回
    public static List getErrand_data(int origin,String destination){//找到数据库中第origin条任务，以此为起点返回6个任务数据
        if(origin<1)
            origin=1;
        List <Errand> errands;
        if(destination.equals("随机")) {
            errands = LitePal.where("is_received = 0")
                    .order("errand_id desc")
                    .limit(6)
                    .offset(origin - 1)
                    .find(Errand.class);
        }
        else
            errands= LitePal.where("destination = ?  and is_received = 0",destination)
                    .order("errand_id desc")
                    .limit(6)
                    .offset(origin-1)
                    .find(Errand.class);
        for (int i=0;i<errands.size();i++){
            Log.i("test2","GET:"+errands.get(i).getId()+errands.get(i).getContent());
        }
        List<Errand> errands2=LitePal.findAll(Errand.class);

        return errands;
    }
    //更新数据
    public static boolean update_Errand_data(List<Errand> errands){
        for (int i=0;i<errands.size();i++){
            Errand errand=errands.get(i);
            List<Errand> found_errand=LitePal.where("errand_id="+errand.getErrand_id()).find(Errand.class);//找到相应的任务
            if(found_errand.size()!=0){
                //errand.update(found_errand.get(0).getId());
            }
            //errand.update();
        }
        return true;
    }
    public static int Get_biggest_errand_id(){//获取最大的任务id
        List<Errand> errands=LitePal.order("errand_id desc").find(Errand.class);
        if(errands.size()==0){//数据库中没有任务，返回-1
            return 0;
        }else{
            return errands.get(0).getErrand_id();
        }
    }
    public static boolean delete_All_Data(){//删除所有数据
        LitePal.deleteAll(Errand.class);
        return true;
    }
    /*
    * 删除所有的已被接受的任务
    * */
    public static boolean delete_Received_errand(){
        LitePal.deleteAll(Errand.class,"is_received=1");//删除所有已经被接受的任务
        return true;
    }
}
