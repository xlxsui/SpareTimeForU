package com.sparetimeforu.android.sparetimeforu.util;

import android.util.Log;

import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.sparetimeforu.android.sparetimeforu.entity.SystemMessage;

import org.litepal.LitePal;

import java.util.List;

/**
 * Created by HQY on 2019/10/19.
 */

public class SystemDataBaseUtil {
    public static boolean save_systemMessage_data(List<SystemMessage> systemMessages){
        for(int i=0;i<systemMessages.size();i++){
            SystemMessage systemMessage=systemMessages.get(i);
            List<Errand> found_errand= LitePal.where("errand_id="+systemMessage.getMessage_id()).find(Errand.class);//找到相应的任务
            if(found_errand.size()<1){//没有找到该任务，说明没有存过
                systemMessage.save();
            }else{
                systemMessage.update(found_errand.get(0).getId());//更新数据
            }
        }
        return true;
    }
    //取适当个数的任务返回
    public static List getSystemMessage_data(int id){

        List <SystemMessage> systemMessages;
        if(id==0)
        systemMessages = LitePal
                .order("date desc")
                .limit(6)
                .find(SystemMessage.class);
        else systemMessages = LitePal.where("Message_id<"+id)
                .order("date")
                .limit(6)
                .find(SystemMessage.class);
        return systemMessages;
    }
}
