package eu.aplusteam.mvp;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: anton<br/>
 * Date: 07.02.17<br/>
 * Time: 08:57<br/>
 */
public interface LoginInteractor {

    interface OnLoginFinishedListener {

        void onUsernameError();

        void onPasswordError();

        void onSuccess();
    }

    void login(String username, String password, OnLoginFinishedListener listener);

}
