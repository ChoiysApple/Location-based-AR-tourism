package com.marchengraffiti.nearism.nearism.sign;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.marchengraffiti.nearism.nearism.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mUsernameField;
    private Button sign_up;
    private Button sign_in;
    private TextView account_guide;
    private static final String TAG = "EmailPassword";
    static final String PREF_USER_ACCOUNT = "account";
    SharedPreferences auto;
    SharedPreferences.Editor toEdit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // Fields
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);
        mUsernameField = findViewById(R.id.fieldName);
        account_guide = findViewById(R.id.account_guide);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Button
        sign_up = (Button) findViewById(R.id.btn_signUp);
        sign_in = (Button) findViewById(R.id.btn_gotoLogin);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(mUsernameField.getText().toString())
                            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    }
                                }
                            });
                }
                catch(Exception e){
                    Log.d("Exception", e.toString());
                }
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });



    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    private void createAccount(String email, final String password){

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("sign up", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent=new Intent(SignUp.this,SignIn.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            if(password.length() < 6)
                                account_guide.setTextColor(Color.RED);
                            else{
                                account_guide.setTextColor(Color.RED);
                                account_guide.setText("@string/account_guide");
                            }


                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void autoSignin() {
        auto = getSharedPreferences(PREF_USER_ACCOUNT, Activity.MODE_PRIVATE);
        toEdit = auto.edit();
        auto = getSharedPreferences(PREF_USER_ACCOUNT, MODE_PRIVATE);
        if (auto != null && auto.contains("saved_email")&& auto.contains("saved_password")) {
            Toast.makeText(getApplicationContext(), "자동 로그인 중입니다", Toast.LENGTH_LONG).show();
            //String email = auto.getString("saved_email", "noname");
            //String password = auto.getString("saved_password", "noname");
            Intent intent=new Intent(SignUp.this,SignIn.class);
            startActivity(intent);
        }

    }


    public void sendEmailVerificationWithContinueUrl() {
        // [START send_email_verification_with_continue_url]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        String url = "http://www.example.com/verify?uid=" + user.getUid();
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl(url)
                .setIOSBundleId("com.example.ios")
                // The default for this is populated with the current android package name.
                .setAndroidPackageName("com.example.android", false, null)
                .build();

        user.sendEmailVerification(actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }




}
