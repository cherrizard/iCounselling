package fyp.counsellingandwellness.icounselling.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserDetails;
import fyp.counsellingandwellness.icounselling.activities.counsellor.CounsellorNavBarActivity;
import fyp.counsellingandwellness.icounselling.activities.student.StudentMorePageMyCounsellor;
import fyp.counsellingandwellness.icounselling.activities.student.StudentNavBarActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class SessionActvity extends AppCompatActivity {
    private LinearLayout layout;
    private RelativeLayout layout_2;
    private ImageView sendButton;
    private EditText messageArea;
    private ScrollView scrollView;
    private Firebase reference1, reference2;
    private TextView displayName;
    private String role;
    private RelativeLayout chatWith;
    private Button button_clearHistory;

    String currentUserId, chatWithId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        layout = findViewById(R.id.layout1);
        layout_2 = findViewById(R.id.layout2);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);
        displayName =  findViewById(R.id.name);
        chatWith = findViewById(R.id.chatWith);
        button_clearHistory = findViewById(R.id.clearChat);


        currentUserId = FirebaseAuth.getInstance().getUid();
        chatWithId = getIntent().getStringExtra("chatWithId");
        role = getIntent().getStringExtra("role");
        displayName.setText(UserDetails.chatWith);

        if(role.equals("counsellor"))
        {
            chatWith.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SessionActvity.this, SessionViewStudentProfileActivity.class);
                    intent.putExtra("studentId",chatWithId);
                    startActivity(intent);

                }
            });
        }
        else if(role.equals("student"))
        {
            chatWith.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SessionActvity.this, StudentMorePageMyCounsellor.class);
                    intent.putExtra("counsellorId",chatWithId);
                   startActivity(intent);

                }
            });
        }

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://icounselling-fyp.firebaseio.com/chat/message/" + currentUserId + "_" + chatWithId);
        reference2 = new Firebase("https://icounselling-fyp.firebaseio.com/chat/message/" + chatWithId + "_" + currentUserId);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat df = new SimpleDateFormat("HH:mm");
                Calendar calobj = Calendar.getInstance();

                String messageText = messageArea.getText().toString();
                if(!messageText.equals("")){
                     messageText = messageArea.getText().toString() +"\n" + df.format(calobj.getTime());
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText );
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });
        if(role.equals("student"))
        {
            button_clearHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SessionActvity.this);
                    alertDialog.setMessage("Are you sure? This cannot be undone")
                            .setPositiveButton("Yeap, burn 'em all", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(SessionActvity.this, "May there be no regrets", Toast.LENGTH_SHORT).show();
                                    reference1.removeValue();
                                    reference2.removeValue();
                                    finish();
                                    startActivity(getIntent());
                                }
                            })
                            .setNegativeButton("Nope, I'm having second thoughts", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(SessionActvity.this, "*Splashing water all over*", Toast.LENGTH_SHORT).show();
                                }
                            });
                    alertDialog.show();
                }
            });
        }
        else if (role.equals("counsellor"))
        {
            View b = findViewById(R.id.clearChat);
            b.setVisibility(View.GONE);
        }

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UserDetails.username)){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    addMessageBox(UserDetails.chatWith + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(SessionActvity.this);
        textView.setText(message +"\n");

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.chatout);
        }
        else{
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.chatin);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public void onBackPressed() {

        if(role.equals("student"))
        {
            Intent intent = new Intent(SessionActvity.this, StudentNavBarActivity.class);
            startActivity(intent);
        }
        else if(role.equals("counsellor"))
        {
            Intent intent = new Intent(SessionActvity.this, CounsellorNavBarActivity.class);
            startActivity(intent);
        }

    }

}