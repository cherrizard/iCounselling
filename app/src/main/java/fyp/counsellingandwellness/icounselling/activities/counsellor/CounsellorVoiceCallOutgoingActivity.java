package fyp.counsellingandwellness.icounselling.activities.counsellor;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserDetails;

public class CounsellorVoiceCallOutgoingActivity extends AppCompatActivity {

    SinchClient sinchClient;
    private Call call;
    private Button button_call;
    private TextView callState;
    private String recipientId;
    private Call calling;
    private String userId;
    private Firebase reference1,reference2;
    private TextView studentName;

    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_voice_call_outgoing);

        Intent intent = getIntent();
        //callerId = intent.getStringExtra("callerId");

        recipientId = intent.getStringExtra("recipientId");

        userId = FirebaseAuth.getInstance().getUid();

        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(userId)
                .applicationKey("04fa0e0c-1123-497e-b4b2-c118159223c6")
                .applicationSecret("+njXuYsMckaGRagWaJoM2w==")
                .environmentHost("sandbox.sinch.com")
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        button_call = (Button) findViewById(R.id.button_call);
        callState = (TextView) findViewById(R.id.callState);
        studentName = (TextView) findViewById(R.id.studentName);
        studentName.setText(UserDetails.chatWith);

        button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (call == null) {
                    call = sinchClient.getCallClient().callUser(recipientId);
                    call.addCallListener(new SinchCallListener());
                    button_call.setText("Hang Up");
                } else {

                    call.hangup();

                }
            }
        });

        Firebase.setAndroidContext(this);

        reference1 = new Firebase("https://icounselling-fyp.firebaseio.com/chat/message/" + userId + "_" + recipientId);
        reference2 = new Firebase("https://icounselling-fyp.firebaseio.com/chat/message/" + recipientId + "_" + userId);


    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(com.sinch.android.rtc.calling.Call endedCall) {
            //call ended by either party
            call = null;
            button_call.setText("Start Voice Call");
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            callState.setText("Call Ended");

            Map<String, String> map = new HashMap<String, String>();
            map.put("message", "Voice Call Duration:" +String.valueOf(endedCall.getDetails().getDuration()) + "s" );
            map.put("user", UserDetails.username);
            reference1.push().setValue(map);
            reference2.push().setValue(map);
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

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, com.sinch.android.rtc.calling.Call incomingCall) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(CounsellorVoiceCallOutgoingActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.activity_student_voice_call_incoming, null);

            Button acceptb = (Button) mView.findViewById(R.id.button_acceptCall);
            Button rejectb = (Button) mView.findViewById(R.id.button_rejectCall);
            calling = incomingCall;

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            acceptb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    call = calling;
                    call.answer();
                    call.addCallListener(new SinchCallListener());
                    button_call.setText("Hang Up");
                }
            });
            rejectb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    call = calling;
                    call.hangup();
                    dialog.dismiss();
                }
            });
        }
    }
}
