package eu.aplusteam.mvp;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: anton<br/>
 * Date: 07.02.17<br/>
 * Time: 08:53<br/>
 */
public interface LoginView {

    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void navigateHome();

}
