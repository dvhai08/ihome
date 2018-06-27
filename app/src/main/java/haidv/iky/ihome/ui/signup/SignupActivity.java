package haidv.iky.ihome.ui.signup;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import haidv.iky.ihome.R;
import haidv.iky.ihome.ui.base.BaseActivity;
import haidv.iky.ihome.ui.login.LoginActivity;
import haidv.iky.ihome.util.ViewUtil;

public class SignupActivity extends BaseActivity {

    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;

    EditText edtName;
    EditText edtEmail;
    EditText edtPassword;
    EditText edtPasswordConfirm;

    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtName = findViewById(R.id.editTextUserNameActivitySignup);
        edtEmail = findViewById(R.id.editTextEmailActivitySignup);
        edtPassword = findViewById(R.id.editTextPasswordActivitySignup);
        edtPasswordConfirm = findViewById(R.id.editTextPasswordConfirmActivitySignup);

        btnSignup = findViewById(R.id.buttonSignupActivitySignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!edtPassword.getText().toString().equals(
                        edtPasswordConfirm.getText().toString() )){
                    toast("Nhập mật khẩu không trùng nhau");
                    return;
                }

                if(!edtPassword.getText().toString().equals(
                        edtPasswordConfirm.getText().toString() )){
                    toast("Nhập mật khẩu không trùng nhau");
                    return;
                }
            }
        });
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        showProgressDialog();
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(SignupActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                        hideProgressDialog();
                    }
                });
    }

    private void createAccount(final String name, String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!ViewUtil.validateText(name)) {
            toast("Tên không được bỏ trống");
            return;
        }

        if (!ViewUtil.validateText(email)) {
            toast("Email không được bỏ trống");
            return;
        }

        if (!ViewUtil.validateText(password)) {
            toast("Mật khẩu không được bỏ trống");
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignupActivity.this, "Tạo tài khoản thành công",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            user.updateProfile(profileUpdates);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser user) {

        if (user != null) {
            if(user.isEmailVerified()) {
//                loginSuccess();
            }
            else
            {
                sendEmailVerification();
            }
        } else {
        }
    }

    private void toast(final String ss){
        Toast.makeText(SignupActivity.this, ss,
                Toast.LENGTH_SHORT).show();
    }
}
