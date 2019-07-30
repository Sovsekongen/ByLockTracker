package p.vikpo.bylocktracker.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import p.vikpo.bylocktracker.R;

public class LoginActivity extends FragmentActivity
{
    private static final int REQUEST_SIGNUP = 0;
    private Button loginButton;
    private EditText emailText, passWordText;
    private TextView noAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final Intent launchSignup = new Intent(this, SignupActivity.class);

        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.btn_login);
        emailText = findViewById(R.id.input_email);
        passWordText = findViewById(R.id.input_password);
        noAccount = findViewById(R.id.link_signup);

        loginButton.setOnClickListener(v -> login());
        noAccount.setOnClickListener(v -> startActivity(launchSignup));
    }

    public void login()
    {
        Log.d("Login", "Login");

        if (!validate())
        {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailText.getText().toString();
        String password = passWordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new Handler().postDelayed(() ->
                {
                    // On complete call either onLoginSuccess or onLoginFailed
                    onLoginSuccess();
                    // onLoginFailed();
                    progressDialog.dismiss();
                }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP)
        {
            if (resultCode == RESULT_OK)
            {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically


                this.finish();
            }
        }
    }

    public void onLoginSuccess()
    {
        loginButton.setEnabled(true);

        Intent mainIntent = new Intent(this, MainActivity.class);

        startActivity(mainIntent);
    }

    public void onLoginFailed()
    {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate()
    {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passWordText.getText().toString();

        if(email.contains("clear"))
        {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

            sharedPref.edit().clear().commit();
            return valid;
        }
        if(email.isEmpty() && password.isEmpty())
        {
            return valid;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailText.setError("enter a valid email address");
            valid = false;
        }
        else
        {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10)
        {
            passWordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        }
        else
        {
            passWordText.setError(null);
        }

        return valid;
    }
}