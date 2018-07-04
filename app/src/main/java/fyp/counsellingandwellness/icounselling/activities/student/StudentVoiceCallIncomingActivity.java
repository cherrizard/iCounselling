package fyp.counsellingandwellness.icounselling.activities.student;

import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserInfo;
import fyp.counsellingandwellness.icounselling.VideoCallAudioPlayer;
import fyp.counsellingandwellness.icounselling.BaseActivity;
import fyp.counsellingandwellness.icounselling.SinchService;

public class StudentVoiceCallIncomingActivity extends BaseActivity {

    private Button button_acceptCall;
    private Button button_rejectCall;
    private VideoCallAudioPlayer mVideoCallAudioPlayer;
    private String mCallID;
    private Call calling;
    private String userID;
    private String chatWithID;
    private Firebase reference1,reference2;
    private String userName;
    private TextView csName;
    private TextView callState;
    SinchClient sinchClient;
    Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_voice_call_incoming);

        button_acceptCall = (Button) findViewById(R.id.button_acceptCall);
        button_acceptCall.setOnClickListener(mClickListener);
        button_rejectCall = (Button) findViewById(R.id.button_rejectCall);
        button_rejectCall.setOnClickListener(mClickListener);
        csName = findViewById(R.id.callerName);
        callState = findViewById(R.id.callState);


        mVideoCallAudioPlayer = new VideoCallAudioPlayer(this);
        mVideoCallAudioPlayer.playRingtone();
        mCallID = getIntent().getStringExtra(SinchService.CALL_ID);
        chatWithID = getIntent().getStringExtra("counsellorId");
        userName = getIntent().getStringExtra("userName");

        userID = FirebaseAuth.getInstance().getUid();

        Firebase.setAndroidContext(this);

        reference1 = new Firebase("https://icounselling-fyp.firebaseio.com/chat/message/" + userID + "_" + chatWithID);
        reference2 = new Firebase("https://icounselling-fyp.firebaseio.com/chat/message/" + chatWithID + "_" + userID);



        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDataRef = mDatabase.getReference();
        mDataRef = mDataRef.child("User").child(chatWithID);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
               String counsellorName = userInfo.getName();
                csName.setText(counsellorName);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDataRef.addValueEventListener(userListener);

    }


    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallID);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            callState.setText("Ringing");
        } else {
            finish();
        }
    }

    private void answerClicked() {
        mVideoCallAudioPlayer.stopRingtone();
        call = getSinchServiceInterface().getCall(mCallID);
        if (calling == null) {
            calling = call;
            call.answer();
            call.addCallListener(new SinchCallListener());
            button_acceptCall.setText("Hang Up");
            button_rejectCall.setVisibility (View.GONE);
        }
        else {
            call.hangup();
            finish();
        }
    }

    private void declineClicked() {
        mVideoCallAudioPlayer.stopRingtone();
        call = getSinchServiceInterface().getCall(mCallID);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    public class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(com.sinch.android.rtc.calling.Call endedCall) {
            mVideoCallAudioPlayer.stopProgressTone();
            mVideoCallAudioPlayer.stopRingtone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            callState.setText("Call Ended");
            finish();
        }

        @Override
        public void onCallEstablished(com.sinch.android.rtc.calling.Call establishedCall) {
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            callState.setText("Connected");
        }

        @Override
        public void onCallProgressing(com.sinch.android.rtc.calling.Call progressingCall) {
            //call is ringing
            callState.setText("Ringing");
        }

        @Override
        public void onShouldSendPushNotification(com.sinch.android.rtc.calling.Call call, List<PushPair> pushPairs) {
            //don't worry about this right now
        }
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_acceptCall:
                    answerClicked();
                    break;
                case R.id.button_rejectCall:
                    declineClicked();
                    break;
            }
        }
    };
}
