package com.sparetimeforu.android.sparetimeforu.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.entity.Comment;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;
import com.sparetimeforu.android.sparetimeforu.entity.IdleThing;
import com.sparetimeforu.android.sparetimeforu.entity.SearchThing;
import com.sparetimeforu.android.sparetimeforu.entity.Study;
import com.sparetimeforu.android.sparetimeforu.entity.SystemMessage;
import com.sparetimeforu.android.sparetimeforu.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

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

    public static List<Errand> handleReleasedErrandMessage(String responseString) {
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
            return  errands;
        }
        return null;
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
    public static List<IdleThing> handlePostIdleThingMessage(String responseString){
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
            return  idleThings;
        }
        return null;
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

    public static List<SearchThing> handlePostSearchThingMessage(String responseString){
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
            return  searchThings;
        }
        return null;
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
    public static List<Study> handlePostStudyMessage(String responseString){
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
            return  studies;
        }
        return null;
    }

    public static boolean handleSystemMessage(List<Message> messages,int un_read_message_count){
        SystemMessage systemMessage;
        //List<SystemMessage> systemMessages=new ArrayList<SystemMessage>();
        for(int i=messages.size()-un_read_message_count;i<messages.size();i++){
            Message message=messages.get(i);
            systemMessage=system_message_handle(((TextContent)message.getContent()).getText());
            if(systemMessage!=null){
                STFUConfig.systemMessages.add(0,systemMessage);
                //systemMessages.add(systemMessage);
            }
        }

       // STFUConfig.systemMessages=systemMessages;
        return true;
    }
    public static Errand handlePost_Errand_Message(String responseString){
        Errand errand;
        if(responseString!=null){
            Gson gson=new Gson();
            try{
                JSONObject jsonObject=new JSONObject(responseString);
                JSONObject data1 = new JSONObject(jsonObject.getString("data"));
                JSONObject data=new JSONObject(data1.getString("errand_message"));
                String errand_data=data.getString("post_0");
                errand=gson.fromJson(errand_data,Errand.class);
                return errand;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
    public static List<Comment> handlePost_Errand_Comment_Message(String responseString){
        List<Comment> comments;
        if(!responseString.isEmpty()){
            Gson gson=new Gson();
            try{
                comments=new ArrayList<Comment>();
                JSONObject jsonObject=new JSONObject(responseString);
                JSONObject data = new JSONObject(jsonObject.getString("data"));
                JSONObject comments_data=new JSONObject(data.getString("comments_message"));
                int i=0;
                while(i<comments_data.length()){
                    String str=(String)comments_data.getString("comment_"+i);
                    Comment comment=gson.fromJson(str,Comment.class);
                    comments.add(comment);
                    i++;
                }
                return comments;
            }catch (Exception e){
                Logger.i(e.toString());;
            }
        }
        return null;
    }
    private static SystemMessage system_message_handle(String jsonString){//处理系统通知的json字符串，转化成systemMessage
        Gson gson=new Gson();
        return gson.fromJson(jsonString,SystemMessage.class);
    }

}
