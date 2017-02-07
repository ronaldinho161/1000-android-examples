package eu.aplusteam.mvpii.eu.aplusteam.mvpii.business;

import android.os.Handler;
import android.text.TextUtils;

import eu.aplusteam.mvpii.business.interfaces.ILoginInteractor;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: anton<br/>
 * Date: 07.02.17<br/>
 * Time: 14:22<br/>
 */
public class LoginInteractor implements ILoginInteractor {
    @Override
    public void login(final String username, final String password, final OnLoginFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean error = false;
                if (TextUtils.isEmpty(username)){
                    listener.onUsernameError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    listener.onPasswordError();
                    error = true;
                    return;
                }
                if (!error){
                    listener.onSuccess();
                }
            }
        }, 2000);
    }
}
