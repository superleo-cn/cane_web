package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import forms.CaneForm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "gz_cane")
public class Cane {

    final static Logger logger = LoggerFactory.getLogger(Cane.class);

    @Id
    private Long id;

    @Required(message = "Device Id cannot be empty")
    private String deviceId;

    private String imsi;

    private String iccid;

    private String sosOne;

    private String sosTwo;

    private Integer gpsSwitch;

    private Integer hasNewData;

    private Date created;

    public static Cane findCraneById(String deviceId) {
        try {
            return Ebean.find(Cane.class).where().eq("deviceId", deviceId).findUnique();
        } catch (Exception e) {
            logger.error("[findCraneById] -> [exception]", e);
        }
        return null;
    }

    public static boolean save(CaneForm crane) {
        try {
            Cane db = new Cane();
            db.setCreated(new Date());
            db.setDeviceId(crane.getDevice_id());
            db.setIccid(crane.getIccid());
            db.setImsi(crane.getImsi());
            db.setGpsSwitch(1);
            db.setHasNewData(1);
            Ebean.save(db);
            return true;
        } catch (Exception e) {
            logger.error("[save] -> [exception]", e);
        }
        return false;
    }

    public static boolean update(CaneForm crane) {
        try {
            ExpressionList<Cane> expList = Ebean.find(Cane.class).where();
            if (StringUtils.isNotEmpty(crane.getDevice_id())) {
                expList.where().eq("deviceId", crane.getDevice_id());
                Cane db = expList.findUnique();
                db.setSosOne(crane.getSos_one());
                db.setSosTwo(crane.getSos_two());
                db.setGpsSwitch(crane.getGps_switch());
                Ebean.update(db);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("[findCraneById] -> [exception]", e);
        }
        return false;
    }

    public static boolean updateSos(CaneForm crane) {
        try {
            ExpressionList<Cane> expList = Ebean.find(Cane.class).where();
            if (StringUtils.isNotEmpty(crane.getDevice_id())) {
                expList.where().eq("deviceId", crane.getDevice_id());
                Cane db = expList.findUnique();
                db.setSosOne(crane.getSos_one());
                db.setSosTwo(crane.getSos_two());
                Ebean.update(db);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("[findCraneById] -> [exception]", e);
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

    public String getSosOne() {
        return sosOne;
    }

    public void setSosOne(String sosOne) {
        this.sosOne = sosOne;
    }

    public String getSosTwo() {
        return sosTwo;
    }

    public void setSosTwo(String sosTwo) {
        this.sosTwo = sosTwo;
    }

    public Integer getGpsSwitch() {
        return gpsSwitch;
    }

    public void setGpsSwitch(Integer gpsSwitch) {
        this.gpsSwitch = gpsSwitch;
    }

    public Integer getHasNewData() {
        return hasNewData;
    }

    public void setHasNewData(Integer hasNewData) {
        this.hasNewData = hasNewData;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
