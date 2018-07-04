package fyp.counsellingandwellness.icounselling.fragments.student;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import fyp.counsellingandwellness.icounselling.R;
import fyp.counsellingandwellness.icounselling.UserDetails;
import fyp.counsellingandwellness.icounselling.UserInfo;
import fyp.counsellingandwellness.icounselling.activities.SessionActvity;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentSessionPageFragment extends Fragment {

    private ListView usersList;
    private TextView noUsersText;
    private ArrayList<String> al = new ArrayList<>();
    private int totalUsers = 0;
    private ProgressDialog pd;
    private String userId;
    private String chatWithId;

    public StudentSessionPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_session_page,
                container, false);

        usersList = (ListView)view.findViewById(R.id.usersList);
        noUsersText = (TextView)view.findViewById(R.id.noUsersText);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();

        userId = FirebaseAuth.getInstance().getUid();
        String url = "https://icounselling-fyp.firebaseio.com/chat/users/"+userId+".json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);

                UserDetails.chatWith = al.get(0);
                Intent intent = new Intent(getActivity().getApplicationContext(), SessionActvity.class);
                intent.putExtra("chatWithId",chatWithId);
                intent.putExtra("role","student");
                startActivity(intent);

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef = myRef.child("User").child(userId);


       //

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                UserDetails.username = userInfo.getName();
                chatWithId = userInfo.getCounsellor();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        };
        myRef.addValueEventListener(userListener);

        return view;
    } //end of oncreate

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(UserDetails.username)) {
                    al.add(key);

                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        pd.dismiss();
    }

}
