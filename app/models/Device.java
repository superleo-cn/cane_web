package models;

import com.avaje.ebean.Ebean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "device_to_sim")
public class Device {

    final static Logger logger = LoggerFactory.getLogger(Device.class);

    @Id
    private Long id;

    @Required(message = "Device Id cannot be empty")
    private String deviceId;

    private String imsi;

    private String iccid;

    private String phoneNum;

    private String password;

    private Integer flag;

    private String resetcode;

    private Date created;

    public static Device findDeviceById(String deviceId) {
        try {
            return Ebean.find(Device.class).where().eq("deviceId", deviceId).findUnique();
        } catch (Exception e) {
            logger.error("[findDeviceById] -> [exception]", e);
        }
        return null;
    }

    public static boolean update(Device device) {
        try {
            Device db = Ebean.find(Device.class).where().eq("deviceId", device.getDeviceId()).findUnique();
            if (db != null) {
                db.setIccid(device.getIccid());
                db.setImsi(device.getImsi());
                db.setPhoneNum(device.getPhoneNum());
                Ebean.update(db);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("[update] -> [exception]", e);
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getResetcode() {
        return resetcode;
    }

    public void setResetcode(String resetcode) {
        this.resetcode = resetcode;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
