package fyp.counsellingandwellness.icounselling;

public class AppointmentCard {

    private String id;
    private String date;
    private String time;
    private String type;
    private String status;
    private String student;

    public AppointmentCard() {
    }

    public AppointmentCard(String id, String date, String time, String type, String status, String student) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.type = type;
        this.status = status;
        this.student = student;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() { return date; }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }


}
