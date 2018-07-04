package fyp.counsellingandwellness.icounselling;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {

    //counsellor and student
    private String email;
    private String name;
    private String role;

    //counsellor
    private String description;
    private String major;

    //student
    private String course;
    private String gender;
    private String age;
    private String contact;
    private String address;
    private String counsellor;

    public UserInfo() {
    }

    //counsellor
    public UserInfo(String email, String name, String role, String description, String major) {
        setEmail(email);
        setName(name);
        setRole(role);
        setDescription(description);
        setMajor(major);
    }

    //student
    public UserInfo(String email, String name, String role, String course, String gender, String age, String contact, String address, String counsellor) {
        setEmail(email);
        setName(name);
        setRole(role);
        setCourse(course);
        setGender(gender);
        setAge(age);
        setContact(contact);
        setAddress(address);
        setCounsellor(counsellor);
    }

    private UserInfo(Parcel parcelIn) {
        setEmail(parcelIn.readString());
        setName(parcelIn.readString());
        setRole(parcelIn.readString());
        setDescription(parcelIn.readString());
        setMajor(parcelIn.readString());
        setCourse(parcelIn.readString());
        setGender(parcelIn.readString());
        setAge(parcelIn.readString());
        setContact(parcelIn.readString());
        setAddress(parcelIn.readString());
        setCounsellor(parcelIn.readString());
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getEmail());
        parcel.writeString(getName());
        parcel.writeString(getRole());
        parcel.writeString(getDescription());
        parcel.writeString(getMajor());
        parcel.writeString(getCourse());
        parcel.writeString(getGender());
        parcel.writeString(getAge());
        parcel.writeString(getContact());
        parcel.writeString(getAddress());
        parcel.writeString(getCounsellor());
    }

    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getRole()
    {
        return this.role;
    }
    public void setRole(String role)
    {
        this.role = role;
    }

    public String getDescription()
    {
        return this.description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getMajor()
    {
        return this.major;
    }
    public void setMajor(String major)
    {
        this.major = major;
    }

    public String getCourse()
    {
        return this.course;
    }
    public void setCourse(String course)
    {
        this.course = course;
    }

    public String getGender()
    {
        return this.gender;
    }
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getAge()
    {
        return this.age;
    }
    public void setAge(String age)
    {
        this.age = age;
    }

    public String getContact()
    {
        return this.contact;
    }
    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getAddress()
    {
        return this.address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getCounsellor()
    {
        return this.counsellor;
    }
    public void setCounsellor(String counsellor)
    {
        this.counsellor = counsellor;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }
    public static final Parcelable.Creator<UserInfo> CREATOR
            = new Parcelable.Creator<UserInfo>() {

        @Override
        public UserInfo createFromParcel(Parcel parcelIn) {
            return new UserInfo(parcelIn);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
