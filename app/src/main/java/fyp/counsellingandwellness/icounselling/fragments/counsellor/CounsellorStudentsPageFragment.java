package fyp.counsellingandwellness.icounselling.fragments.counsellor;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
public class CounsellorStudentsPageFragment extends Fragment {


    private ListView usersList;
    private TextView noUsersText;
    private ArrayList<String> al = new ArrayList<>();
    private int totalUsers = 0;
    private ProgressDialog pd;
    private String userId;

    private String chatWithId;

    //private ArrayList<String> userIds = new ArrayList<String>();


    public CounsellorStudentsPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_counsellor_students_page,
                container, false);

        usersList = (ListView)view.findViewById(R.id.usersList);
        noUsersText = (TextView)view.findViewById(R.id.noUsersText);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();

        userId =  FirebaseAuth.getInstance().getUid();

        String url = "https://icounselling-fyp.firebaseio.com/chat/users/"+userId+".json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef = myRef.child("User").child(userId);

                ValueEventListener userListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                        UserDetails.username = userInfo.getName();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value

                    }
                };
                myRef.addValueEventListener(userListener);

                UserDetails.chatWith = al.get(position);

                myRef = database.getReference("chat/users/"+userId+"/"+UserDetails.chatWith+"/UID");
                        //myRef.child("chat").child("users").child(userId).child(UserDetails.chatWith).child("UID");
                ValueEventListener userListener1 = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       // for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            chatWithId = dataSnapshot.getValue(String.class);

                        Intent intent = new Intent(getActivity().getApplicationContext(), SessionActvity.class);
                        intent.putExtra("chatWithId",chatWithId);
                        intent.putExtra("role","counsellor");
                        startActivity(intent);
                       // }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value

                    }
                };
                myRef.addValueEventListener(userListener1);
            }
        });
        return view;
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

               // if(!key.equals(UserDetails.username)) {
                    al.add(key);
               // }

                totalUsers++;
                int test  = totalUsers;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers < 1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, al));
        }
        pd.dismiss();
    }
}