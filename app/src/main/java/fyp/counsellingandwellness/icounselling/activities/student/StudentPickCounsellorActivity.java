package fyp.counsellingandwellness.icounselling.activities.student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import fyp.counsellingandwellness.icounselling.R;

public class StudentPickCounsellorActivity extends AppCompatActivity {

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_pick_counsellor);

        userId =  FirebaseAuth.getInstance().getUid();

        (findViewById(R.id.tanahkhim)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView counName = findViewById(R.id.name_tanahkhim);
                // Launching new Activity on hitting the image
                Intent pick = new Intent(StudentPickCounsellorActivity.this, StudentPickCounsellorProfileActivity.class);
                String name_tanahkhim = counName.getText().toString();
                pick.putExtra("counsellorName", name_tanahkhim);
                startActivity(pick);
            }
        });

        (findViewById(R.id.leekokhan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView counName = findViewById(R.id.name_leekokhan);
                // Launching new Activity on hitting the image
                Intent pick = new Intent(StudentPickCounsellorActivity.this, StudentPickCounsellorProfileActivity.class);
                String name_leekokhan = counName.getText().toString();
                pick.putExtra("counsellorName", name_leekokhan);
                startActivity(pick);
            }
        });

        (findViewById(R.id.tehjunyi)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView counName = findViewById(R.id.name_tehjunyi);
                // Launching new Activity on hitting the image
                Intent pick = new Intent(StudentPickCounsellorActivity.this, StudentPickCounsellorProfileActivity.class);
                String name_tehjunyi = counName.getText().toString();
                pick.putExtra("counsellorName", name_tehjunyi);
                startActivity(pick);
            }
        });

        (findViewById(R.id.tangjoynn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView counName = findViewById(R.id.name_tangjoynn);
                // Launching new Activity on hitting the image
                Intent pick = new Intent(StudentPickCounsellorActivity.this, StudentPickCounsellorProfileActivity.class);
                String name_tangjoynn = counName.getText().toString();
                pick.putExtra("counsellorName", name_tangjoynn);
                startActivity(pick);
            }
        });

        (findViewById(R.id.chaipeiling)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView counName = findViewById(R.id.name_chaipeiling);
                // Launching new Activity on hitting the image
                Intent pick = new Intent(StudentPickCounsellorActivity.this, StudentPickCounsellorProfileActivity.class);
                String name_chaipeiling = counName.getText().toString();
                pick.putExtra("counsellorName", name_chaipeiling);
                startActivity(pick);
            }
        });

        (findViewById(R.id.engqianjun)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView counName = findViewById(R.id.name_engqianjun);
                // Launching new Activity on hitting the image
                Intent pick = new Intent(StudentPickCounsellorActivity.this, StudentPickCounsellorProfileActivity.class);
                String name_engqianjun = counName.getText().toString();
                pick.putExtra("counsellorName", name_engqianjun);
                startActivity(pick);
            }
        });

        (findViewById(R.id.wongkahyee)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView counName = findViewById(R.id.name_wongkahyee);
                // Launching new Activity on hitting the image
                Intent pick = new Intent(StudentPickCounsellorActivity.this, StudentPickCounsellorProfileActivity.class);
                String name_wongkahyee = counName.getText().toString();
                pick.putExtra("counsellorName", name_wongkahyee);
                startActivity(pick);
            }
        });

        /*
        (findViewById(R.id.wongkhakyt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView counName = findViewById(R.id.name_wongkhakyt);
                // Launching new Activity on hitting the image
                Intent pick = new Intent(StudentPickCounsellorActivity.this, StudentPickCounsellorProfileActivity.class);
                String name_wongkhakyt = counName.getText().toString();
                pick.putExtra("counsellorName", name_wongkhakyt);
                startActivity(pick);
            }
        });
        */

    }
}
