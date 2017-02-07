package eu.aplusteam.mvp;

import android.os.Handler;
import android.text.TextUtils;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: anton<br/>
 * Date: 07.02.17<br/>
 * Time: 09:03<br/>
 */
public class LoginInteractorImpl implements LoginInteractor {
    @Override
    public void login(final String username, final String password, final OnLoginFinishedListener listener) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean error = false;

                if (TextUtils.isEmpty(username)) {
                    listener.onUsernameError();
                    error = true;
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    listener.onPasswordError();
                    error = true;
                }

                if (!error) {
                    listener.onSuccess();
                }
            }
        }, 2000);

    }
}
