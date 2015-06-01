package mobi.meerchat.meerchat2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.facebook.Session;

//"Class 'bafitinc.bafit.MainActivity clashes with package of same name" so I deleted folder MainActivity in this directory
public class CarouselActivity extends FragmentActivity {
    public final static int PAGES = 100;
    public final static int LOOPS = 10000;
    //  public final static int FIRST_PAGE = PAGES * LOOPS / 2;
    public final static int FIRST_PAGE = 0;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 1.0f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    private ImageButton move = null;
    private ImageButton grub = null;
    private ImageButton love = null;
    private ImageButton study = null;
    private ImageButton btnFeedback = null;
    int category = -1;
    static int initialLoad = 4;
    static String listOfVideos = "";

    public MyPagerAdapter adapter;
    public ViewPager pager;

    //public static ArrayList<File> fileList;
    public static ArrayList<String> vuriList;
    public static ArrayList<Drawable> thumbList;
    //  public static Iterator<File> fIterator;
    public static File rd;
    Bundle extras;
    static String fbid = "";
    String bun="";
    String from = "";
    String outputFileName = "";
    static double longitude;
    static double latitude;

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    //for files, doesn't matter anymore
    public void listf(File directory, ArrayList<File> files) {
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                listf(file, files);
            }
        }
    }


    private void chooseCategory(int chosenCategory)
    {
        boolean didSomething = false;
        for(int i=0; i<5; i++) {
            if(category==i) {
                //do something in another method
                chooseCategoryHelper(i,false);
                didSomething=true;
            }
        }
        chooseCategoryHelper(chosenCategory,true);
        category=chosenCategory;
    }

    private void chooseCategoryHelper(int cat,boolean chosen) {
        if(cat==0) {
            if(chosen) {
                move.setBackgroundResource(R.mipmap.move_btn_active);
            }
            else {
                move.setBackgroundResource(R.mipmap.move_btn_inactive);
            }
        }
        if(cat==1) {
            if(chosen) {
                grub.setBackgroundResource(R.mipmap.grub_btn_active);
            }
            else {
                grub.setBackgroundResource(R.mipmap.grub_btn_inactive);
            }
        }
        if(cat==2) {
            if(chosen) {
                love.setBackgroundResource(R.mipmap.love_btn_active);
            }
            else {
                love.setBackgroundResource(R.mipmap.love_btn_inactive);
            }
        }
        if(cat==3) {
            if(chosen) {
                study.setBackgroundResource(R.mipmap.study_btn_active);
            }
            else {
                study.setBackgroundResource(R.mipmap.study_btn_inactive);
            }
        }
    }

    public static String getData (String url) {
        InputStream content = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            //String result = EntityUtils.toString(response);
            //content = response.getEntity().getContent();
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            return responseString;
        } catch (Exception e) {
            Log.v("[GET REQUEST]", "Network exception", e);
            return "";
        }
    }

    //use this function with thumbList to put the drawables in here, then refer to thumbList to add pictures to buttons in fragment
    public static Drawable getDrawableFromUrl(URL url) {
        try {
            InputStream is = url.openStream();
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
            // e.printStackTrace();
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return null;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            extras = getIntent().getExtras();
            if(extras==null) {
                fbid=null;
                bun=null;
                from=null;
            } else {
                fbid=extras.getString("UID");
                bun = extras.getString("BUN");
                from=extras.getString("FROM");
            }
        } else {
            fbid = (String)savedInstanceState.getSerializable("UID");
            bun = (String)savedInstanceState.getSerializable("BUN");
            from = (String)savedInstanceState.getSerializable("FROM");
        }
        Log.v("Carousel",fbid+","+bun+","+from);


        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    String data = getData(MainActivity.phpUrl+"requestUserList.php?UIDr="+fbid+"&&GPSlat="+latitude+"&GPSlon="+longitude+"&Filter=1&FilterValue=0&Oby=0");
                    listOfVideos = data;
                    //String data = getData(MainActivity.phpUrl+"requestUserList.php?UIDr="+fbid+"&&GPSlat="+30.0+"&GPSlon="+80.0+"&Filter=1&FilterValue=0&Oby=0");

                    Log.v("server", data);
                    String vuri = "";
                    while (data.indexOf("vidURI")>-1 && vuriList.size()<initialLoad) { //it seems like there are issues if I let it have more than this many; maybe load more stuff as the person scrolls through carosel?
                        data = data.substring(data.indexOf("vidURI")+9);
                        vuri = data.substring(0,data.indexOf("\""));
                        Log.v("vUri",vuri);
                        thumbList.add(getDrawableFromUrl(new URL("http://bafit.mobi/userPosts/thumb/"+vuri+".jpg")));
                        vuriList.add(vuri);
                        //make thumblist general with vuri later
                        Log.i("thumb","http://bafit.mobi/userPosts/thumb/"+vuri+".jpg");

                        //thumbList.add(getDrawableFromUrl(new URL("http://bafit.mobi/userPosts/thumb/536F2E74-4D4E-4096-98D7-8A3CD334E1C5_536F2E74-4D4E-4096-98D7-8A3CD334E1C5_131.jpg")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();


        setContentView(R.layout.activity_carousel);

        pager = (ViewPager) findViewById(R.id.myviewpager);

        adapter = new MyPagerAdapter(this, this.getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(adapter);


        // Set current item to the middle page so we can fling to both
        // directions left and right
        pager.setCurrentItem(FIRST_PAGE); //was not false

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.setOffscreenPageLimit(3);

        // Set margin for pages as a negative number, so a part of next and
        // previous pages will be showed
        pager.setPageMargin(-275);

        //post button
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.post3);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.mainview);
        Button btnAll = new Button(this);
        btnAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                Intent postIntent = new Intent(getApplicationContext(), Post.class);
                postIntent.putExtra("FBID",fbid);
                postIntent.putExtra("BUN",bun);
                postIntent.putExtra("LATITUDE",latitude);
                postIntent.putExtra("LONGITUDE",longitude);
                startActivity(postIntent);
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width/10,width/10);
        //btnAll.setBackgroundColor(80000000);
        params.leftMargin=width*9/10;
        params.topMargin=width/20;
        rl.addView(btnAll, params);

        Button btnChat = new Button(this);
        btnChat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                //Intent chatIntent = new Intent(getApplicationContext(), bafitinc.bafit.ChatList.class); //was Chat
                //startActivity(chatIntent);
            }
        });
        params = new RelativeLayout.LayoutParams(width/10,width/10);
        //btnAll.setBackgroundColor(80000000);
        params.leftMargin=0;
        params.topMargin=width/20;
        rl.addView(btnChat,params);

        btnFeedback = new ImageButton(this);
        btnFeedback.setBackgroundResource(R.mipmap.feedback_btn2);
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                //Intent feedbackIntent = new Intent(getApplicationContext(), bafitinc.bafit.Feedback.class); //was Chat
                //startActivity(feedbackIntent);
            }
        });
        params = new RelativeLayout.LayoutParams(width/3,width/7);
        //btnAll.setBackgroundColor(80000000);
        params.leftMargin=width*7/10;
        params.topMargin=height*21/25;
        rl.addView(btnFeedback,params);

        move = new ImageButton(this);
        move.setBackgroundResource(R.mipmap.move_btn_inactive);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                chooseCategory(0);
            }
        });
        params = new RelativeLayout.LayoutParams(width/4,height*3/40);
        params.leftMargin=0;
        params.topMargin=height*3/50;
        rl.addView(move,params);

        grub = new ImageButton(this);
        grub.setBackgroundResource(R.mipmap.grub_btn_inactive);
        grub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                chooseCategory(1);
            }
        });
        params = new RelativeLayout.LayoutParams(width/4,height*3/40);
        params.leftMargin=width/4;
        params.topMargin=height*3/50;
        rl.addView(grub,params);

        love = new ImageButton(this);
        love.setBackgroundResource(R.mipmap.love_btn_inactive);
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                chooseCategory(2);
            }
        });
        params = new RelativeLayout.LayoutParams(width/4,height*3/40);
        params.leftMargin=width/2;
        params.topMargin=height*3/50;
        rl.addView(love,params);

        study = new ImageButton(this);
        study.setBackgroundResource(R.mipmap.study_btn_inactive);
        study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                chooseCategory(3);
            }
        });
        params = new RelativeLayout.LayoutParams(width/4,height*3/40);
        params.leftMargin=width*3/4;
        params.topMargin=height*3/50;
        rl.addView(study, params);



        //vuri webrequest was here


        //returns jason array: UID of user, username, distance, vidURI, hash, category, messae count
        //bafit.mobi/userPosts/vidURI.jpg for thumbnail
        //bafit.mobi/vidURI.mp4 for video
        //http://bafit.mobi/cScripts/v1/requestUserList.php?UIDr=2&&GPSlat=12.12&GPSlon=15.15&Filter=1&FilterValue=1&Oby=0

        if(isExternalStorageReadable()) {
            String directory = Environment.getExternalStorageDirectory()+"/Bafit/vids"; //get bafit directory
            rd = new File(directory);
            //fileList = new ArrayList<File>();
            vuriList = new ArrayList<String>();
            thumbList = new ArrayList<Drawable>();
            //listf(rd, fileList); THIS LINE MAY BE CAUSING DEVICE ISSUES
//      fIterator = fileList.iterator();
        }

        Log.v("VerificationCode","trying");
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Log.v("VerificationCode","getting location");
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //maybe GPS_PROVIDER
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        Log.v("VerificationCode",longitude+","+latitude);
        MyLL ll = new MyLL();
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,ll);

        //this was where posting was happening


    }

    private class MyLL implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            //do stuff
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //do stuf
        }
        @Override
        public void onProviderEnabled(String provider) {
            //do stuff
        }
        @Override
        public void onProviderDisabled(String provider) {
            //do stuff
        }
    }


    public void uploadFile() {
        try {
            FileInputStream fstrm = new FileInputStream(outputFileName);
            Log.v("PostActivity",outputFileName+"fos");
            HttpFileUpload hfu = new HttpFileUpload(MainActivity.phpUrl+"uploadVid.php",outputFileName);
            hfu.Send_Now(fstrm);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void uploadFileThumb() {
        try {
            String outputThumbName = MainActivity.replaceString(outputFileName,"mp4","jpg");
            //outputThumbName= MainActivity.replaceString(outputThumbName,"/vids/","/vids/thumb/");
            Log.v("PostActivity",outputThumbName);
            FileInputStream fstrm = new FileInputStream(outputThumbName);
            Log.v("PostActivity",outputThumbName);
            HttpFileUpload hfu = new HttpFileUpload(MainActivity.phpUrl+"uploadThumb.php",outputThumbName);
            hfu.Send_Now(fstrm);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
