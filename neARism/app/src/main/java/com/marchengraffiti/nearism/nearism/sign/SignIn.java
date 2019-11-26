package com.marchengraffiti.nearism.nearism.sign;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.marchengraffiti.nearism.nearism.MainActivity;
import com.marchengraffiti.nearism.nearism.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button sign_in;
    SharedPreferences auto;
    SharedPreferences.Editor toEdit;
    static final String PREF_USER_ACCOUNT = "account";
    String saved_email, saved_password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        // Fields
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        sign_in = (Button) findViewById(R.id.btn_login);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saved_email = mEmailField.getText().toString();
                    saved_password = mPasswordField.getText().toString();
                    signin(mEmailField.getText().toString(), mPasswordField.getText().toString());
                    Toast.makeText(SignIn.this, "Signing in...", Toast.LENGTH_SHORT).show();
                }catch (IllegalArgumentException e){
                    Toast.makeText(SignIn.this, "Email, password requires", Toast.LENGTH_SHORT).show();
                }
            }
        });

        autoSignin();
    }

    private void signin(String email, String password) {
        if (mEmailField.getText().toString() == null || mPasswordField.getText().toString() == null)
            return;
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            saveAccountInfo(saved_email, saved_password);
                            Toast.makeText(SignIn.this, "환영합니다", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                            startActivity(intent);

                            finish();
                        } else {
                            // 로그인 실패
                            Toast.makeText(SignIn.this, "올바르지 않은 패스워드 또는 이메일입니다!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveAccountInfo(String email, String password) {
        auto = getSharedPreferences(PREF_USER_ACCOUNT, Activity.MODE_PRIVATE);
        toEdit = auto.edit();
        toEdit.putString("saved_email", email);
        toEdit.putString("saved_password", password);
        toEdit.commit();
    }

    private void autoSignin() {
        auto = getSharedPreferences(PREF_USER_ACCOUNT, MODE_PRIVATE);
        if (auto != null && auto.contains("saved_email")&& auto.contains("saved_password")) {
            Toast.makeText(getApplicationContext(), "자동 로그인 중입니다", Toast.LENGTH_LONG).show();
            String email = auto.getString("saved_email", "noname");
            String password = auto.getString("saved_password", "noname");
            this.signin(email, password);
        }

    }


    /*
    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    // [END auth_fui_result]

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_signout]
    }

    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_delete]
    }*/

}
