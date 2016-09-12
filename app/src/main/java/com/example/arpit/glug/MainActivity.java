package com.example.arpit.glug;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private EditText userName;
    private EditText email;
    private EditText phoneNum;
    private Button submit;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userName = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phoneNum = (EditText) findViewById(R.id.phone);
        submit = (Button) findViewById(R.id.btnSubmit);
        next = (Button) findViewById(R.id.btnNext);
        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                final String name = userName.getText().toString().trim();
                final String emailValidate = email.getText().toString().trim();
                final String phone = phoneNum.getText().toString().trim();
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                boolean flag = true;
                if (emailValidate == null) {
                    flag = false;
                } else {
                    flag = emailValidate.matches(emailPattern);
                }

                if(flag == false)
                    Toast.makeText(getApplicationContext(),
                            "Enter email correctly", Toast.LENGTH_LONG)
                            .show();
                else {
                    try {
                        JSONObject toSend = new JSONObject();
                        toSend.put("name", name);
                        toSend.put("email",emailValidate);
                        toSend.put("phone",phone);
                        SendMessage s1=new SendMessage();
                        s1.execute(toSend);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                 {
                    Intent i = new Intent(getApplicationContext(),
                            Next.class);
                    startActivity(i);
                    finish();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class SendMessage extends AsyncTask<JSONObject,JSONObject,JSONObject> {

        private Dialog loadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        String url = "http://192.168.100.27/intern/insert.php";
        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            JSONObject json = params[0];
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);

            JSONObject jsonResponse = null;
            HttpPost post = new HttpPost(url);
            try {
                StringEntity se = new StringEntity("json="+json.toString());
                post.addHeader("content-type", "application/x-www-form-urlencoded");
                post.setEntity(se);

                HttpResponse response;
                response = client.execute(post);
                String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());

                jsonResponse=new JSONObject(resFromServer);
            } catch (Exception e) { e.printStackTrace();}

            return jsonResponse;
        }




        @Override
        protected void onPostExecute(JSONObject result) {
            if(result==null)
            {
                Toast.makeText(getApplicationContext(),"DOne",Toast.LENGTH_LONG).show();

            }
            else{

                         String status=null;

                   try{
                       status=result.getString("status");

                   }
                   catch (JSONException s){

                   }
                Toast.makeText(getApplicationContext(),status, Toast.LENGTH_LONG).show();
                }





        }
    }
}
