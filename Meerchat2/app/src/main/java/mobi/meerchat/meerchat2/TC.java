package mobi.meerchat.meerchat2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class TC extends Activity {
    String tc = "";
    String fbid = "";
    String fbemail = "";
    Bundle extras;

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
        Log.v("Terms&Conditions",fbid);
        Log.v("Terms&Conditions",fbemail);
        setContentView(R.layout.activity_tc);
        String contents = "";
        TextView tcView = (TextView) findViewById(R.id.tc);
        //startTimerThread(contents);
        outputThread ot = new outputThread();
        ot.start();
        Log.v("Terms&Conditions","trying to set");
        try {
            Thread.sleep(10000);
        } catch(Exception e) {
            e.printStackTrace();
        }
        Log.v("Terms&Conditions","while done");
        contents= ot.getContents();
        contents = contents.substring(contents.indexOf("Welcome to "));
        Log.v("Terms&Conditions",contents);
        while (contents.indexOf("<")>-1 || contents.indexOf(">")>-1) {
            if(contents.indexOf("<")==contents.indexOf("</p")) {
                contents = replaceString(contents,contents.substring(contents.indexOf("<"),contents.indexOf(">")+1),"\n\n");
            } else {
                contents = replaceString(contents,contents.substring(contents.indexOf("<"),contents.indexOf(">")+1),"");
            }
            Log.v("Terms&Conditions",contents);
        }
        //remove &ldquo ", &rdquo ", nbsp ,&quot ",&#39 ', &rsquo ',
        contents = replaceAllString(contents,"&ldquo;","\"");
        contents = replaceAllString(contents,"&rdquo;","\"");
        contents = replaceAllString(contents,"&nbsp;"," ");
        contents = replaceAllString(contents,"&quot;","\"");
        contents = replaceAllString(contents,"&#39;","\'");
        contents = replaceAllString(contents,"&rsquo;","\'");
        tcView.setText(contents);
        Log.v("Terms&Conditions","set");
        /**
        Thread t = new Thread(new Runnable(){
            public void run() {
                try
                {
                    //urlAddress=""; //CHANGE THIS LINE WEEK AND NUMBERS
                    //page = new URL(urlAddress);
                    //contents="";
                    //pageToRead=page;
                    final URL page = new URL(urlAddress);
                    String contents = "";
                    final TextView tcView = (TextView) findViewById(R.id.tc);
                    URLConnection yc = page.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    String inputLine;
                    while((inputLine=in.readLine())!=null)
                    {
                        contents+=inputLine;
                    }
                    in.close();
                    Log.v("Terms&Conditions",contents);
                    contents = contents.substring(contents.indexOf("Welcome to "));
                    Log.v("Terms&Conditions",contents);
                    while (contents.indexOf("<")>-1 || contents.indexOf(">")>-1) {
                        contents = replaceString(contents,contents.substring(contents.indexOf("<"),contents.indexOf(">")+1),"");
                        Log.v("Terms&Conditions",contents);
                    }
                    tcView.setText(contents);
                    Log.v("Terms&Conditions","done");
                }

                catch(Exception e)
                {
                    e.printStackTrace();
                    Log.v("Terms&Conditions","broken");
                }
            }
        });
        t.start();
         */


        /**
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    Log.v("TCLOG","trying url");
                    URL tcPage = new URL("http://www.oracle.com/"); //http://meerchat.mobi/PrivacyPolicy_Terms/
                    Log.v("TCLOG","trying buffered");
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(tcPage.openStream()));

                    String inputLine;
                    Log.v("TCLOG","running while");
                    while ((inputLine = in.readLine()) != null) {
                        tc += inputLine;
                        Log.v("TCLOG", tc);
                    }
                    in.close();
                    Log.v("TCLOG","done");
                } catch(Exception e) {
                    Log.v("TCLOG", "error");
                    e.printStackTrace();
                }
            }
        });
        t.start();
         */
        /**
        try {
            Log.v("TCLOG","trying url");
            URL tcPage = new URL("http://www.oracle.com/");
            Log.v("TCLOG","trying buffered");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(tcPage.openStream()));

            String inputLine;
            Log.v("TCLOG","running while");
            while ((inputLine = in.readLine()) != null)
                tc+=inputLine;
            in.close();
        } catch(Exception e) {
            Log.v("TCLOG", "error");
            e.printStackTrace();
        }
         */
        //Log.v("TCLOG","setting text");
        //tcView.setText(tc);
    }
/**
    private void startTimerThread(final String contents) {
        final String contents2;
        Runnable runnable = new Runnable() {
            //private long startTime = System.currentTimeMillis();
            public void run() {
                Handler handler = new Handler();
                final String urlAddress = "http://meerchat.mobi/PrivacyPolicy_Terms/";

                //String contents = "";
                final TextView tcView = (TextView) findViewById(R.id.tc);
                try
                {
                    final URL page = new URL(urlAddress);
                    //urlAddress=""; //CHANGE THIS LINE WEEK AND NUMBERS
                    //page = new URL(urlAddress);
                    //contents="";
                    //pageToRead=page;
                    URLConnection yc = page.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    String inputLine;
                    while((inputLine=in.readLine())!=null)
                    {
                        contents+=inputLine;
                    }
                    in.close();
                    Log.v("Terms&Conditions",contents);
                    contents = contents.substring(contents.indexOf("Welcome to "));
                    Log.v("Terms&Conditions",contents);
                    while (contents.indexOf("<")>-1 || contents.indexOf(">")>-1) {
                        contents = replaceString(contents,contents.substring(contents.indexOf("<"),contents.indexOf(">")+1),"");
                        Log.v("Terms&Conditions",contents);
                    }
                    tcView.setText(contents);
                    //final String contents2 = contents;
                    Log.v("Terms&Conditions","done");
                    contents2=contents;
                }

                catch(Exception e)
                {
                    e.printStackTrace();
                    Log.v("Terms&Conditions","broken");
                }
                    handler.post(new Runnable(){
                        public void run() {
                            tcView.setText(contents);
                        }
                    });

            }
        };
        new Thread(runnable).start();
    }
 */

    public static String replaceString(String bigString, String old, String newS) {
        while(bigString.indexOf(old)>-1) {
            bigString = bigString.substring(0,bigString.indexOf(old)) + newS + bigString.substring(bigString.indexOf(old)+old.length());
        }
        return bigString;
    }

    public static String replaceAllString(String bigString, String old, String newS) {
        while(bigString.indexOf(old)>-1){
            bigString=replaceString(bigString, old, newS);
        }
        return bigString;
    }

    public void goToNextPage(View view) {
        Intent RegisterIntent = new Intent(getApplicationContext(), UserCreation.class);
        RegisterIntent.putExtra("FBemail",fbemail);
        RegisterIntent.putExtra("FBid",fbid);
        startActivity(RegisterIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tc, menu);
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
