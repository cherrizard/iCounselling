package fyp.counsellingandwellness.icounselling;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import fyp.counsellingandwellness.icounselling.activities.student.StudentAppointmentsPageMakeAnAppointmentActivity;
import fyp.counsellingandwellness.icounselling.activities.student.StudentNavBarActivity;
import fyp.counsellingandwellness.icounselling.fragments.student.StudentAppointmentsPageFragment;

public class StudentAppointmentAdapter extends RecyclerView.Adapter<StudentAppointmentAdapter.StudentViewHolder> {

    private Context mContext;
    private List<AppointmentCard> appointmentList;

    public class StudentViewHolder extends RecyclerView.ViewHolder{

        public TextView date, time, type, status;

        public StudentViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            time = (TextView) view.findViewById(R.id.time);
            type = (TextView) view.findViewById(R.id.type);
            status = (TextView) view.findViewById(R.id.status);

/*
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), StudentAppointmentsPageMakeAnAppointmentActivity.class);
                    intent.putExtra("function","edit");
                    String test = post_key;
                    intent.putExtra("apptId",post_key);
                    v.getContext().startActivity(intent);
                }
            });
            */
        }
    }

    public StudentAppointmentAdapter(Context mContext, List<AppointmentCard> appointmentList) {
        this.mContext = mContext;
        this.appointmentList = appointmentList;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_student_appointments_page_card_view, parent, false);

        return new StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StudentViewHolder holder, int position) {

        AppointmentCard appointment = appointmentList.get(position);

        final String post_key = appointmentList.get(position).getId();

        holder.date.setText(appointment.getDate());
        holder.time.setText(appointment.getTime());
        holder.type.setText(appointment.getType());
        holder.status.setText(appointment.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), StudentAppointmentsPageMakeAnAppointmentActivity.class);
                intent.putExtra("function","edit");
                String test = post_key;
                intent.putExtra("apptId",post_key);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }
}
