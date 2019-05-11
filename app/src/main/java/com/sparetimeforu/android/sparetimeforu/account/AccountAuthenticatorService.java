package com.sparetimeforu.android.sparetimeforu.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * AccountManagerTest
 * Created by Jin on 2019/5/8.
 * Email:17wjli6@stu.edu.cn
 */
public class AccountAuthenticatorService extends Service {

    private AccountAuthenticator accountAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        accountAuthenticator = new AccountAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return accountAuthenticator.getIBinder();
    }
}
