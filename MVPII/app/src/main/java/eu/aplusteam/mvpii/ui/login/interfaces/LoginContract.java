package eu.aplusteam.mvpii.ui.login.interfaces;

import eu.aplusteam.mvpii.business.interfaces.ILoginInteractor;
import eu.aplusteam.mvpii.ui.interfaces.BasePresenter;
import eu.aplusteam.mvpii.ui.interfaces.BaseView;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: anton<br/>
 * Date: 07.02.17<br/>
 * Time: 13:59<br/>
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void showProgress();

        void hideProgress();

        void setUsernameError();

        void setPasswordError();

        void navigateToHome();
    }

    interface Presenter extends BasePresenter {

        void login(String username, String password);

        void onDestroy();
    }
}
