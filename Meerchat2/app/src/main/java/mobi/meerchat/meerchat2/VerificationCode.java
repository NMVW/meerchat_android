package mobi.meerchat.meerchat2;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class VerificationCode extends Activity {
    EditText verifyBox;
    Bundle extras;
    String fbid = "";
    String fbemail = "";
    String bafemail = "";
    String bun = "";
    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            extras = getIntent().getExtras();
            if(extras==null) {
                fbid=null;
                fbemail=null;
                bafemail = null;
                bun = null;
            } else {
                fbid=extras.getString("FBid");
                fbemail = extras.getString("FBemail");
                bafemail = extras.getString("BAFemail");
                bun = extras.getString("BUN");
            }
        } else {
            fbid = (String)savedInstanceState.getSerializable("FBid");
            fbemail = (String)savedInstanceState.getSerializable("FBemail");
            bafemail = (String)savedInstanceState.getSerializable("BAFemail");
            bun = (String)savedInstanceState.getSerializable("BUN");
        }
        setContentView(R.layout.activity_verification_code);
        verifyBox= (EditText)findViewById(R.id.vcode);

        Log.v("VerificationCode","trying");
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Log.v("VerificationCode","getting location");
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //maybe GPS_PROVIDER
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        Log.v("VerificationCode",longitude+","+latitude);
        MyLL ll = new MyLL();
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,ll);


        //double longitude = location.getLongitude();
        //double latitude = location.getLatitude();
        //Log.v("VerificationCode", longitude + "," + latitude);
    }

    private class MyLL implements LocationListener{
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

    public void verify(View view) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.v("VerificationCode",MainActivity.phpUrl+"createUser.php?UIDr="+fbid+"&BUN="+bun+"&BAFemail="+bafemail+"&RVC="+verifyBox.getText().toString()+"&FBemail="+fbemail+"&FBid="+fbid+"&GPSlat="+latitude+"&GPSlon="+longitude);
                    String data = MainActivity.getData(MainActivity.phpUrl+"createUser.php?UIDr="+fbid+"&BUN="+bun+"&BAFemail="+bafemail+"&RVC="+verifyBox.getText().toString()+"&FBemail="+fbemail+"&FBid="+fbid+"&GPSlat="+latitude+"&GPSlon="+longitude);
                    Log.v("VerificationCode",data);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_verification_code, menu);
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
