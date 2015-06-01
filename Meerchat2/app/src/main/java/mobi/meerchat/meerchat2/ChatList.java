package mobi.meerchat.meerchat2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class ChatList extends Activity{

    ListView chatListView;
    static ArrayList<String> chatList;
    ArrayAdapter<String> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.myRLL);

        RelativeLayout.LayoutParams params;

        chatListView = new ListView(this);
        params = new RelativeLayout.LayoutParams(width,height*9/10);
        params.leftMargin=0;
        params.topMargin=height*1/5;
        rl.addView(chatListView,params);


        chatList = new ArrayList<String>();
        chatList.add("condo"); //currently hardcoded, will go soft later
        aa = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,chatList);
        chatListView.setAdapter(aa);

        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.v("click","im at" + position);
                Intent chatIntent = new Intent(getApplicationContext(),Chat.class);
                chatIntent.putExtra("BUDDYNAME",chatList.get(position));
                startActivity(chatIntent);
            }
        });

    }


    /**
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_list, menu);
        return true;
    }
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     @Override
     public void onClick(View v) {
     //trying to handle clicking on list
     chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
     public void onItemClick(AdapterView<?> parent, View view,
     int position, long id) {

     Log.v("click","im at" + position);
     }
     });
     }
     */

}
