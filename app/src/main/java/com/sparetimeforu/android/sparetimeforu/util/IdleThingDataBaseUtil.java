package com.sparetimeforu.android.sparetimeforu.util;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.sparetimeforu.android.sparetimeforu.entity.IdleThing;

import org.litepal.LitePal;

import java.util.List;

/**
 * Created by HQY on 2019/10/2.
 */

public class IdleThingDataBaseUtil {
    //将任务存到任务数据库中
    public static boolean save_idlething_data(List<IdleThing> idlethings) {
        for (int i = 0; i < idlethings.size(); i++) {
            IdleThing idlething = idlethings.get(i);
            List<IdleThing> found_idlething = LitePal.where("idle_thing_id=" + idlething.getIdle_thing_id()).find(IdleThing.class);//找到相应的任务
            if (found_idlething.size() < 1) {//没有找到该任务，说明没有存过
                idlething.save();
            } else {
                idlething.update(found_idlething.get(0).getId());//更新数据
            }
        }
        List<IdleThing> idlethings1 = LitePal.findAll(IdleThing.class);
        return true;
    }

    //取适当个数的任务返回
    public static List getIdleThing_data(int origin) {//找到数据库中第origin条任务，以此为起点返回6个任务数据
        if (origin < 1)
            origin = 1;
        List<IdleThing> idlethings;

        idlethings = LitePal
                .order("idle_thing_id desc")
                .limit(6)
                .offset(origin - 1)
                .find(IdleThing.class);
        List<IdleThing> idlethings2 = LitePal.findAll(IdleThing.class);
        return idlethings;
    }

    //更新数据
    public static boolean update_IdleThing_data(List<IdleThing> idlethings) {
        for (int i = 0; i < idlethings.size(); i++) {
            IdleThing idlething = idlethings.get(i);
            List<IdleThing> found_idlething = LitePal.where("idle_thing_id=" + idlething.getIdle_thing_id()).find(IdleThing.class);//找到相应的任务
            if (found_idlething.size() != 0) {
                //errand.update(found_errand.get(0).getId());
            }
            //errand.update();
        }
        return true;
    }

    public static int Get_biggest_idlething_id() {//获取最大的任务id
        List<IdleThing> idlethings = LitePal.order("idle_thing_id desc").find(IdleThing.class);
        if (idlethings.size() == 0) {//数据库中没有任务，返回-1
            return 0;
        } else {
            return idlethings.get(0).getIdle_thing_id();
        }
    }

    public static boolean delete_All_Data() {//删除所有数据
        LitePal.deleteAll(IdleThing.class);
        return true;
    }
}
