package mobi.meerchat.meerchat2;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class Chat extends Activity {

    public static final String HOST = "meerchat.mobi";
    public static final int PORT = 5222;
    public static final String SERVICE = "meerchat.mobi";
    public static final String USERNAME="condo";
    public static final String PASSWORD = "condo";
    private XMPPConnection connection;
    private ArrayList<String> messages = new ArrayList<String>();
    private Handler mHandler = new Handler();
    private EditText recipient;
    private EditText textMessage;
    private ListView listview;
    String buddyName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recipient = (EditText) this.findViewById(R.id.toET);
        textMessage = (EditText) this.findViewById(R.id.chatET);
        listview = (ListView) this.findViewById(R.id.listMessages);
        setListAdapter();

        buddyName = getIntent().getExtras().getString("BUDDYNAME");
        Log.v("chat",buddyName);

        Button send = (Button) this.findViewById(R.id.sendBtn);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //String to = recipient.getText().toString();
                String to = buddyName+"@meerchat.mobi";
                String text = textMessage.getText().toString();
                Log.v("chat","sending");
                Message msg = new Message(to,Message.Type.chat);
                msg.setBody(text);
                if (connection !=null) {
                    try {
                        connection.sendPacket(msg);
                        messages.add(connection.getUser() + ":");
                        messages.add(text);
                        setListAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //File Transfer
                //http://try-code.org/merlene/2015/04/22/android-sending-files-with-asmack-or-any-alternatives/
                //https://community.igniterealtime.org/thread/52015

                try {
                    FileTransferManager manager = new FileTransferManager(connection);
                    OutgoingFileTransfer send = manager.createOutgoingFileTransfer(connection.getRoster().getPresence(to).getFrom());
                    String fname = "1.mp4";
                    send.sendFile(new File(fname), "File send");
                } catch(Exception e) {
                    e.printStackTrace();
                }

            }
        });
        connect();
    }

    public void setConnection(XMPPConnection connection) {
        this.connection = connection;
        if (connection !=null) {
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
            connection.addPacketListener(new PacketListener() {
                @Override
                public void processPacket(Packet packet) {
                    Message message = (Message) packet;
                    if (message.getBody() !=null) {
                        String fromName = StringUtils.parseBareAddress(message.getFrom());
                        Log.v("chat","Received "+ message.getBody() + " from" +fromName);
                        messages.add(fromName+":");
                        messages.add(message.getBody());
                        mHandler.post(new Runnable() {
                            public void run() {
                                setListAdapter();
                            }
                        });
                    }
                }
            }, filter);
        }
    }

    private void setListAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.listitem,messages);
        listview.setAdapter(adapter); //listviewr?
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace(); //always do this i think
        }
    }

    public void connect() {
        final ProgressDialog dialog = ProgressDialog.show(this,"Connecting","please wait",false);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST,PORT,SERVICE);
                XMPPConnection connection = new XMPPTCPConnection(connConfig); //was xmppconnection but maybe this is still ok
                try {
                    connection.connect();
                    Log.v("chat","connected");
                } catch (Exception ex) { //catch all, not just xmpp
                    ex.printStackTrace(); //always
                    setConnection(null);
                }
                try {
                    connection.login(USERNAME,PASSWORD);
                    Log.v("chat","logged in");
                    Presence presence = new Presence(Presence.Type.available);
                    connection.sendPacket(presence);
                    setConnection(connection);
                    Roster roster = connection.getRoster();
                    Collection<RosterEntry> entries = roster.getEntries();
                    for (RosterEntry entry : entries) {
                        Presence entryPresence = roster.getPresence(entry.getUser());
                        Presence.Type type = entryPresence.getType();
                        if(type == Presence.Type.available) {
                            Log.v("chat","avail");
                        }
                    }
                } catch (Exception ex) { //catch all exceptions, not just xmpp
                    ex.printStackTrace();
                    setConnection(null);
                }
                dialog.dismiss();
            }
        });
        t.start();
        dialog.show();
    }

}
