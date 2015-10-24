package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import forms.CraneForm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "gz_crane")
public class Crane {

    final static Logger logger = LoggerFactory.getLogger(Crane.class);

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

    public static Crane findCraneById(String deviceId) {
        try {
            ExpressionList<Crane> expList = Ebean.find(Crane.class).where();
            if (StringUtils.isNotEmpty(deviceId)) {
                expList.where().eq("deviceId", deviceId);
                return expList.findUnique();
            }
            return null;
        } catch (Exception e) {
            logger.error("[findCraneById] -> [exception]", e);
        }
        return null;
    }

    public static boolean update(CraneForm crane) {
        try {
            ExpressionList<Crane> expList = Ebean.find(Crane.class).where();
            if (StringUtils.isNotEmpty(crane.getDevice_id())) {
                expList.where().eq("deviceId", crane.getDevice_id());
                Crane db = expList.findUnique();
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

    public static boolean updateSos(CraneForm crane) {
        try {
            ExpressionList<Crane> expList = Ebean.find(Crane.class).where();
            if (StringUtils.isNotEmpty(crane.getDevice_id())) {
                expList.where().eq("deviceId", crane.getDevice_id());
                Crane db = expList.findUnique();
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
