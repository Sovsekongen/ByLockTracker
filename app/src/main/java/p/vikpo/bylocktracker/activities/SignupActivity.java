package p.vikpo.bylocktracker.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintStream;

import p.vikpo.bylocktracker.R;
import p.vikpo.bylocktracker.login.MySingleton;
import p.vikpo.bylocktracker.login.SessionHandler;
import p.vikpo.bylocktracker.login.User;

public class SignupActivity extends FragmentActivity
{
    private static final String TAG = "SignupActivity";
    private ProgressDialog pDialog;
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_EMPTY = "";
    private String email, password, name;
    private EditText emailTextView, passwordTextView, nameTextView;
    private Button signUp;
    private TextView account;
    private String register_url = "http://192.168.1.50:80/register.php";
    private SessionHandler session;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        final Intent launchLogin = new Intent(this, LoginActivity.class);

        account = findViewById(R.id.link_login);
        emailTextView = findViewById(R.id.su_email);
        passwordTextView = findViewById(R.id.su_pword);
        nameTextView = findViewById(R.id.su_name);
        signUp = findViewById(R.id.btn_signup);

        account.setOnClickListener(v -> startActivity(launchLogin));
        signUp.setOnClickListener(v1 ->
        {
            loadInputs();
        });
    }

    public void loadInputs()
    {
        email = emailTextView.getText().toString().toLowerCase().trim();
        password = passwordTextView.getText().toString().trim();
        name = nameTextView.getText().toString().trim();

        if(validate())
        {
            signup();
        }
    }

    public void signup()
    {
        displayLoader();
        JSONObject request = new JSONObject();
        try
        {
            //Populate the request parameters
            request.put(KEY_EMAIL, email);
            request.put(KEY_PASSWORD, password);
            request.put(KEY_FULL_NAME, name);

            Log.e("bylock", "Request:   " + request.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.e("bylock", "error", e);
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, register_url, request, response ->
        {
            pDialog.dismiss();

            try
            {
                Log.e("bylock", "Response: " + response.toString());

                if(response.toString().equals("{}"))
                {
                    return;
                }
                                //Check if user got registered successfully
                if (response.getInt(KEY_STATUS) == 0)
                {
                    //Set the user session
                    session.loginUser(email, name);
                    onSignupSuccess();

                }
                else if(response.getInt(KEY_STATUS)  == 1)
                {
                    //Display error message if username is already existsing
                    emailTextView.setError("Account Already Exists!");
                    emailTextView.requestFocus();
                }
                else
                {
                    emailTextView.setError("Bro you fucked up...");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Log.e("bylock", "error here", e);
            }
        },
                error ->
                {
                    pDialog.dismiss();

                    //Display error message whenever an error occurs
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("bylock", "error here here", error);
                });

        /*
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, register_url, request, response ->
        {
            pDialog.dismiss();

            Log.e("bylock", response.toString());

            if(response.toString().equals("{}"))
            {
                return;
            }

            try
            {

                //Check if user got registered successfully
                if (response.getInt(KEY_STATUS) == 0)
                {
                    //Set the user session
                    session.loginUser(email, name);
                    onSignupSuccess();

                }
                else if(response.getInt(KEY_STATUS)  == 1)
                {
                    //Display error message if username is already existsing
                    emailTextView.setError("Account Already Exists!");
                    emailTextView.requestFocus();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Log.e("bylock", "error here", e);
            }
        },
        error ->
        {
            pDialog.dismiss();

            //Display error message whenever an error occurs
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("bylock", "error here here", error);
        });*/

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    public void onSignupSuccess()
    {
        Intent loginActivity = new Intent(this, LoginActivity.class);

        startActivity(loginActivity);
    }

    public void onSignupFailed()
    {

    }

    public boolean validate()
    {
        if (KEY_EMPTY.equals(name))
        {
            nameTextView.setError("Full Name cannot be empty");
            nameTextView.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(email))
        {
            emailTextView.setError("Username cannot be empty");
            emailTextView.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password))
        {
            passwordTextView.setError("Password cannot be empty");
            passwordTextView.requestFocus();
            return false;
        }
        return true;
    }

    private void displayLoader()
    {
        pDialog = new ProgressDialog(SignupActivity.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
