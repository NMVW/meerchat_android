package mobi.meerchat.meerchat2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
//import android.graphics.Movie;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

//import libraries.ffmpeg4android.

//https://www.youtube.com/watch?v=ZScE1aXS1Rs
public class Post extends Activity implements SurfaceHolder.Callback, OnInfoListener, OnErrorListener{
    private Button initBtn = null;
    private Button startBtn = null;
    private Button stopBtn = null;
    //private Button playBtn = null;
    //private Button stopPlayBtn = null;
    private VideoView videoView = null;
    private SurfaceHolder holder = null;
    private Camera camera = null;
    private MediaRecorder recorder = null;
    private String outputFile;
    private String outputFileName;
    Timer timer = new Timer();
    ProgressBar mProgress;
    private ToggleButton togAnonymous = null;
    private TextView textAnon = null;
    private ToggleButton togLocation = null;
    private TextView textLoc = null;
    private ImageButton move = null;
    private ImageButton grub = null;
    private ImageButton love = null;
    private ImageButton study = null;
    private ImageButton clear = null;
    private Button flipCamera = null;
    private Button play = null;
    private Button stopPlay = null;
    private ImageButton post = null;
    private TextView noCategory = null;
    private EditText hashtag = null;
    int category = -1;
    boolean tapped = false;
    boolean recording = false;
    //private Button btnAll = null; //this replaces init, start, stop
    Bundle extras;
    String fbid= "";
    String bun = "";
    double longitude=0;
    double latitude=0;
    Context context = this;
    public static int LONG_PRESS_TIME = 500; // Time in miliseconds
    Calendar c;
    int seconds;
    int vidPieceNumber = 1;
    Activity activity = this;
    Boolean front = true;

