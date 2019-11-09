package com.sparetimeforu.android.sparetimeforu.util;

import com.sparetimeforu.android.sparetimeforu.entity.SearchThing;
import com.sparetimeforu.android.sparetimeforu.entity.Study;

import org.litepal.LitePal;

import java.util.List;

/**
 * Created by HQY on 2019/10/2.
 */

public class StudyDataBaseUtil {
    public static boolean save_Study_data(List<Study> studies){
        for(int i=0;i<studies.size();i++){
            Study study=studies.get(i);
            List<Study> found_Study= LitePal.where("study_id="+study.getStudy_id()).find(Study.class);//找到相应的任务
            if(found_Study.size()<1){//没有找到该任务，说明没有存过
                study.save();
            }else{
                if (study.getLike_number() == 0) {
                    study.setToDefault("like_number");
                }
                study.update(found_Study.get(0).getId());//更新数据
            }
        }
        List<Study> studies1=LitePal.findAll(Study.class);
        return true;
    }
    //取适当个数的任务返回
    public static List getStudy_data(int origin){//找到数据库中第origin条任务，以此为起点返回6个任务数据
        if(origin<1)
            origin=1;
        List <Study> studies;

        studies = LitePal
                .order("study_id desc")
                .limit(6)
                .offset(origin - 1)
                .find(Study.class);
        List<Study> studies2=LitePal.findAll(Study.class);
        return studies;
    }
    //更新数据
    public static boolean update_Study_data(List<Study> studies){
        for (int i=0;i<studies.size();i++){
            Study study=studies.get(i);
            List<Study> found_Study=LitePal.where("study_id="+study.getStudy_id()).find(Study.class);//找到相应的任务
            if(found_Study.size()!=0){
                //errand.update(found_errand.get(0).getId());
            }
            //errand.update();
        }
        return true;
    }
    public static int Get_biggest_Study_id(){//获取最大的任务id
        List<Study> studies=LitePal.order("study_id desc").find(Study.class);
        if(studies.size()==0){//数据库中没有任务，返回-1
            return 0;
        }else{
            return studies.get(0).getStudy_id();
        }
    }
    public static boolean delete_All_Data(){//删除所有数据
        LitePal.deleteAll(Study.class);
        return true;
    }
}
