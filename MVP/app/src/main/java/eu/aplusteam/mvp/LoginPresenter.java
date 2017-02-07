package eu.aplusteam.mvp;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: anton<br/>
 * Date: 07.02.17<br/>
 * Time: 08:55<br/>
 */
public interface LoginPresenter {

    void validateCredentials(String username, String password);

    void onDestroy();

}
