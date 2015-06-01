package mobi.meerchat.meerchat2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.List;


public class Feedback extends Activity {
    Spinner issues;
    EditText details;
    EditText addFeedback;
    Switch rec;
    String recValue="no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);
        //RelativeLayout rl = (RelativeLayout) findViewById(R.id.myRLL);
        TableLayout tl = (TableLayout) findViewById(R.id.myRLL);
        Display display = getWindowManager().getDefaultDisplay();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        Button btnCancel = new Button(this);
        btnCancel.setText("Cancel");
        btnCancel.setTextSize(14);
        btnCancel.getBackground().setColorFilter(Color.rgb(236, 156, 19), PorterDuff.Mode.MULTIPLY);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                Intent cancelIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(cancelIntent);
            }
        });
        btnCancel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(btnCancel);
        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width/4,width/10);
        //params.leftMargin=0;
        //params.topMargin=height/20;
        //rl.addView(btnCancel,params);

        Button btnSubmit = new Button(this);
        btnSubmit.setText("Submit");
        btnSubmit.setTextSize(14);
        btnSubmit.getBackground().setColorFilter(Color.rgb(236, 156, 19), PorterDuff.Mode.MULTIPLY);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {

                Intent submitIntent = new Intent(getApplicationContext(), MainActivity.class); //Change to database webservice later

                Thread t = new Thread(new Runnable() {
                    public void run() {
                        try {
                            String data = getData("http://bafit.mobi/cScripts/v1/sendFeedback.php?issue="+issues.getSelectedItem().toString()+"&issueDetail="+details.getText().toString()+"&contFeedback="+addFeedback.getText().toString()+"&recommend="+recValue+"&UID="+"123456"+"&BUN="+"abcdef"+"&lat=50.52&lon=52.50");
                            Log.i("feedback",data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();


                Log.i("feedback", "send");
                Log.i("feedback","http://bafit.mobi/cScripts/v1/sendFeedback.php?issue="+issues.getSelectedItem().toString()+"&issueDetail="+details.getText().toString()+"&contFeedback="+addFeedback.getText().toString()+"&recommend="+recValue+"&UID="+"123456"+"&BUN="+"abcdef"+"&lat=50.52&lon=52.50");
                startActivity(submitIntent);
            }
        });
        btnSubmit.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(btnSubmit);
        //params = new RelativeLayout.LayoutParams(width/4,width/10);
        //btnAll.setBackgroundColor(80000000);
        //params.leftMargin = width*3/4;
        //params.topMargin=height/20;
        //rl.addView(btnSubmit,params);

        TextView textTitle = new TextView(this);
        textTitle.setText("Feedback");
        textTitle.setTextSize(20);
        textTitle.setGravity(Gravity.CENTER);
        textTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(textTitle);
        //params = new RelativeLayout.LayoutParams(width/2,width/10);
        //params.leftMargin=width/4;
        //params.topMargin=height/20;
        //rl.addView(textTitle, params);
        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow trSpinner = new TableRow(this);
        trSpinner.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView textSpinner = new TextView(this);
        textSpinner.setText("Issues Experienced");
        textSpinner.setTextSize(20);
        textSpinner.setGravity(Gravity.CENTER);
        textSpinner.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        trSpinner.addView(textSpinner);
        //params = new RelativeLayout.LayoutParams(width/2,height/10);
        //params.leftMargin=0;
        //params.topMargin=width*4/20;
        //rl.addView(textSpinner, params);

        String [] issueChoices = {"Interface", "Data Loss", "Video Playback", "Messaging", "Location Based", "Other", "None"};
        issues = new Spinner(this);
        ArrayAdapter spinAdapater = new ArrayAdapter(this,android.R.layout.simple_spinner_item,issueChoices);
        issues.setAdapter(spinAdapater);
        issues.setSelection(6);
        issues.setPrompt("Please Select");
        issues.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        trSpinner.addView(issues);
        //params = new RelativeLayout.LayoutParams(width/2,height/10);
        //params.addRule(RelativeLayout.ALIGN_BOTTOM,btnCancel.getId());
        //params.leftMargin=width/2;
        //params.topMargin=btnCancel.getHeight();
        //int[] coords = {0,0};
        //params.topMargin=width/5;
        //rl.addView(issues,params);
        tl.addView(trSpinner, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow trDetails = new TableRow(this);
        trDetails.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        details = new EditText(this);
        details.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v,boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        details.setHint("Further Details..");

        details.setTextSize(14);
        details.setGravity(Gravity.TOP);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.span = 2;
        details.setLayoutParams(params);
        trDetails.addView(details);
        //params = new RelativeLayout.LayoutParams(width,height/4);
        //params.leftMargin=0;
        //params.topMargin=width/4+height/10;
        //rl.addView(details, params);
        tl.addView(trDetails, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


        TableRow trAddFB = new TableRow(this);
        trAddFB.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        addFeedback = new EditText(this);
        addFeedback.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        addFeedback.setHint("Additional Feedback...");
        addFeedback.setTextSize(14);
        addFeedback.setGravity(Gravity.TOP);
        //addFeedback.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.span = 2;
        addFeedback.setLayoutParams(params);
        trAddFB.addView(addFeedback);
        //arams = new RelativeLayout.LayoutParams(width,height/4);
        //params.leftMargin=0;
        //params.topMargin=width/4+height/10+height/4;
        //rl.addView(addFeedback, params);
        tl.addView(trAddFB, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow trRec = new TableRow(this);
        trRec.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView textRec = new TextView(this);
        textRec.setText("Would you recommend this app?");
        textRec.setTextSize(14);
        textRec.setGravity(Gravity.CENTER);
        textRec.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        trRec.addView(textRec);
        //params = new RelativeLayout.LayoutParams(width*3/4,height/10);
        //params.leftMargin=0;
        //params.topMargin=width/4+height*3/5;
        //rl.addView(textRec, params);

        rec = new Switch(this);
        rec.setTextOn("YES");
        rec.setTextOff("NO");
        rec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recValue="yes";
                } else {
                    recValue="no";
                }
            }
        });


        rec.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        trRec.addView(rec);
        //params = new RelativeLayout.LayoutParams(width/4,height/10);
        //params.leftMargin=width*3/4;
        //params.topMargin=width/4+height*3/5;
        //rl.addView(rec, params);
        tl.addView(trRec, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
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

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

}
