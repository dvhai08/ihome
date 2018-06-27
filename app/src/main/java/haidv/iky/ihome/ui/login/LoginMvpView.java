package haidv.iky.ihome.ui.login;

import com.google.firebase.auth.FirebaseUser;

public interface LoginMvpView {

    void showToast(String message);

    void loginSuccess();

    void updateUI(FirebaseUser user);
}