    //move 1, study 2, love 3, grub 4

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //http://stackoverflow.com/questions/1016896/how-to-get-screen-dimensions
        if(savedInstanceState==null){
            extras = getIntent().getExtras();
            if(extras==null) {
                fbid=null;
                bun=null;
                longitude=0;
                latitude=0;
            } else {
                fbid=extras.getString("FBID");
                bun = extras.getString("BUN");
                longitude=extras.getDouble("LONGITUDE");
                latitude=extras.getDouble("LATITUDE");
            }
        } else {
            fbid = (String)savedInstanceState.getSerializable("FBID");
            bun = (String)savedInstanceState.getSerializable("BUN");
            longitude= (Double) savedInstanceState.getSerializable("LONGITUDE");
            latitude = (Double) savedInstanceState.getSerializable("LATITUDE");
        }
        register();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.v("screensize","x,y" + width + "and" + height);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.myRLL);

        /**
        btnAll = new Button(this);
        btnAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                if(!tapped) {
                    tapped=true;
                    initRecorder();
                    UpdateTask updateTask = new UpdateTask();
                    beginRecording();
                    recording = true;
                    timer.scheduleAtFixedRate(updateTask,0,1000);
                }
                else if(tapped && recording) {
                    recording = false;
                    stopRecording();
                }
                else {
                    Log.v("tag","to be continued"); //later this will allow for start-stop recording
                }
            }
        });
         */
        /**
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width*2/3,width*2/3);
        btnAll.setBackgroundColor(80000000);
        params.leftMargin=width/6;
        params.topMargin=height*3/10;
        rl.addView(btnAll,params);
         */

        /**
        initBtn = new Button(this);
        initBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Toast.makeText(getApplicationContext(), "init", Toast.LENGTH_SHORT).show();
                initRecorder();
                //start functions
                UpdateTask updateTask = new UpdateTask();
                beginRecording();
                timer.scheduleAtFixedRate(updateTask,0,1000);
            }
        });
         */
        /**
         params = new RelativeLayout.LayoutParams(100,100); //dimensions
         params.leftMargin = 0;
         params.topMargin = 300;
         rl.addView(initBtn,params);
         */

        /**
         startBtn = new Button(this);
         startBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View V) {
        Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
        UpdateTask updateTask = new UpdateTask();
        beginRecording();
        timer.scheduleAtFixedRate(updateTask, 0, 1000);
        }
        });
         params = new RelativeLayout.LayoutParams(100,100);
         params.leftMargin=0;
         params.topMargin=400;
         rl.addView(startBtn,params);
         */

        /**
        stopBtn = new Button(this);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                stopRecording();
            }
        });
         */
        /**
         params = new RelativeLayout.LayoutParams(100,100);
         params.leftMargin=0;
         params.topMargin=500;
         rl.addView(stopBtn,params);
         */


        videoView = new VideoView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width*2/3,width*2/3);
        params = new RelativeLayout.LayoutParams(width*2/3,width*2/3);
        params.leftMargin=width/6;
        params.topMargin=height*3/10;
        rl.addView(videoView,params);

        togAnonymous = new ToggleButton(this);
        togAnonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        });
        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width*2/3,width*2/3);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin=width/5;
        params.topMargin=(int)(height*.689);
        rl.addView(togAnonymous, params);

        textAnon = new TextView(this);
        textAnon.setText("share as username?");
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin=width*7/20;
        params.topMargin=(int)(height*.689);
        rl.addView(textAnon, params);

        togLocation = new ToggleButton(this);
        togLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        });
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin=width/4;
        params.topMargin=(int)(height*.751);
        rl.addView(togLocation,params);

        textLoc = new TextView(this);
        textLoc.setText("share location?");
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin=width*2/5;
        params.topMargin=(int)(height*.751);
        rl.addView(textLoc,params);

        mProgress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        params = new RelativeLayout.LayoutParams(width*5/6,(int)(height*.039));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.leftMargin=width/6;
        //mProgress.setBottom(height);
        //params.topMargin= (int) (height*.93);
        rl.addView(mProgress,params);

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
        rl.addView(move, params);

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
        rl.addView(grub, params);

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
        rl.addView(love, params);

        study = new ImageButton(this);
        study.setBackgroundResource(R.mipmap.study_btn_inactive);
        study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Log.v("PostActivity", "send");
                //I'm using this as a proxy send button
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(outputFileName,
                        MediaStore.Images.Thumbnails.MINI_KIND);
                if (thumb == null) {
                    Log.v("PostActivity", "null image");
                }
                saveImageToExternalStorage(thumb);

                // end proxy send button
                Intent returnIntent = new Intent(getApplicationContext(), CarouselActivity.class);
                returnIntent.putExtra("UID", fbid);
                returnIntent.putExtra("BUN", bun);
                returnIntent.putExtra("FROM", category + "," + outputFileName);
                startActivity(returnIntent);
                chooseCategory(3);
            }
        });
        params = new RelativeLayout.LayoutParams(width/4,height*3/40);
        params.leftMargin=width*3/4;
        params.topMargin=height*3/50;
        rl.addView(study, params);


        clear = new ImageButton(this);
        clear.setBackgroundResource(R.mipmap.study_btn_inactive);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Log.v("clear", "clicked clear");
                mProgress.setProgress(0);
                vidPieceNumber = 1;
                flipCamera.setVisibility(View.VISIBLE);
                play.setVisibility(View.INVISIBLE);
                stopPlay.setVisibility(View.INVISIBLE);
            }
        });
        params = new RelativeLayout.LayoutParams(width/4,height*3/40);
        params.leftMargin=0;
        params.topMargin=height*34/40;
        rl.addView(clear, params);

        flipCamera = new Button(this);
        flipCamera.setBackgroundResource(R.mipmap.study_btn_inactive);
        flipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Log.v("flip", "clicked flip");
                front = !front;
                initRecorder();
                //initCamera();
            }
        });
        params = new RelativeLayout.LayoutParams(width/4,height*3/40);
        params.leftMargin=(int)width*5/6;
        params.topMargin=(int) width*3/10;
        rl.addView(flipCamera, params);

        play = new Button(this);
        play.setBackgroundResource(R.mipmap.study_btn_inactive);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Log.v("play", "clicked play");
                playRecording();
                flipCamera.setVisibility(View.INVISIBLE);
                stopPlay.setVisibility(View.VISIBLE);
            }
        });
        params = new RelativeLayout.LayoutParams(width/4,height*3/40);
        params.leftMargin=(int)width*1/6;
        params.topMargin=(int) height*3/10;
        rl.addView(play, params);
        play.setVisibility(View.INVISIBLE);


        stopPlay = new Button(this);
        stopPlay.setBackgroundResource(R.mipmap.study_btn_active);
        stopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Log.v("play", "clicked stopplay");
                videoView.stopPlayback();
                flipCamera.setVisibility(View.VISIBLE);
                stopPlay.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
            }
        });
        params = new RelativeLayout.LayoutParams(width/4,height*3/40);
        params.leftMargin=(int)width*1/6;
        params.topMargin=(int) height*3/10;
        rl.addView(stopPlay, params);
        stopPlay.setVisibility(View.INVISIBLE);

        post = new ImageButton(this);
        post.setBackgroundResource(R.mipmap.study_btn_inactive);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Log.v("time limit", mProgress.getProgress() + "");
                if (mProgress.getProgress() < 3000) {
                    Toast.makeText(activity, "too short", Toast.LENGTH_SHORT).show();
                } else {
                    //merging small video files

                    //String f1 = Post.class.getProtectionDomain().getCodeSource().getLocation().getFile() + "/1.mp4";
                    String f1 = Environment.getExternalStorageDirectory() + "/Bafit/vids/" + 1 + ".mp4";
                    //String f2 = Post.class.getProtectionDomain().getCodeSource().getLocation().getFile() + "/2.mp4";
                    String f2 = Environment.getExternalStorageDirectory() + "/Bafit/vids/" + 2 + ".mp4";
                    //String f3 = Post.class.getProtectionDomain().getCodeSource().getLocation().getFile() + "/3.mp4";
                    String f3 = Environment.getExternalStorageDirectory() + "/Bafit/vids/" + 3 + ".mp4";


                    Log.v("PostActivity", "send");
                    //I'm using this as a proxy send button
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(outputFileName,
                            MediaStore.Images.Thumbnails.MINI_KIND);
                    if (thumb == null) {
                        Log.v("PostActivity", "null image");
                    }
                    saveImageToExternalStorage(thumb);

                    // end proxy send button
                    //outputFileName = Environment.getExternalStorageDirectory()+"/Bafit/vids/output.mp4";


                    try {
                        //Log.v("Carousel",Integer.parseInt(from.substring(0,1))+"");

                        outputFileName = outputFileName;
                        Log.v("PostActivity", "uploading");
                        Thread u = new Thread(new Runnable() {
                            public void run() {
                                Log.v("PostActivity", "inner uploading");
                                uploadFileThumb();
                                Log.v("PostActivity", "uploading2");
                                uploadFile();
                                Log.v("PostActivity", "inner uploaded");


                                Log.v("PostActivity", outputFileName);
                                outputFileName = outputFileName.substring(outputFileName.indexOf("/vids") + 6, outputFileName.indexOf("."));
                                String mc = outputFileName.substring(outputFileName.indexOf("_") + 1);
                                mc = mc.substring(mc.indexOf("_") + 1);
                                String hashtags = TC.replaceAllString(hashtag.getText().toString(), "#", "%23");
                                String request = MainActivity.phpUrl + "postVideo.php?UIDr=" + fbid + "&BUN=" + bun + "&hash_tag=" + "%23carnival" + "&category=" + category + "&GPSLat=" + latitude + "&GPSLon=" + longitude + "&FName=" + outputFileName + "&MC=" + mc + "&FBID=" + fbid;
                                //while(request.indexOf("-")>-1) {
                                //    request=MainActivity.replaceString(request,"-","%2d");
                                //}
                                Log.v("PostActivity", request);
                                String data = MainActivity.getData(request);
                                //make hashtag general
                                Log.v("PostActivity", "posted," + data);
                            }
                        });
                        u.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.v("Carousel", "no int");
                    }


                    Intent returnIntent = new Intent(getApplicationContext(), CarouselActivity.class);
                    returnIntent.putExtra("UID", fbid);
                    returnIntent.putExtra("BUN", bun);
                    returnIntent.putExtra("FROM", category + "," + outputFileName);
                    startActivity(returnIntent);
                }


            }
        });
        params = new RelativeLayout.LayoutParams(width/4,height*3/40);
        params.leftMargin=width*3/4;
        params.topMargin=height*34/40;
        rl.addView(post, params);

        noCategory = new TextView(this);
        noCategory.setText("choose a category");
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin=width/4;
        params.topMargin=height/5;
        rl.addView(noCategory, params);

        hashtag = new EditText(this);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        hashtag.setHint("put hashtags here");
        params.leftMargin=width/4;
        params.topMargin=height/5;
        rl.addView(hashtag, params);
        hashtag.clearFocus();
        hashtag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    Log.v("hashtag","focusChanged");
                    hashtag.setText(TC.replaceAllString(hashtag.getText().toString()," ","#"));
                }
            }
        });
