package forms;

public class CraneForm {

    private String device_id;

    private String imsi;

    private String iccid;

    private String sos_one;

    private String sos_two;

    private Integer gps_switch;

    private String phone_num;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getSos_one() {
        return sos_one;
    }

    public void setSos_one(String sos_one) {
        this.sos_one = sos_one;
    }

    public String getSos_two() {
        return sos_two;
    }

    public void setSos_two(String sos_two) {
        this.sos_two = sos_two;
    }

    public Integer getGps_switch() {
        return gps_switch;
    }

    public void setGps_switch(Integer gps_switch) {
        this.gps_switch = gps_switch;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }
}
