package mobi.meerchat.meerchat2;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by ksadman on 11/6/2014.
 */
public class MyLovelyOnClickListener implements View.OnClickListener {
    int myLovelyVariable;
    ArrayList<String> myVuriList;
    public MyLovelyOnClickListener(int LovelyVariable, ArrayList<String> vuriList) {
        //Log.v("Flipping",vuriList.get(vuriList.size()));
        this.myLovelyVariable = LovelyVariable;
        this.myVuriList = vuriList;
    }

    @Override
    public void onClick(View V) {

    }
}
