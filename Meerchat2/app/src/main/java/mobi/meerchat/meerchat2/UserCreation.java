package mobi.meerchat.meerchat2;

import android.app.Activity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;


public class UserCreation extends Activity {
    EditText unBox;
    EditText emBox;
    String data;
    final Activity activity = this;
    Bundle extras;
    String fbid = "";
    String fbemail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            extras = getIntent().getExtras();
            if(extras==null) {
                fbid=null;
                fbemail=null;
            } else {
                fbid=extras.getString("FBid");
                fbemail = extras.getString("FBemail");
            }
        } else {
            fbid = (String)savedInstanceState.getSerializable("FBid");
            fbemail = (String)savedInstanceState.getSerializable("FBemail");
        }
        setContentView(R.layout.activity_user_creation);
        unBox = (EditText)findViewById(R.id.usernameBox);
        emBox = (EditText)findViewById(R.id.emailbox);
        String bread = "";

    }

    public void check(View view) {
        Log.v("UserCreation", unBox.getText().toString());
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    data = getData(MainActivity.phpUrl+"uniqueUN.php?BUN="+unBox.getText().toString());
                } catch(Exception e) {
                    e.printStackTrace();
                }
                if(data.equals("F")){
                    Log.v("UserCreation","bad username");

                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "bad usernae", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //Toast.makeText(getApplicationContext(),"try again",Toast.LENGTH_SHORT);
                } else {
                    if(emBox.getText().toString().contains("@ufl.edu")){
                        data = getData(MainActivity.phpUrl+"verifyEmail.php?BAFemail="+emBox.getText().toString());
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, "check email", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent RegisterIntent = new Intent(getApplicationContext(), VerificationCode.class);
                        RegisterIntent.putExtra("FBemail",fbemail);
                        RegisterIntent.putExtra("FBid",fbid);
                        RegisterIntent.putExtra("BAFemail",emBox.getText().toString());
                        RegisterIntent.putExtra("BUN",unBox.getText().toString());
                        startActivity(RegisterIntent);
                    } else {
                        Log.v("UserCreation","bad email");
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, "bad email", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        t.start();
    }

    public static String getData (String url) {


        String encodedurl = "";
        HttpResponse response;
        /**
         try {
         //HttpPost post = new HttpPost(url);
         //encodedurl = URLEncoder.encode(url,"UTF-8");

         encodedurl = URLEncoder.encode(url.substring(url.indexOf("?")+1),"UTF-8");
         String phpFunction = url.substring(url.indexOf("/v1/")+4,url.indexOf("?")+1);
         encodedurl = "http://bafit.mobi/cScripts/v1/"+phpFunction+encodedurl;

         Log.v("GET REQUEST ENCODEDURL",encodedurl);

         } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
         Log.v("GET REQUEST ENCODEDURL","couldn't encode");
         }
         */


        try {
            //HttpClient httpclient = new DefaultHttpClient();
            AndroidHttpClient httpclient = AndroidHttpClient.newInstance("Android");
            response = httpclient.execute(new HttpGet(url));
            httpclient.close();
            Log.v("WEBREQUESTS","EXECUTED");
            //HttpResponse response = httpclient.execute(new HttpGet(url));
            //String result = EntityUtils.toString(response);
            //content = response.getEntity().getContent();
        } catch (Exception e) {
            Log.v("WEBREQUESTS", "httpget broke", e);
            return "";
        }


        try {
            Log.v("WEBREQUESTS",response + "");
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            return responseString;
        } catch (Exception e) {
            Log.v("WEBREQUESTS", "Network exception", e);
            return "";
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_creation, menu);
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
}
