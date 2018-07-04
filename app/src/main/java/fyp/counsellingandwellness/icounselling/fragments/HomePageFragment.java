package fyp.counsellingandwellness.icounselling.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fyp.counsellingandwellness.icounselling.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment  {

    ImageView eventsAndWorkshops;
    ImageView connectionMagazine ;
    ImageView recommendedMentalHealthApps ;
    ImageView peerCounsellingVolunteer ;

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventsAndWorkshops = (ImageView)getView().findViewById(R.id.eventsAndWorkshops);
        connectionMagazine = (ImageView)getView().findViewById(R.id.connectionMagazine);
        recommendedMentalHealthApps = (ImageView)getView().findViewById(R.id.recommendedMentalHealthApps);
        peerCounsellingVolunteer = (ImageView)getView().findViewById(R.id.peerCounsellingVolunteer);

        eventsAndWorkshops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.facebook.com/sunwayeducationcounsellingandwellness/"));
                getActivity().startActivity(i);
            }
        });

        connectionMagazine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://student.sunway.edu.my/connections"));
                getActivity().startActivity(i);
            }
        });

        recommendedMentalHealthApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://studentlife.ryerson.ca/health-and-wellness/5-mental-health-apps-for-students/"));
                getActivity().startActivity(i);
            }
        });

        peerCounsellingVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.facebook.com/sunwaypcv/"));
                getActivity().startActivity(i);
            }
        });
    }
}