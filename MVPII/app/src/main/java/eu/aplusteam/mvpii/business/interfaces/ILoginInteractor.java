package eu.aplusteam.mvpii.business.interfaces;

/**
 * Use case for login <br/>
 * User: anton<br/>
 * Date: 07.02.17<br/>
 * Time: 14:18<br/>
 */
public interface ILoginInteractor {

    interface OnLoginFinishedListener {
        void onUsernameError();

        void onPasswordError();

        void onSuccess();
    }

    void login(final String username, final String password, final OnLoginFinishedListener listener);

}
