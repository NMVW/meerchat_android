package mobi.meerchat.meerchat2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//"Class 'bafitinc.bafit.MainActivity clashes with package of same name" so I deleted folder MainActivity in this directory
public class MyFragment extends Fragment {

    File delFile = null;
    Bitmap thumb = null;
    String vuri = "";
    int pos = 0;
    String videoList = "";
    String data="";
    //ArrayList<String> vuriList = MainActivity.vuriList; //if this breaks everything, comment it out

    public static Fragment newInstance(CarouselActivity context, int pos,
                                       float scale)
    {
        Log.v("fragment","instance");
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putFloat("scale", scale);
        return Fragment.instantiate(context, MyFragment.class.getName(), b);
    }



    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

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

    //I might try just making vuriList in here since I can't do MainActivity.vuriList for some reason (it shows up empty)
    public static String getData (String url) {
        InputStream content = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            return responseString;
        } catch (Exception e) {
            Log.v("[GET REQUEST]", "Network exception", e);
            return "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        LinearLayout l = (LinearLayout)
                inflater.inflate(R.layout.mf, container, false);

        pos = this.getArguments().getInt("pos");
        Log.v("Flipping","pos: "+pos);
        while(pos>=CarouselActivity.vuriList.size() && pos<CarouselActivity.initialLoad) {
            try{
                Log.v("sleep","sleep loop"+pos);
                Thread.sleep(1000);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        //while(CarouselActivity.vuriList.size()<pos+1){
            Log.v("reload",CarouselActivity.listOfVideos);
            if(CarouselActivity.listOfVideos.indexOf("{",2)!=-1) {
                CarouselActivity.listOfVideos = CarouselActivity.listOfVideos.substring(CarouselActivity.listOfVideos.indexOf("{",2));
                if(pos<CarouselActivity.initialLoad) {
                    //doo nothing
                } else {
                    data = CarouselActivity.listOfVideos.substring(CarouselActivity.listOfVideos.indexOf("vidURI")+9);
                    data = data.substring(0,data.indexOf("\""));
                    Log.v("vUri", data);
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.v("reload","http://bafit.mobi/userPosts/thumb/" + data + ".jpg");
                                CarouselActivity.thumbList.add(getDrawableFromUrl(new URL("http://bafit.mobi/userPosts/thumb/" + data + ".jpg")));
                            } catch(Exception e) {
                                Log.v("reload","thumb reload failed");
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                    Log.v("reload", data +" "+ CarouselActivity.vuriList.size());
                    CarouselActivity.vuriList.add(data);
                    Log.v("reload", data + " "+ CarouselActivity.vuriList.size());
                }
            }
        //}
        //videostring = CA.listofvideos
        //for(0 to vurlist.size()) {
        //videostring=videostring.substring(indexOf(""));
        //ca.vurilist.add();
        TextView tv = (TextView) l.findViewById(R.id.text);
        final Button btn = (Button) l.findViewById(R.id.content);
        final VideoView myVideoView = (VideoView) l.findViewById(R.id.myvideoview);


        Log.v("sleep","about to sleep while thread going in activity");
        if (pos==0) {
            try {
                Thread.sleep(2000);
                Log.v("sleep", "Im done sleeping");
            } catch (Exception e) {
                Log.v("sleep", "I couldnt sleep");
                e.printStackTrace();
            }
        }

        Log.i("thumb",""+CarouselActivity.vuriList.size());
        //sleep loop

        while(pos>=CarouselActivity.thumbList.size()) {
            try{
                Log.v("sleep","sleep loop"+pos);
                Thread.sleep(1000);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        if(pos < CarouselActivity.vuriList.size()) { //was vurilist

            Log.i("thumb",pos+",1"); //I've deteremined that for some reason the if returns False
            Drawable draw;
            Log.i("thumb","2");
            draw = CarouselActivity.thumbList.get(pos);
            Log.i("thumb","3");
            btn.setBackground(draw);
            Log.i("thumb","4");
            /**
             Bitmap backgroundBitmap = null;
             Thread r = new Thread(new Runnable() {
             public void run() {
             try { //make the number general based on the jason array
             thumb = getBitmapFromURL("http://bafit.mobi/userPosts/thumb/536F2E74-4D4E-4096-98D7-8A3CD334E1C5_536F2E74-4D4E-4096-98D7-8A3CD334E1C5_131.jpg");
             Log.v("sleep","I found background");
             } catch (Exception e) {
             Log.v("sleep","I couldnt find background");
             e.printStackTrace();
             }
             }
             });
             r.start();
             try {
             Thread.sleep(1000);
             Log.v("sleep", "Im done sleeping");
             } catch (Exception e) {
             Log.v("sleep", "I couldnt sleep");
             e.printStackTrace();
             }
             draw = new BitmapDrawable(getResources(),backgroundBitmap);
             Log.v("sleep","I set draw");
             btn.setBackground(draw);
             btn.setVisibility(btn.VISIBLE);
             Log.v("sleep","I set background" + pos);
             /*

             //      Drawable d = Drawable.createFromPath(MainActivity.fileList.get(pos).getAbsolutePath());
             //      btn.setBackground(d);
             //Bitmap thumb = ThumbnailUtils.createVideoThumbnail(MainActivity.fileList.get(pos).getAbsolutePath(),MediaStore.Images.Thumbnails.MINI_KIND);
             //Bitmap thumb;
             //Log.v("drawable test",""+pos);
             /**
             Thread t = new Thread(new Runnable() {
             public void run() {
             try { //make the number general based on the jason array

             thumb = getBitmapFromURL("http://bafit.mobi/userPosts/thumb/536F2E74-4D4E-4096-98D7-8A3CD334E1C5_536F2E74-4D4E-4096-98D7-8A3CD334E1C5_131.jpg");
             Log.v("test","pos");
             } catch (Exception e) {
             e.printStackTrace();
             }
             }
             });
             t.start();
             */
            //Drawable d = new BitmapDrawable(getResources(),thumb);
            //Drawable d = new BitmapDrawable(getResources(),thumb);
            //btn.setBackground(d);
            /**
             * //move this try catch to where btn.setOnClick is
             try {
             Log.v("drawable test","trying");
             d = getDrawableFromUrl(new URL("http://bafit.mobi/userPosts/thumb/536F2E74-4D4E-4096-98D7-8A3CD334E1C5_536F2E74-4D4E-4096-98D7-8A3CD334E1C5_131.jpg"));
             } catch(Exception e) {
             Log.v("drawable test","caught");
             e.printStackTrace();
             }
             btn.setBackground(d);
             */
            //tv.setText("#" + MainActivity.fileList.get(pos).getName());
//    tv.setText("test");

            //myVideoView.setVideoPath(MainActivity.fileList.get(pos).getAbsolutePath());
            vuri = CarouselActivity.vuriList.get(pos);
            myVideoView.setVideoURI(Uri.parse("http://bafit.mobi/userPosts/"+vuri));
            Log.v("VIDEOURI", "http://bafit.mobi/userPosts/" + vuri);
            myVideoView.seekTo(100); //FEB11
            myVideoView.setMediaController(new MediaController(getActivity()));
            myVideoView.requestFocus();


            btn.setOnClickListener(new MyLovelyOnClickListener(pos, CarouselActivity.vuriList) {

                boolean t1 = true;
                boolean t2 = true;

                public void onClick(View v) {
                    Log.v("test", "LCC clicked");
                    Log.v("test", CarouselActivity.vuriList.size() + "size2");
                    if (t1) {
                        btn.setVisibility(btn.INVISIBLE);
                        myVideoView.setVideoURI(Uri.parse("http://bafit.mobi/userPosts/" + myVuriList.get(myLovelyVariable) + ".mp4"));
                        //myVideoView.setVideoURI(Uri.parse("http://bafit.mobi/userPosts/536F2E74-4D4E-4096-98D7-8A3CD334E1C5_536F2E74-4D4E-4096-98D7-8A3CD334E1C5_131.mp4"));
                        //maybe myVideoView.setMediaController(new MediaController(this));
                        Log.v("test", "" + myLovelyVariable);
                        Log.v("test", myVuriList.get(myLovelyVariable));
                        myVideoView.setVisibility(myVideoView.VISIBLE);
                        myVideoView.start();
                        t1 = false;
                    } else {
                        if (t2) {
                            myVideoView.suspend();
                            myVideoView.setVisibility(myVideoView.INVISIBLE);
                            btn.setVisibility(btn.VISIBLE);
                            t2 = false;
                        } else {
                            btn.setVisibility(btn.INVISIBLE);
                            myVideoView.setVisibility(myVideoView.VISIBLE);
                            myVideoView.resume();
                            t2 = true;
                        }
                    }
                }
            });


        }

        MyLinearLayout root = (MyLinearLayout) l.findViewById(R.id.root);
        float scale = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);

        //slide function
        //http://stackoverflow.com/questions/11421368/android-fragment-oncreateview-with-gestures
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        Log.v("gesture","touched");
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        Log.i("tag", "onFling has been called!");
                        final int SWIPE_MIN_DISTANCE = 60; //was 120
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 25; //was 100
                        Log.v("tag",e1.getX()+","+e1.getY()+" "+e2.getX()+","+e2.getY());

                        //http://stackoverflow.com/questions/7321817/android-get-video-source-path-from-videoview/12360508#12360508
                        Uri mUri = null;
                        try {
                            Field mUriField = VideoView.class.getDeclaredField("mUri");
                            mUriField.setAccessible(true);
                            mUri = (Uri)mUriField.get(myVideoView);
                        } catch(Exception e) {}
                        //Toast.makeText(getActivity(), mUri.toString(), Toast.LENGTH_SHORT).show();

                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            Log.i("swipe","y's," + e1.getY() + "and" + e2.getY() + "and" + velocityY);
                            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("tag", "UP");
                                //Toast.makeText(getActivity(), "up", Toast.LENGTH_SHORT).show();
                                Log.i("tag","trying intent");
                                //Intent upIntent = new Intent(getActivity(),Forth.class);
                                Log.i("tag","did intent" + mUri.toString()); //FEB11
                                //upIntent.putExtra("FILENAME",mUri.toString());
                                //startActivity(upIntent);
                            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("tag", "DOWN");
                                Thread t = new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            final String video = CarouselActivity.vuriList.get(pos);
                                            Log.v("tag",video);

                                            String uidp = video.substring(0,video.indexOf("_"));
                                            while(uidp.indexOf("-")>-1) {
                                                uidp = MainActivity.replaceString(uidp,"-","%2D");
                                            }
                                            Log.v("tag",MainActivity.phpUrl+"notToday.php?UIDr="+CarouselActivity.fbid+"&UIDp="+uidp+"&MC="+video.substring(video.indexOf("_",video.indexOf("_")+1) +1));
                                            String data = MainActivity.getData( MainActivity.phpUrl+"notToday.php?UIDr="+CarouselActivity.fbid+"&UIDp="+uidp+"&MC="+video.substring(video.indexOf("_",video.indexOf("_")+1) +1));
                                            Log.v("tag",data);
                                            if(data.equals("T")) {
                                                CarouselActivity.vuriList.remove(pos);
                                                CarouselActivity.thumbList.remove(pos);
                                                Log.v("tag","removed");
                                            }
                                        } catch(Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                t.start();
                                //CarouselActivity.fileList.remove(0); //change to appropriate index
                                //Log.v("delete",CarouselActivity.fileList.size() + "left");
                                //Intent downIntent = new Intent(getActivity(),Post.class);
                                //startActivity(downIntent);
                            } else if(e2.getX()-e1.getX()>SWIPE_MIN_DISTANCE && pos==CarouselActivity.vuriList.size()-1) {
                                Log.v("tag","right");
                                /**
                                 Get vuri of last thing on initial vurilist
                                 In thread, check vuri of videos starting on position 2
                                 If equal, add videos 3 and 4 (if they exist)
                                 Else, look at position 3, 4, etc.
                                 */
                                final int size = CarouselActivity.vuriList.size();
                                Thread t = new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            String data = getData(MainActivity.phpUrl+"requestUserList.php?UIDr="+CarouselActivity.fbid+"&&GPSlat="+CarouselActivity.latitude+"&GPSlon="+CarouselActivity.longitude+"&Filter=1&FilterValue=0&Oby=0");
                                            Log.v("server", data);
                                            String vuri = "";
                                            while (data.indexOf("vidURI")>-1 && CarouselActivity.vuriList.size()<size+2) { //it seems like there are issues if I let it have more than this many; maybe load more stuff as the person scrolls through carosel?
                                                data = data.substring(data.indexOf("vidURI")+9);
                                                vuri = data.substring(0,data.indexOf("\""));
                                                Log.v("vUri",vuri);
                                                CarouselActivity.thumbList.add(getDrawableFromUrl(new URL("http://bafit.mobi/userPosts/thumb/"+vuri+".jpg")));
                                                CarouselActivity.vuriList.add(vuri);
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
                            } else if (e1.getX()-e2.getX()>SWIPE_MIN_DISTANCE&&pos==0) {
                                Log.v("tag","left");
                                /*
                                Store initial vuri of initial position of vurilist
                                If equal to first position vuri in threa, break
                                Else, add video and check vuri of position 2
                                If those are equal, break
                                Else, add
                                 */
                                String initvuri = CarouselActivity.vuriList.get(0);
                                Thread t = new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            String data = getData(MainActivity.phpUrl+"requestUserList.php?UIDr="+CarouselActivity.fbid+"&&GPSlat="+CarouselActivity.latitude+"&GPSlon="+CarouselActivity.longitude+"&Filter=1&FilterValue=0&Oby=0");
                                            Log.v("server", data);
                                            String vuri = "";
                                            while (data.indexOf("vidURI")>-1 && CarouselActivity.vuriList.size()<2) { //it seems like there are issues if I let it have more than this many; maybe load more stuff as the person scrolls through carosel?
                                                data = data.substring(data.indexOf("vidURI")+9);
                                                vuri = data.substring(0,data.indexOf("\""));
                                                Log.v("vUri",vuri);
                                                if(CarouselActivity.vuriList.contains(vuri)) {
                                                    //do nothing
                                                } else {
                                                    CarouselActivity.thumbList.add(getDrawableFromUrl(new URL("http://bafit.mobi/userPosts/thumb/"+vuri+".jpg")));
                                                    CarouselActivity.vuriList.add(vuri);
                                                }
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
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        l.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
        //Log.v("tesrt",""+pos);

        /**
         Thread t = new Thread(new Runnable() {
         public void run() {
         Drawable d;
         try {
         Log.v("drawable test","trying");
         d = getDrawableFromUrl(new URL("http://bafit.mobi/userPosts/thumb/536F2E74-4D4E-4096-98D7-8A3CD334E1C5_536F2E74-4D4E-4096-98D7-8A3CD334E1C5_131.jpg"));
         btn.setBackground(d);
         } catch(Exception e) {
         Log.v("drawable test","caught");
         e.printStackTrace();
         }
         }
         });
         t.start();
         */

        /**
         Log.v("test","custom runnable");
         Log.v("test",pos + "");
         Log.v("test",vuriList + "");
         String vuriName = vuriList.get(pos);
         Log.v("test","got name");
         Runnable r = new MyThread(btn,vuriName);
         new Thread(r).start();
         */
        /**
         ArrayList<String> fragVuriList = new ArrayList<String>();
         Log.v("runnable","trying the run");
         Runnable r = new MyThread(fragVuriList);
         new Thread(r).start();
         //maybe actually go through jason array and find the vuri then do stuff
         //make general later
         try {
         btn.setBackground(getDrawableFromUrl(new URL("http://bafit.mobi/userPosts/thumb/536F2E74-4D4E-4096-98D7-8A3CD334E1C5_536F2E74-4D4E-4096-98D7-8A3CD334E1C5_131.jpg")));
         } catch(Exception e) {
         e.printStackTrace();
         }
         */































        /**
         btn.setOnClickListener(new View.OnClickListener() {
         boolean t1 = true;
         boolean t2 = true;
         public void onClick(View v) {
         if(t1) {
         btn.setVisibility(btn.INVISIBLE);
         myVideoView.setVideoURI(Uri.parse("http://bafit.mobi/userPosts/536F2E74-4D4E-4096-98D7-8A3CD334E1C5_536F2E74-4D4E-4096-98D7-8A3CD334E1C5_131.mp4"));
         //maybe myVideoView.setMediaController(new MediaController(this));
         //Log.v("test",""+pos);
         myVideoView.setVisibility(myVideoView.VISIBLE);
         myVideoView.start();
         t1 = false;
         }else{
         if(t2) {
         myVideoView.suspend();
         myVideoView.setVisibility(myVideoView.INVISIBLE);
         btn.setVisibility(btn.VISIBLE);
         t2 = false;
         }else{
         btn.setVisibility(btn.INVISIBLE);
         myVideoView.setVisibility(myVideoView.VISIBLE);
         myVideoView.resume();
         t2 = true;
         }
         }
         }
         });
         */

        return l;
    }



}
