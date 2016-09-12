package com.example.arpit.glug;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arpit on 12/9/16.
 */
public class Next extends AppCompatActivity {
    private EditText number;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next);


        number = (EditText) findViewById(R.id.number);
        submit = (Button) findViewById(R.id.btnSubmit);
        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                final int idx=Integer.parseInt(number.getText().toString());
                try {
                    JSONObject toSend = new JSONObject();
                    toSend.put("idx", idx);
                    SendMessage s1=new SendMessage();
                    s1.execute(toSend);




                } catch (JSONException e) {
                    e.printStackTrace();
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

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            JSONObject json = params[0];
            int id=-1;
            try {
                 id = json.getInt("idx");
            }
            catch (JSONException c)
            {

            }
            String url = "http://192.168.100.27/intern/getDetails.php?idt="+id;
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);

            JSONObject jsonResponse = null;
            HttpGet post = new HttpGet(url);
            try {
               ;

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



                }
                Toast.makeText(getApplicationContext(),result.toString(), Toast.LENGTH_LONG).show();
            }





        }

}
