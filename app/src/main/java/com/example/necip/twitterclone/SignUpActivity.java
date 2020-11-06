package com.example.necip.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSignUpEmail, edtSignUpUsername, edtSignUpPassword;
    private Button btnSignUpSA, btnLoginSA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Sign Up");

        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpUsername = findViewById(R.id.edtSignUpUsername);
        edtSignUpPassword = findViewById(R.id.edtLoginPassword);

        btnSignUpSA = findViewById(R.id.btnSignUpSA);
        btnSignUpSA.setOnClickListener(this);
        btnLoginSA = findViewById(R.id.btnLoginSA);
        btnLoginSA.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            //ParseUser.getCurrentUser().logOut();
            transitionalTwitterUsers();

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUpSA:

                if (edtSignUpEmail.getText().toString().equals("") || edtSignUpUsername.getText().toString().equals("") || edtSignUpPassword.getText().toString().equals(""))
                        FancyToast.makeText(SignUpActivity.this, "Email, username, password is necessary!", Toast.LENGTH_LONG, FancyToast.INFO, true).show();
                else {
                    final ParseUser parseUser = new ParseUser();
                    parseUser.setEmail(edtSignUpEmail.getText().toString());
                    parseUser.setUsername(edtSignUpUsername.getText().toString());
                    parseUser.setPassword(edtSignUpPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                    progressDialog.setMessage("Signin up " + edtSignUpUsername.getText().toString());
                    progressDialog.show();

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                FancyToast.makeText(SignUpActivity.this, parseUser.getUsername() + " is signed up successfully", Toast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                transitionalTwitterUsers();

                            } else
                                FancyToast.makeText(SignUpActivity.this, "Olmadı galiba ;) \n\n" + e.getMessage(), Toast.LENGTH_LONG, FancyToast.ERROR, true).show();

                            progressDialog.dismiss();
                        }
                    });

                }

                break;

            case R.id.btnLoginSA:
                Intent intentlog = new Intent(this, LoginActivity.class);
                startActivity(intentlog);
                break;
        }

    }

    /**
     * for hide the keyboard
     * @param view
     */
    public void constraintLayoutTappedS(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionalTwitterUsers() {
        Intent intent = new Intent(SignUpActivity.this, TwitterUsers.class);
        startActivity(intent);
        finish();
    }
}