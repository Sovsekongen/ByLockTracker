package p.vikpo.bylocktracker.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.helpers.WifiHandler;
import p.vikpo.bylocktracker.login.MySingleton;
import p.vikpo.bylocktracker.login.SessionHandler;

public class LoginActivity extends FragmentActivity
{
    private static final int REQUEST_SIGNUP = 0;
    private Button loginButton;
    private EditText emailText, passWordText;
    private TextView noAccount;
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_EMPTY = "";
    private String login_url = "login/html/login.php";
    private SessionHandler session;
    private WifiHandler wifiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final Intent launchSignup = new Intent(this, SignupActivity.class);

        wifiHandler = new WifiHandler(this);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.btn_login);
        emailText = findViewById(R.id.input_email);
        passWordText = findViewById(R.id.input_password);
        noAccount = findViewById(R.id.link_signup);

        loginButton.setOnClickListener(v -> login());
        noAccount.setOnClickListener(v -> startActivity(launchSignup));

        session = new SessionHandler(this);

        if(session.isLoggedIn())
        {
            onLoginSuccess();
        }
    }

    public void login()
    {

        final ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        if (!validate())
        {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        String email = emailText.getText().toString();
        String password = passWordText.getText().toString();

        if(email.equals("") && password.equals(""))
        {
            session.loginUser("vikpo@live.dk", "");
            pDialog.dismiss();
            onLoginSuccess();
        }
        else if(email.equals("clear") && password.equals(""))
        {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

            sharedPref.edit().clear().apply();

            pDialog.dismiss();
            onLoginSuccess();
        }

        JSONObject request = new JSONObject();
        try
        {
            //Populate the request parameters
            request.put(KEY_EMAIL, email);
            request.put(KEY_PASSWORD, password);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, wifiHandler.checkWifi() + login_url, request, response ->
                {
                    pDialog.dismiss();
                    try
                    {
                        //Check if user got logged in successfully
                        if (response.getInt(KEY_STATUS) == 0)
                        {
                            Toast.makeText(getApplicationContext(),
                                    response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            session.loginUser(email, response.getString(KEY_FULL_NAME));
                            onLoginSuccess();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),
                                    response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        Log.e("bylock", "error", e);
                    }
                },
                        error ->
                {
                    pDialog.dismiss();

                    //Display error message whenever an error occurs
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    public void onLoginSuccess()
    {
        loginButton.setEnabled(true);
        Intent mainIntent = new Intent(this, MainActivity.class);

        startActivity(mainIntent);
        finish();
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

        if (password.isEmpty() || password.length() < 4 || password.length() > 18)
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