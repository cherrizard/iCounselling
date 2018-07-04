package fyp.counsellingandwellness.icounselling;

import android.os.Parcel;
import android.os.Parcelable;

public class AppointmentInfo {
    private String appointmentID;
    private String counsellor;
    private String student;
    private String date;
    private String time;
    private String appointmentType;
    private String appointmentStatus;
    private String dateFormatShort;

    public AppointmentInfo(){
    }

    public AppointmentInfo(String appointmentID, String counsellor, String student, String date, String time, String appointmentType, String appointmentStatus, String dateFormatShort){
        setAppointmentID(appointmentID);
        setCounsellor(counsellor);
        setStudent(student);
        setDate(date);
        setTime(time);
        setAppointmentType(appointmentType);
        setAppointmentStatus(appointmentStatus);
        setDateFormatShort(dateFormatShort);
    }

    private AppointmentInfo(Parcel parcelIn) {
        setAppointmentID(parcelIn.readString());
        setCounsellor(parcelIn.readString());
        setStudent(parcelIn.readString());
        setDate(parcelIn.readString());
        setTime(parcelIn.readString());
        setAppointmentType(parcelIn.readString());
        setAppointmentStatus(parcelIn.readString());
        setDateFormatShort(parcelIn.readString());
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getAppointmentID());
        parcel.writeString(getCounsellor());
        parcel.writeString(getStudent());
        parcel.writeString(getDate());
        parcel.writeString(getTime());
        parcel.writeString(getAppointmentType());
        parcel.writeString(getAppointmentStatus());
        parcel.writeString(getDateFormatShort());
    }

    public String getAppointmentID() {
        return this.appointmentID;
    }
    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getCounsellor() {
        return this.counsellor;
    }
    public void setCounsellor(String counsellor) {
        this.counsellor = counsellor;
    }

    public String getStudent() { return this.student; }
    public void setStudent(String student) {
        this.student = student;
    }

    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getAppointmentType() {
        return this.appointmentType;
    }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }

    public String getAppointmentStatus() {
        return this.appointmentStatus;
    }
    public void setAppointmentStatus(String appointmentStatus) { this.appointmentStatus = appointmentStatus; }

    public String getDateFormatShort() {
        return this.dateFormatShort;
    }
    public void setDateFormatShort(String dateFormatShort) { this.dateFormatShort = dateFormatShort; }


    public int describeContents()
    {
        return 0;
    }
    public static final Parcelable.Creator<AppointmentInfo> CREATOR
            = new Parcelable.Creator<AppointmentInfo>() {

        @Override
        public AppointmentInfo createFromParcel(Parcel parcelIn) {
            return new AppointmentInfo(parcelIn);
        }

        @Override
        public AppointmentInfo[] newArray(int size) {
            return new AppointmentInfo[size];
        }
    };
}