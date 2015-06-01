package mobi.meerchat.meerchat2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.net.http.AndroidHttpClient.*;

import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.android.Facebook;
import com.facebook.model.GraphMultiResult;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class MainActivity extends Activity {
    private BroadcastReceiver receiver;
    public static Activity activity;
    public static String phpUrl = "http://bafit.mobi/cScripts/v1/";
    String get_id, get_name, get_gender, get_email, get_birthday, get_locale, get_location;
    private static final List<String> PERMISSIONSFRD = Arrays.asList("user_friends");
    LoginButton loginButton;
    //private Facebook mFacebook; deprecated
    private Session.StatusCallback statusCallback = new Session.StatusCallback()
    {
        public void call(Session session, SessionState state, Exception exception)
        {
            if (state.isOpened())
            {
                Request.newMeRequest(session, new Request.GraphUserCallback()
                {

                    // callback after Graph API response with user object
                    @Override
                    public void onCompleted(GraphUser user, Response response)
                    {
                        if (user != null)
                        {
                            if (response != null)
                            {
                                // do something with <response> now
                                try
                                {
                                    get_id = user.getId();
                                    get_name = user.getName();
                                    get_gender = (String) user.getProperty("gender");
                                    get_email = (String) user.getProperty("email");
                                    get_birthday = user.getBirthday();
                                    get_locale = (String) user.getProperty("locale");
                                    get_location = user.getLocation().toString();

                                    Log.d("DATAFORREQUEST", user.getId() + "; " +
                                            user.getName() + "; " +
                                            (String) user.getProperty("gender") + "; " +
                                            (String) user.getProperty("email") + "; " +
                                            user.getBirthday() + "; " +
                                            (String) user.getProperty("locale") + "; " +
                                            user.getLocation());
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    Log.d("DATAFORREQUEST", "Exception e");
                                }
                            }
                        }
                    }
                }).executeAsync();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(new String[]{"public_profile","email","user_friends"}));
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Session session = Session.getActiveSession();
        if (session!=null && session.isOpened()) {
            Log.v("sessionExisting","session exists");
            List<String> permissions = new ArrayList<String>();
            permissions.add("email");
            permissions.add("user_friends");

            openActiveSession(this, true, new Session.StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                    if (session.isOpened()) {
                        //make request to the /me API
                        Log.e("sessionopened", "true");

                        final Session mySession = session;
                        Request.newMeRequest(session, new Request.GraphUserCallback() {
                            @Override
                            public void onCompleted(GraphUser user, Response response) {
                                if (user != null) {
                                    //String friends = user.getProperty("user_friends").toString();
                                    //Log.v("DATAFORREQUEST",user.getProperty("user_friends").toString());
                                    final String email = user.getProperty("email").toString();
                                    final String json = user.getInnerJSONObject().toString();
                                    final String fbid = user.getId().toString();

                                    Log.e("DATAFORREQUEST", email);
                                    Log.e("DATAFORREQUEST", user.getInnerJSONObject().toString());
                                    //JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
                                    //Log.e("DATAFORREQUEST",graphResponse.toString());

                                    //I'm not sure if nested requests make sense.......
                                    new Request(
                                            mySession,
                                            "/me/friends",
                                            null,
                                            HttpMethod.GET,
                                            new Request.Callback() {
                                                public void onCompleted(Response response) {
                                                    final JSONObject graphResponse = response.getGraphObject()
                                                            .getInnerJSONObject();
                                                    Log.e("DATAFORREQUEST", graphResponse.toString());

                                                    //WEB REQUESTS
                                                    Thread t = new Thread(new Runnable() {
                                                        public void run() {
                                                            try {
                                                                String data = getData(phpUrl + "userExists.php?FBemail=" + email);
                                                                Log.i("WEBREQUESTS1", data); //comma implies doesn't exist; else replies UID, bun
                                                                //String data = "";
                                                                //eg 1645ED10-7AC8-47B1-BC8B-87DC48793780,jbtt
                                                                //data = getData("http://bafit.mobi/cScripts/v1/sendFBdata.php?FBemail=smritisadman@yahoo.com&Data={%22id%22:%22772021986186301%22,%22first_name%22:%22Khondaker%22,%22timezone%22:-4,%22email%22:%22smritisadman@yahoo.com%22,%22verified%22:true,%22name%22:%22Khondaker%20Sadman%22,%22locale%22:%22en_US%22,%22link%22:%22https:\\/\\/www.facebook.com\\/app_scoped_user_id\\/772021986186301\\/%22,%22last_name%22:%22Sadman%22,%22gender%22:%22male%22,%22updated_time%22:%222013-10-07T21:48:14+0000%22}&Flist={%22summary%22:{%22total_count%22:379},%22data%22:[{%22id%22:%2210204824602787428%22,%22name%22:%22Matthew%20Weingarten%22}],%22paging%22:{%22next%22:%22https:\\/\\/graph.facebook.com\\/v2.2\\/772021986186301\\/friends?format=json&access_token=CAAWzHZCvITMwBAJslCZC5UbTQIHiL5ZBEbXZBnZA2gac1khcrLswScqWa4uQospp3T8As9sd8IgdWhlpZBDWWNENnZAzdWIX7NDMw7JKU5uaiIIbZBewxUjg0pNekpZBvPpZAQTUlJaTfiJ5sg74Gf6d22PrcARc6u9mlbVxBNZBZAkXWmqw9LxhWp92wKCuzekTLeizdkbup8IabEuPFp7Ov1l1&limit=25&offset=25&__after_id=enc_AdADw07CEU1t5RTdnCZAZBFxbYMl9x6ZBqxZBCpuzpMCLNSdJcDZBjcZBRda826NtE6S72np8ZD%22}}");
                                                                //Log.i("WEBREQUESTS",data);

                                                                //was >-1
                                                                if (data.indexOf(",") == 0) { //somehow if this person does exist do an intent to carousel, else continue login. idk if ,
                                                                    Log.i("WEBREQUESTS", "continue registration");
                                                                    Log.i("WEBREQUESTS", phpUrl + "sendFBdata.php?FBemail=" + email + "&Data=" + json + "&Flist=" + graphResponse.toString());
                                                                    data = getData2(phpUrl + "sendFBdata.php?FBemail=" + email + "&Data=" + json + "&Flist=" + graphResponse.toString());
                                                                    Log.v("WEBREQUESTS", "about to data");
                                                                    Log.i("WEBREQUESTS", data);
                                                                    Log.v("WEBREQUESTS", "did data");
                                                                    if (data.equals("T")) {
                                                                        Intent RegisterIntent = new Intent(getApplicationContext(), TC.class);
                                                                        RegisterIntent.putExtra("FBemail", email);
                                                                        RegisterIntent.putExtra("FBid", fbid);
                                                                        startActivity(RegisterIntent);
                                                                    } else {
                                                                        Log.v("WEBREQUESTS", "error");
                                                                    }
                                                                } else { //if the user does exist
                                                                    Log.v("WEBREQUESTS", data);
                                                                    Intent carouselIntent = new Intent(getApplicationContext(), CarouselActivity.class);
                                                                    carouselIntent.putExtra("UID", data.substring(0, data.indexOf(",")));
                                                                    carouselIntent.putExtra("BUN", data.substring(data.indexOf(",") + 1));
                                                                    carouselIntent.putExtra("FROM", "MAIN");
                                                                    startActivity(carouselIntent);
                                                                }


                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    });
                                                    t.start();


                                                }
                                            }).executeAsync();

                                }
                            }
                        }).executeAsync();

                    }
                }
            }, permissions);
        }
        if(session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));

            }
        }

        try {
            //openActiveSession(this, true, statusCallback, Arrays.asList(
            //        new String[]{"email", "user_location", "user_birthday",
            //                "user_likes", "publish_actions"}), savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("TIME_TICK");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("WEBREQUESTS","received");
            }
        };
        registerReceiver(receiver,filter);
        //statusCallback.call(mySession,mySession.getState(),null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }



    private Session openActiveSession(Activity activity, boolean allowLoginUI, Session.StatusCallback callback, List<String> permissions, Bundle savedInstanceState)
    {
        Session.OpenRequest openRequest = new Session.OpenRequest(activity).
                setPermissions(permissions).setLoginBehavior(SessionLoginBehavior.
                SSO_WITH_FALLBACK).setCallback(callback).
                setDefaultAudience(SessionDefaultAudience.FRIENDS);

        Session session = Session.getActiveSession();
        Log.d("OPENACTIVESESSION", "" + session);
        getUserData(session,session.getState());
        if (session == null)
        {
            Log.d("OPENACTIVESESSION", "" + savedInstanceState);
            if (savedInstanceState != null)
            {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null)
            {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED) || allowLoginUI)
            {
                session.openForRead(openRequest);
                Log.d("OPENACTIVESESSION","RETURNING");
                return session;
            }
        }
        return null;
    }

    private void getUserData(Session session, SessionState state)
    {
        if (state.isOpened())
        {
            Request.newMeRequest(session, new Request.GraphUserCallback()
            {
                @Override
                public void onCompleted(GraphUser user, Response response)
                {
                    if (response != null)
                    {
                        try
                        {
                            String name = user.getName();
                            // If you asked for email permission
                            String email = (String) user.getProperty("email");
                            String get_birthday = user.getBirthday();
                            //String get_location = user.getLocation().toString();
                            Log.e("DATAFORREQUEST", "Name: " + name + " Email: " + email + " bday: " + get_birthday + " loc: ");
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            Log.d("DATAFORREQUEST", "Exception e");
                        }

                    }
                }
            }).executeAsync();
        }
    }

    private static Session openActiveSession(Activity activity, boolean allowLoginUI, Session.StatusCallback callback, List<String> permissions) {
        Session.OpenRequest openRequest = new Session.OpenRequest(activity).setPermissions(permissions).setCallback(callback);
        Session session = new Session.Builder(activity).build();
        if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI) {
            Session.setActiveSession(session);
            session.openForRead(openRequest);
            return session;
        }
        return null;
    }

    private void requestFacebookFriends(Session session) {
        Request.executeMyFriendsRequestAsync(session,
                new Request.GraphUserListCallback() {
                    @Override
                    public void onCompleted(List<GraphUser> users,
                                            Response response) {
                        // TODO: your code for friends here!
                        Log.v("DATAFORREQUEST","stuff");
                    }
                });
    }

    private boolean isSubsetOf(Collection<String> subset,Collection<String> superset) {
        for (String string: subset) {
            if(!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }

    private String buildUserInfoDisplay(GraphUser user) {
        StringBuilder userInfo = new StringBuilder("");

        // Example: typed access (name)
        // - no special permissions required
        //Log.v("Stringing",user.getName());
        //userInfo.append(String.format("Name: %s\n\n",
                //user.getName()));

        // Example: typed access (birthday)
        // - requires user_birthday permission
        //Log.v("Stringing",user.getId());
        //userInfo.append(String.format("ID: %s\n\n",
                //user.getId()));

        // Example: partially typed access, to location field,
        // name key (location)
        // - requires user_location permission
        //Log.v("Stringing", user.getLink());
        //userInfo.append(String.format("Link: %s\n\n",
        //        user.getLink()));

        // Example: access via property name (locale)
        // - no special permissions required
        //Log.v("Stringing", (String) user.getProperty("locale"));
        //userInfo.append(String.format("Locale: %s\n\n",
        //        user.getProperty("locale")));

        //Log.v("Stringing",user.getFirstName());
        //userInfo.append(String.format("FName: %s\n\n",user.getFirstName()));

        //Log.v("Stringing",user.getLastName());
        //userInfo.append(String.format("FName: %s\n\n",user.getLastName()));

        //Log.v("Stringing",(String) user.getProperty("gender"));
        //userInfo.append(String.format("Gender: %s\n\n",user.getProperty("gender")));

        //Log.v("Stringing",(String)user.getProperty("timezone"));
        //userInfo.append(String.format("TimeZone: %s\n\n",(String)user.getProperty("timezone")));

        //Log.v("Stringing",(String)user.getProperty("updated_time"));
        //userInfo.append(String.format("Updated_Time %s\n\n",(String)user.getProperty("updated_time")));

        //Log.v("Stringing",user.getInnerJSONObject().toString());
        //Log.v("Stringing",(String)user.getProperty("is_verified"));
        //userInfo.append(String.format("Verified %s\n\n",(String)user.getProperty("is_verified")));

        // Example: access via key for array (languages)
        // - requires user_likes permission
        Log.v("DATAFORREQUEST",(String)user.getProperty("email"));
        return user.getInnerJSONObject().toString();
        ///return userInfo.toString();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        Log.d("DATAFORREQUEST", "in activity result");

        List<String> permissions = new ArrayList<String>();
        permissions.add("email");
        permissions.add("user_friends");

        openActiveSession(this, true, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {
                    //make request to the /me API
                    Log.e("sessionopened", "true");

                    final Session mySession = session;
                    Request.newMeRequest(session, new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                //String friends = user.getProperty("user_friends").toString();
                                //Log.v("DATAFORREQUEST",user.getProperty("user_friends").toString());
                                final String email = user.getProperty("email").toString();
                                final String json = user.getInnerJSONObject().toString();
                                final String fbid = user.getId().toString();

                                Log.e("DATAFORREQUEST", email);
                                Log.e("DATAFORREQUEST", user.getInnerJSONObject().toString());
                                //JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
                                //Log.e("DATAFORREQUEST",graphResponse.toString());

                                //I'm not sure if nested requests make sense.......
                                new Request(
                                        mySession,
                                        "/me/friends",
                                        null,
                                        HttpMethod.GET,
                                        new Request.Callback() {
                                            public void onCompleted(Response response) {
                                                final JSONObject graphResponse = response.getGraphObject()
                                                        .getInnerJSONObject();
                                                Log.e("DATAFORREQUEST", graphResponse.toString());

                                                //WEB REQUESTS
                                                Thread t = new Thread(new Runnable() {
                                                    public void run() {
                                                        try {
                                                            String data = getData(phpUrl + "userExists.php?FBemail=" + email);
                                                            Log.i("WEBREQUESTS1", data); //comma implies doesn't exist; else replies UID, bun
                                                            //String data = "";
                                                            //eg 1645ED10-7AC8-47B1-BC8B-87DC48793780,jbtt
                                                            //data = getData("http://bafit.mobi/cScripts/v1/sendFBdata.php?FBemail=smritisadman@yahoo.com&Data={%22id%22:%22772021986186301%22,%22first_name%22:%22Khondaker%22,%22timezone%22:-4,%22email%22:%22smritisadman@yahoo.com%22,%22verified%22:true,%22name%22:%22Khondaker%20Sadman%22,%22locale%22:%22en_US%22,%22link%22:%22https:\\/\\/www.facebook.com\\/app_scoped_user_id\\/772021986186301\\/%22,%22last_name%22:%22Sadman%22,%22gender%22:%22male%22,%22updated_time%22:%222013-10-07T21:48:14+0000%22}&Flist={%22summary%22:{%22total_count%22:379},%22data%22:[{%22id%22:%2210204824602787428%22,%22name%22:%22Matthew%20Weingarten%22}],%22paging%22:{%22next%22:%22https:\\/\\/graph.facebook.com\\/v2.2\\/772021986186301\\/friends?format=json&access_token=CAAWzHZCvITMwBAJslCZC5UbTQIHiL5ZBEbXZBnZA2gac1khcrLswScqWa4uQospp3T8As9sd8IgdWhlpZBDWWNENnZAzdWIX7NDMw7JKU5uaiIIbZBewxUjg0pNekpZBvPpZAQTUlJaTfiJ5sg74Gf6d22PrcARc6u9mlbVxBNZBZAkXWmqw9LxhWp92wKCuzekTLeizdkbup8IabEuPFp7Ov1l1&limit=25&offset=25&__after_id=enc_AdADw07CEU1t5RTdnCZAZBFxbYMl9x6ZBqxZBCpuzpMCLNSdJcDZBjcZBRda826NtE6S72np8ZD%22}}");
                                                            //Log.i("WEBREQUESTS",data);

                                                            //was >-1
                                                            if (data.indexOf(",") == 0) { //somehow if this person does exist do an intent to carousel, else continue login. idk if ,
                                                                Log.i("WEBREQUESTS", "continue registration");
                                                                Log.i("WEBREQUESTS", phpUrl + "sendFBdata.php?FBemail=" + email + "&Data=" + json + "&Flist=" + graphResponse.toString());
                                                                data = getData2(phpUrl + "sendFBdata.php?FBemail=" + email + "&Data=" + json + "&Flist=" + graphResponse.toString());
                                                                Log.v("WEBREQUESTS", "about to data");
                                                                Log.i("WEBREQUESTS", data);
                                                                Log.v("WEBREQUESTS", "did data");
                                                                if (data.equals("T")) {
                                                                    Intent RegisterIntent = new Intent(getApplicationContext(), TC.class);
                                                                    RegisterIntent.putExtra("FBemail", email);
                                                                    RegisterIntent.putExtra("FBid", fbid);
                                                                    startActivity(RegisterIntent);
                                                                } else {
                                                                    Log.v("WEBREQUESTS", "error");
                                                                }
                                                            } else { //if the user does exist
                                                                Log.v("WEBREQUESTS", data);
                                                                Intent carouselIntent = new Intent(getApplicationContext(), CarouselActivity.class);
                                                                carouselIntent.putExtra("UID", data.substring(0, data.indexOf(",")));
                                                                carouselIntent.putExtra("BUN", data.substring(data.indexOf(",") + 1));
                                                                carouselIntent.putExtra("FROM", "MAIN");
                                                                startActivity(carouselIntent);
                                                            }


                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                });
                                                t.start();


                                            }
                                        }).executeAsync();

                            }
                        }
                    }).executeAsync();

                }
            }
        }, permissions);

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

    public static String replaceString(String bigString, String old, String newS) {
        while(bigString.indexOf(old)>-1) {
            bigString = bigString.substring(0,bigString.indexOf(old)) + newS + bigString.substring(bigString.indexOf(old)+old.length());
        }
        return bigString;
    }

    public static String getData2(String url) {
        String encodedurl = "";
        //String url2 = "";
        //String url3 = "";
        Log.v("WEBREQUESTS","getting data2");
        try {
            while(url.indexOf("\\")>-1){
                url = url.substring(0,url.indexOf("\\")) + url.substring(url.indexOf("\\")+1);
                Log.v("WEBREQUESTS",url);
            }
            //url2 = url;
            //url3 = url2.replaceAll("\\","");
            Log.v("WEBREQUESTS",url);
            encodedurl = URLEncoder.encode(url.substring(url.indexOf("?") + 1), "UTF-8");
            String phpFunction = url.substring(url.indexOf("/v1/") + 4, url.indexOf("?") + 1);
            encodedurl = "http://bafit.mobi/cScripts/v1/" + phpFunction + encodedurl;
            //keep changing this until the encodedurl works in browser
            encodedurl = replaceString(encodedurl,"%3D","=");
            encodedurl = replaceString(encodedurl,"%26","&");
            encodedurl = replaceString(encodedurl,"%5f","_"); //maybe?
            /**
            while(encodedurl.indexOf("%3D")>-1){
                encodedurl = encodedurl.substring(0,encodedurl.indexOf("%3D")) + "="+encodedurl.substring(encodedurl.indexOf("%3D")+3);
                Log.v("WEBREQUESTS",encodedurl);
            }
             */


            Log.v("WEBREQUESTS",encodedurl);
        } catch(Exception e) {
            Log.v("WEBREQUESTS","exception");
            e.printStackTrace();
        }
        //String result = getData(encodedurl);




        HttpResponse response;
        try {
            //HttpClient httpclient = new DefaultHttpClient();
            AndroidHttpClient httpclient = AndroidHttpClient.newInstance("Android");
            response = httpclient.execute(new HttpPost(encodedurl));
            httpclient.close();
            Log.v("WEBREQUESTS","EXECUTED");
        } catch (Exception e) {
            Log.v("WEBREQUESTS", "httppostt broke", e);
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



private class SessionStatusCallback implements Session.StatusCallback {
    @Override
    public void call(Session session, SessionState state, Exception exception) {
        // Respond to session state changes, ex: updating the view
    }
}

}

