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

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginEmail, edtLoginPassword;
    private Button btnLoginLA, btnSignUpLA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log In");

        edtLoginEmail = findViewById(R.id.edtSignUpEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);

        btnLoginLA = findViewById(R.id.btnLoginLA);
        btnLoginLA.setOnClickListener(this);
        btnSignUpLA = findViewById(R.id.btnSignUpLA);
        btnSignUpLA.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser().logOut();
            transitionalTwitterUsers();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnLoginLA:

                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading in");
                progressDialog.show();

                ParseUser.logInInBackground(edtLoginEmail.getText().toString(), edtLoginPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null) {
                            FancyToast.makeText(LoginActivity.this, ParseUser.getCurrentUser().getUsername() + " is logged in successfully", Toast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            transitionalTwitterUsers();
                        } else
                            FancyToast.makeText(LoginActivity.this, "Olmadı galiba ;) \n\n" + e.getMessage(), Toast.LENGTH_LONG, FancyToast.ERROR, true).show();

                        progressDialog.dismiss();
                    }
                });

                break;

            case R.id.btnSignUpLA:
                Intent intentlog = new Intent(this, SignUpActivity.class);
                startActivity(intentlog);
                break;
        }
    }

    /**
     * for hide the keyboard
     * @param view
     */
    public void constraintLayoutTappedL(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionalTwitterUsers() {

        Intent intent = new Intent(LoginActivity.this, TwitterUsers.class);
        startActivity(intent);
        finish();
    }
}