package mobi.meerchat.meerchat2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ksadman on 3/22/2015.
 */
public class outputThread extends Thread{
    private String contents;

    public outputThread() {

    }

    public static String replaceString(String bigString, String old, String newS) {
        while(bigString.indexOf(old)>-1) {
            bigString = bigString.substring(0,bigString.indexOf(old)) + newS + bigString.substring(bigString.indexOf(old)+old.length());
        }
        return bigString;
    }

    public void run() {
        try
        {
            final String urlAddress = "http://meerchat.mobi/PrivacyPolicy_Terms/";
            final URL page = new URL(urlAddress);
            URLConnection yc = page.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while((inputLine=in.readLine())!=null)
            {
                contents+=inputLine;
            }
            in.close();
            Log.v("Terms&Conditions", contents);
            //the rest of processing in TC.java

            //final String contents2 = contents;
            Log.v("Terms&Conditions","done");
        }

        catch(Exception e)
        {
            e.printStackTrace();
            Log.v("Terms&Conditions","broken");
        }
    }

    public String getContents() {
        return contents;
    }
}
