package com.sparetimeforu.android.sparetimeforu.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sparetimeforu.android.sparetimeforu.activity.LoginActivity;

/**
 * AccountManagerTest
 * Created by Jin on 2019/5/8.
 * Email:17wjli6@stu.edu.cn
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {
    private Context context;

    public AccountAuthenticator(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    /**
     * 添加账号
     *
     * @param response
     * @param accountType
     * @param authTokenType
     * @param requiredFeatures
     * @param options
     * @return Bundle 里面包含一个登录的intent，用此intent可以打开登录activity，然后activity里面会调用addAccountExplicitly()增加新的账号。
     * @throws NetworkErrorException
     */
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType,
                             String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Intent intent = new Intent(context, LoginActivity.class);
        if (options != null) {
            intent.putExtras(options);
        }
        //一定要把response传入intent的extra中，便于将登录操作的结果回调给AccountManager
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
                response);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    /**
     * 自己实现：验证用户的密码
     */
    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response,
                                     Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    /**
     * 自己完成获取token的流程，联网获取数据，然后重新设置account，设置对应的数据setUserData()
     */
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response,
                               Account account, String authTokenType, Bundle options)
            throws NetworkErrorException {
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }

}
