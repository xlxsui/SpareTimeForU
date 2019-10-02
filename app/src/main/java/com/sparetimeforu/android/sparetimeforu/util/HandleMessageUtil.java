package com.sparetimeforu.android.sparetimeforu.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.sparetimeforu.android.sparetimeforu.entity.IdleThing;
import com.sparetimeforu.android.sparetimeforu.entity.SearchThing;
import com.sparetimeforu.android.sparetimeforu.entity.Study;
import com.sparetimeforu.android.sparetimeforu.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HQY on 2018/11/15.
 */

public class HandleMessageUtil {

    /**
     * 解析返回的登陆信息用户数据
     *
     * @param responseString
     * @return
     */
    public static User handleLoginMessage(String responseString) {
        Logger.i(responseString);
        if (!TextUtils.isEmpty(responseString)) {
            Gson gson = new Gson();
            User user = null;
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                user = gson.fromJson(jsonObject.getString("data"), User.class);
            } catch (JSONException e) {
                Logger.e(e.toString());
            }
            return user;
        }
        return null;
    }
    public static boolean handleErrandMessage(String responseString) {
        List<Errand> errands=new ArrayList<Errand>();
        if (!TextUtils.isEmpty(responseString)) {
            Gson gson = new Gson();
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                JSONObject data = new JSONObject(jsonObject.getString("data"));
                Logger.i(data.toString());
                int i=0;
                while(i<data.length()){
                    String str=(String)data.getString("post_"+i);
                    Errand errand=gson.fromJson(str,Errand.class);
                    errands.add(errand);
                    i++;
                }
                //errands =gson.fromJson(jsonObject.getString("data"), new TypeToken<List<Errand>>(){}.getType());
            } catch (JSONException e) {
                Logger.e(e.toString());
            }
            return  ErrandDataBaseUtil.save_errand_data(errands);
        }
        return false;
    }
    public static boolean handleIdleThingMessage(String responseString){
        List<IdleThing> idleThings=new ArrayList<IdleThing>();
        if (!TextUtils.isEmpty(responseString)) {
            Gson gson = new Gson();
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                JSONObject data = new JSONObject(jsonObject.getString("data"));
                Logger.i(data.toString());
                int i=0;
                while(i<data.length()){
                    String str=(String)data.getString("post_"+i);
                    IdleThing idleThing=gson.fromJson(str,IdleThing.class);
                    idleThings.add(idleThing);
                    i++;
                }
                //errands =gson.fromJson(jsonObject.getString("data"), new TypeToken<List<Errand>>(){}.getType());
            } catch (JSONException e) {
                Logger.e(e.toString());
            }
            return  IdleThingDataBaseUtil.save_idlething_data(idleThings);
        }

        return false;
    }
    public static boolean handleSearchThingMessage(String responseString){
        List<SearchThing> searchThings=new ArrayList<SearchThing>();
        if (!TextUtils.isEmpty(responseString)) {
            Gson gson = new Gson();
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                JSONObject data = new JSONObject(jsonObject.getString("data"));
                Logger.i(data.toString());
                int i=0;
                while(i<data.length()){
                    String str=(String)data.getString("post_"+i);
                    SearchThing searchThing=gson.fromJson(str,SearchThing.class);
                    searchThings.add(searchThing);
                    i++;
                }
                //errands =gson.fromJson(jsonObject.getString("data"), new TypeToken<List<Errand>>(){}.getType());
            } catch (JSONException e) {
                Logger.e(e.toString());
            }
            return  SearchThingBaseUtil.save_SearchThing_data(searchThings);
        }

        return false;
    }
    public static boolean handleStudyMessage(String responseString){
        List<Study> studies=new ArrayList<Study>();
        if (!TextUtils.isEmpty(responseString)) {
            Gson gson = new Gson();
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                JSONObject data = new JSONObject(jsonObject.getString("data"));
                Logger.i(data.toString());
                int i=0;
                while(i<data.length()){
                    String str=(String)data.getString("post_"+i);
                    Study study=gson.fromJson(str,Study.class);
                    studies.add(study);
                    i++;
                }
                //errands =gson.fromJson(jsonObject.getString("data"), new TypeToken<List<Errand>>(){}.getType());
            } catch (JSONException e) {
                Logger.e(e.toString());
            }
            return  StudyDataBaseUtil.save_Study_data(studies);
        }

        return false;
    }

}
