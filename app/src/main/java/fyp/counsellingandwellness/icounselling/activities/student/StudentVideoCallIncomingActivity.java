package fyp.counsellingandwellness.icounselling.activities.student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;

import java.util.List;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserInfo;
import fyp.counsellingandwellness.icounselling.VideoCallAudioPlayer;
import fyp.counsellingandwellness.icounselling.BaseActivity;
import fyp.counsellingandwellness.icounselling.SinchService;
import fyp.counsellingandwellness.icounselling.activities.SessionVideoCallScreenActivity;

public class StudentVideoCallIncomingActivity extends BaseActivity {

    static final String TAG = StudentVideoCallIncomingActivity.class.getSimpleName();
    private String mCallId;
    private VideoCallAudioPlayer mVideoCallAudioPlayer;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;
    private UserInfo userInfo;
    private String userId;
    private String chatWithName;
    private String csId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_video_call_incoming);

        Button answer = findViewById(R.id.answerButton);
        answer.setOnClickListener(mClickListener);
        Button decline = findViewById(R.id.rejectButton);
        decline.setOnClickListener(mClickListener);
        userId = FirebaseAuth.getInstance().getUid();

        mVideoCallAudioPlayer = new VideoCallAudioPlayer(this);
        mVideoCallAudioPlayer.playRingtone();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
        csId = getIntent().getStringExtra("counsellorId");

        mDatabase = FirebaseDatabase.getInstance();
        mDataRef = mDatabase.getReference();
        mDataRef = mDataRef.child("User").child(csId);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(UserInfo.class);
                chatWithName = userInfo.getName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDataRef.addValueEventListener(userListener);

    }

    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            TextView remoteUser = (TextView) findViewById(R.id.remoteUser);
            remoteUser.setText(chatWithName);
        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }


    private void answerClicked() {
        mVideoCallAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, SessionVideoCallScreenActivity.class);
            intent.putExtra(SinchService.CALL_ID, mCallId);
            intent.putExtra("chatWithName", chatWithName);
            intent.putExtra("chatWithId", csId);
            intent.putExtra("role", "student");
            startActivity(intent);
        } else {
            finish();
        }
    }

    private void declineClicked() {
        mVideoCallAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
          //  CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended");
            mVideoCallAudioPlayer.stopRingtone();
            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }

        @Override
        public void onVideoTrackAdded(Call call) {
            // Display some kind of icon showing it's a video call
        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }

        @Override
        public void onVideoTrackResumed(Call call) {

        }

    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.answerButton:
                    answerClicked();
                    break;
                case R.id.rejectButton:
                    declineClicked();
                    break;
            }
        }
    };
}
