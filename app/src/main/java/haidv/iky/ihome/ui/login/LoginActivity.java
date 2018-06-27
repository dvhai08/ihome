package haidv.iky.ihome.ui.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.security.PrivateKey;

import haidv.iky.ihome.R;
import haidv.iky.ihome.ui.addboard.AddBoardActivity;
import haidv.iky.ihome.ui.base.BaseActivity;
import haidv.iky.ihome.ui.main.MainActivity;
import haidv.iky.ihome.ui.signup.SignupActivity;
import haidv.iky.ihome.util.ViewUtil;

public class LoginActivity extends BaseActivity implements LoginMvpView{

    private static final String TAG = "haidv";

    private Button btnLogin;
    private TextView tvSignUp;
    private EditText edtEmail;
    private EditText edtPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btnLogin = this.findViewById(R.id.buttonLogin);
        edtEmail = this.findViewById(R.id.editTextEmail);
        edtPassword = this.findViewById(R.id.editTextPassword);
        tvSignUp = this.findViewById(R.id.tvSignupActivityLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edtEmail.getText().toString(),edtPassword.getText().toString());
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                //createAccount(edtEmail.getText().toString(),edtPassword.getText().toString());
            }
        });
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void updateUI(FirebaseUser user) {

        if (user != null) {

            if(user.isEmailVerified()) {
                loginSuccess();
            }
            else
            {
                showToast("Tài khoản chưa được xác minh.");
            }
        } else {
        }
    }

    @Override
    public void loginSuccess() {

        Intent intentHome = new Intent(this, MainActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentHome);

    }

    private void signIn(String email, String password) {

        if(!ViewUtil.validateText(email)){
            showToast(getString(R.string.login_error_input_user));
            return;
        }

        if(!ViewUtil.validateText(password)){
            showToast(getString(R.string.login_error_input_pass));
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            showToast(getString(R.string.authentication_failed));
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
    }
}
