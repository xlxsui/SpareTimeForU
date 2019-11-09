package com.sparetimeforu.android.sparetimeforu.util;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.sparetimeforu.android.sparetimeforu.STFUConfig;
import com.sparetimeforu.android.sparetimeforu.ServerConnection.OkHttpUtil;
import com.sparetimeforu.android.sparetimeforu.entity.Errand;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


/**
 * Created by HQY on 2019/6/28.
 */

//数据库操作类
public class ErrandDataBaseUtil {

    //将任务存到任务数据库中
    public static boolean save_errand_data(List<Errand> errands) {
        for (int i = 0; i < errands.size(); i++) {
            Errand errand = errands.get(i);
            List<Errand> found_errand = LitePal.where("errand_id=" + errand.getErrand_id()).find(Errand.class);//找到相应的任务
            if (found_errand.size() < 1) {//没有找到该任务，说明没有存过
                errand.save();
            } else {
                if (errand.getComment_number() == 0) {
                    errand.setToDefault("comment_number");
                }
                if (errand.getLike_number() == 0) {
                    errand.setToDefault("like_number");
                }
                if (errand.getIs_received() == 0) {
                    errand.setToDefault("is_received");
                }
                if (errand.getIs_finished() == 0) {
                    errand.setToDefault("is_finished");
                }
                if (errand.getIs_confirmed_finish() == 0) {
                    errand.setToDefault("is_confirmed_finish");
                }
                errand.update(found_errand.get(0).getId());//更新数据
            }
        }

        // 同步服务器删除的数据
        FormBody body = new FormBody.Builder()
                .build();

        OkHttpUtil.sendOkHttpPostRequest(STFUConfig.HOST + "/mission/get_deleted_missions", body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            int errandID = array.getInt(i);
                            List<Errand> found_errand = LitePal
                                    .where("errand_id=" + errandID)
                                    .find(Errand.class);//找到相应的任务
                            if (found_errand.size() > 0) {
                                Errand errand = found_errand.get(0);
                                errand.delete();
                            }
                        }
                    }
                } catch (JSONException e) {
                    Logger.e(e.toString());
                }
            }
        });
        return true;
    }

    //取适当个数的任务返回
    public static List getErrand_data(int origin, String destination) {//找到数据库中第origin条任务，以此为起点返回6个任务数据
        if (origin < 1)
            origin = 1;
        List<Errand> errands;
        if (destination.equals("随机")) {
            errands = LitePal.where("is_received = 0")
                    .order("errand_id desc")
                    .limit(6)
                    .offset(origin - 1)
                    .find(Errand.class);
        } else {
            errands = LitePal.where("destination = ?  and is_received = 0", destination)
                    .order("errand_id desc")
                    .limit(6)
                    .offset(origin - 1)
                    .find(Errand.class);
        }
        return errands;
    }

    //更新数据
    public static boolean update_Errand_data(List<Errand> errands) {
        for (int i = 0; i < errands.size(); i++) {
            Errand errand = errands.get(i);
            List<Errand> found_errand = LitePal.where("errand_id=" + errand.getErrand_id()).find(Errand.class);//找到相应的任务
            if (found_errand.size() != 0) {
                //errand.update(found_errand.get(0).getId());
            }
            //errand.update();
        }
        return true;
    }

    public static int Get_biggest_errand_id() {//获取最大的任务id
        List<Errand> errands = LitePal.order("errand_id desc").find(Errand.class);
        if (errands.size() == 0) {//数据库中没有任务，返回-1
            return 0;
        } else {
            return errands.get(0).getErrand_id();
        }
    }

    public static boolean delete_All_Data() {//删除所有数据
        LitePal.deleteAll(Errand.class);
        return true;
    }

    /*
     * 删除所有的已被接受的任务
     * */
    public static boolean delete_Received_errand() {
        LitePal.deleteAll(Errand.class, "is_received=1");//删除所有已经被接受的任务
        return true;
    }

    public static boolean deleteErrandNow(int id) {
        LitePal.deleteAll(Errand.class, "errand_id=" + id);
        return true;
    }
}