//        hashtag.addTextChangedListener(new TextWatcher()
//        {
//            private String lastValue = "";
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
//                //do nothing
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after)
//            {
//                //do nothing
//            }
//
//            @Override
//            public void afterTextChanged(Editable s)
//            {
//                String newValue = hashtag.getText().toString();
//                if(!newValue.equals(lastValue)) {
//                    hashtag.setText(TC.replaceAllString(hashtag.getText().toString()," ","#"));
//                    lastValue = newValue;
//                }
//
//            }
//        });


        mProgress.setMax(10000);

        c = Calendar.getInstance();
        //initRecorder();


    }

    class UpdateTask extends TimerTask { //more seamless with animation, use onDown, onUp
        //do nothing yet
        public void run() {
            if(recording) {
                Log.v("tag","time tick");
                mProgress.setProgress(mProgress.getProgress()+1000);
            }
        }
    }

    /**
     public void buttonTapped(View view) {
     switch(view.getId()) {
     case R.id.init:
     Toast.makeText(getApplicationContext(), "init", Toast.LENGTH_SHORT).show();
     initRecorder();
     break;
     case R.id.start:
     Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
     UpdateTask updateTask = new UpdateTask();
     beginRecording();
     timer.scheduleAtFixedRate(updateTask,0,1000);
     break;
     case R.id.stop:
     stopRecording();
     break;
     /**
     case R.id.revstart:
     playRecording();
     break;
     case R.id.revstop:
     stopPlayback();
     break;
     //was star slash here
     }
     }
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v("PostUX","touched");
        if(event.getX()>videoView.getX() && event.getX()<videoView.getX()+videoView.getWidth() && event.getY()>videoView.getY() && event.getY()<videoView.getY()+videoView.getHeight()*2) {
            Log.v("PostUX","in video");
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    outputFileName= Environment.getExternalStorageDirectory()+"/Bafit/vids/"+vidPieceNumber+".mp4";
                    //we'll ahve 1.mp4, 2.mp4, 3.mp4 --> glue together to get UID_UID_MC.mp4
                    initRecorder();
                    Log.v("PostUX", "making updatetask");
                    UpdateTask updateTask = new UpdateTask();
                    Log.v("PostUX","updatetask made");
                    beginRecording();
                    Log.v("PostUX", "set timer timer");
                    timer.scheduleAtFixedRate(updateTask, 0, 1000);
                    recording = true;
                    Log.v("PostUX", "look for timer");
                    vidPieceNumber++;
                    break;
                case MotionEvent.ACTION_UP:
                    Log.v("PostUX", "up");
                    stopRecording();
                    recording = false;
                    play.setVisibility(View.VISIBLE);
                    //camera.startPreview();
                    break;
            }
        }
        return true;
    }

    //http://stackoverflow.com/questions/6413700/android-proper-way-to-use-onbackpressed
    public void onBackPressed() {
        Log.v("PostActivity","back pressed");
        finish();
        super.onBackPressed();
    }

    private void stopRecording() {
        if(recorder!=null) {
            recorder.setOnErrorListener(null);
            recorder.setOnInfoListener(null);
            try {
                recorder.stop();
                Log.v("PostUX","stopping recording");
            }
            catch(IllegalStateException e) {
                Log.e("tag","illegalStateException");
            }
            releaseRecorder();
            releaseCamera();
            //startBtn.setEnabled(false);
            //stopBtn.setEnabled(false);
            //playBtn.setEnabled(true);
        }
    }

    private void releaseCamera() {
        if(camera!=null) {
            try {
                camera.reconnect();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            camera.release();
            camera = null;
        }
    }

    private void releaseRecorder() {
        if(recorder!=null) {
            recorder.release();
            recorder = null;
        }
    }

    private void beginRecording() {
        Log.v("PostUX","beginning recording");
        recorder.setOnInfoListener(this);
        recorder.setOnErrorListener(this);
        recorder.start();
        //startBtn.setEnabled(false);
        //stopBtn.setEnabled(true);

    }

    private void initRecorder() {
        initCamera();
        if(recorder !=null) return;
        //Random rand = new Random();

        //outputFileName = Environment.getExternalStorageDirectory()+"/Bafit/vids/"+"testvid"+".mp4"; //get bafit directory and randomize name
        File outFile = new File(outputFileName);
        if(outFile.exists())
            outFile.delete();
        try {
            camera.stopPreview();
            camera.unlock();
            recorder = new MediaRecorder();
            recorder.setCamera(camera);
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC); //was CAMCORDER
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); //was MPEG_4
            recorder.setOrientationHint(270); //new line
            recorder.setVideoSize(352, 288); //352,288
            //recorder.setVideoFrameRate(15);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264); //was MPEG_4_SP
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); //was AMR_NB
            recorder.setMaxDuration(10000);
            recorder.setPreviewDisplay(holder.getSurface());
            Log.v("tag", "setting outputfilename");
            recorder.setOutputFile(outputFileName);
            Log.v("tag", "set outputfilename");
            recorder.prepare();
            Log.v("tag","mediarecorder good");
            //initBtn.setEnabled(false);
            //startBtn.setEnabled(true);
        }
        catch(Exception e) {
            Log.v("tag","failed");
            e.printStackTrace();
        }
    }

    private void playRecording() {
        MediaController mc = new MediaController(this);
        videoView.setMediaController(mc);
        videoView.setVideoPath(outputFileName);
        videoView.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        Log.v("tag","surface create");
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }
        catch (IOException e) {
            Log.v("tag","can't preview");
            e.printStackTrace();
        }
        //initBtn.setEnabled(true);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        /**
        if (recording) {
            recorder.stop();
            recording = false;
        }
        recorder.release();
        finish();
         */


        /**
        Log.v("PostActivity","uploaded");
        Thread u = new Thread(new Runnable() {
            public void run() {
                Log.v("PostActivity","inner uploading2");
                //uploadFile();
                uploadFileThumb();
                Log.v("PostActivity","inner uploaded2");
            }
        });
        u.start();
         */
        //String commandStr = "ffmpeg -y -i /sdcard/videokit/in.mp4 -strict experimental -vf transpose=1 -s 160x120 -r 30 -aspect 4:3 -ab 48000 -ac 2 -ar 22050 -b 2097k /sdcard/videokit/out.mp4";
        //setCommand(commandStr);

        //connect();
        //http://www.igniterealtime.org/builds/smack/dailybuilds/javadoc/org/jivesoftware/smack/XMPPConnection.html
        /**
         SmackAndroid.init(getApplicationContext());
         try {
         Log.v("destroy","trying");
         XMPPConnection con = new XMPPTCPConnection("condo@meerchat.mobi");
         Log.v("destroy","xmppconnection made");
         con.connect();
         Log.v("destroy","connected");
         con.login("condo@meerchat.mobi","condo");
         Log.v("destroy","logged in");
         Chat chat = ChatManager.getInstanceFor(con).createChat("condo@meerchat.mobi", new MessageListener() {
        @Override
        public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
        Log.v("destroy","message" + message);
        }
        });

         chat.sendMessage("l");
         con.disconnect();
         Log.v("destroy","try complete");
         } catch (Exception e) { //this fixes things a lot, but idk why.....
         e.printStackTrace();
         Log.v("destroy","catch");
         }
         */


    }

    //I'm not sure this is necessary
    protected void onResume() {
        Log.v("tag","onResume");
        super.onResume();
        //initBtn.setEnabled(false);
        //startBtn.setEnabled(false);
        //stopBtn.setEnabled(false);
        if (!initCamera())
            finish();
    }

    @SuppressWarnings("deprecation")
    private boolean initCamera() {
        try {
            if(front) {
                camera = getFrontFacingCamera();
            } else {
                camera = getRearFacingCamera();
                Log.v("flip","gotRearFAcingCamera");
            }
            camera.setDisplayOrientation(90);
            Camera.Parameters camParams = camera.getParameters();
            Log.v("tag","testing");
            Size size = getBestPreviewSize(450,450, camParams); //my line
            camera.lock();
            holder = videoView.getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //maybe check types
        }
        catch(RuntimeException re) {
            Log.v("tag","can't init");
            re.printStackTrace();
            return false;
        }
        return true;
    }

    Camera getFrontFacingCamera() throws NoSuchElementException {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraIndex = 0; cameraIndex < Camera.getNumberOfCameras(); cameraIndex++) {
            Camera.getCameraInfo(cameraIndex, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    return Camera.open(cameraIndex);
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new NoSuchElementException("no front camera");
    }
    Camera getRearFacingCamera() throws NoSuchElementException {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraIndex = 0; cameraIndex < Camera.getNumberOfCameras(); cameraIndex++) {
            Camera.getCameraInfo(cameraIndex, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    return Camera.open(cameraIndex);
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new NoSuchElementException("no front camera");
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        // TODO Auto-generated method stub

    }

    //http://stackoverflow.com/questions/16785086/mediarecorder-setvideosize-fails-with-maximum-resolution
    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Log.v("tag","I'm in");
        Camera.Size result=null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width<=width && size.height<=height) {
                if (result==null) {
                    result=size;
                } else {
                    int resultArea=result.width*result.height;
                    int newArea=size.width*size.height;
                    Log.v("tag",size.width+"and" +size.height); //my line

                    if (newArea>resultArea) {
                        result=size;
                    }
                }
            }
        }
        return(result);
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
        if(!didSomething) { //this implies nothing was chosen yet so erase the text now
            noCategory.setText("");
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

    public void register() {
        //in reality this should be in response to a button or something
        Thread t = new Thread(new Runnable() {
            public void run() {
                Log.v("PostActivity","trying register");
                Log.v("PostActivity",fbid);
                try {
                    Log.v("PostActivity",MainActivity.phpUrl+"registerVid.php?UIDr="+fbid+"&UIDp="+fbid);
                    String data = MainActivity.getData(MainActivity.phpUrl+"registerVid.php?UIDr="+fbid+"&UIDp="+fbid);
                    data = data.substring(data.indexOf(":")+2);
                    outputFileName=Environment.getExternalStorageDirectory()+"/Bafit/vids/"+data.substring(0,data.indexOf("\""))+".mp4";
                    outputFile = data.substring(0,data.indexOf("\""));
                    Log.v("PostActivity",outputFileName);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
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

    //http://www.e-nature.ch/tech/saving-loading-bitmaps-to-the-android-device-storage-internal-external/
    public boolean saveImageToExternalStorage(Bitmap image) {
        String fullPath = Environment.getExternalStorageDirectory()+"/Bafit/vids/";
        try {
            File dir = new File(fullPath);
            if(!dir.exists()) {
                dir.mkdirs();
            }
            OutputStream fOut = null;
            File file = new File(fullPath,MainActivity.replaceString(outputFileName.substring(outputFileName.indexOf("/vids/") + 5), "mp4", "jpg"));
            file.createNewFile();
            fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG,100,fOut);
            fOut.flush();
            fOut.close();
            MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
            Log.v("PostActivity","Saved Image");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("PostActivity","Failed to Save");
            return false;
        }
    }




}
