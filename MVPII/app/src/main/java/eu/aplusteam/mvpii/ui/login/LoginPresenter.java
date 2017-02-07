package eu.aplusteam.mvpii.ui.login;

import eu.aplusteam.mvpii.business.interfaces.ILoginInteractor;
import eu.aplusteam.mvpii.business.LoginInteractor;
import eu.aplusteam.mvpii.ui.login.interfaces.LoginContract;

/**
 * Presenter for login functionality. <br/>
 * User: anton<br/>
 * Date: 07.02.17<br/>
 * Time: 14:02<br/>
 */
public class LoginPresenter implements LoginContract.Presenter, LoginInteractor.OnLoginFinishedListener {

    private LoginContract.View loginView;
    private ILoginInteractor loginInteractor;

    public LoginPresenter(LoginContract.View loginView, ILoginInteractor loginInteractor){
        this.loginView = loginView;
        this.loginInteractor = loginInteractor;
    }

    @Override
    public void start() {

    }

    @Override
    public void login(String username, String password) {
        if (loginView != null) {
            loginView.showProgress();
        }

        loginInteractor.login(username, password, this);
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onUsernameError() {
        if (loginView != null) {
            loginView.setUsernameError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onPasswordError() {
        if (loginView != null) {
            loginView.setPasswordError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onSuccess() {
        if (loginView != null) {
            loginView.navigateToHome();
        }
    }
}
