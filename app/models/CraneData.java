package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "gps_collection")
public class CraneData {

    final static Logger logger = LoggerFactory.getLogger(CraneData.class);

    @Id
    private Long id;

    private String deviceId;

    private String latitude;

    private String longitude;

    private String acc;

    private String cellId;

    private String lac;

    private String plmn;

    private Integer orientation;

    private Date created;


    // realtime
    public static List<CraneData> findRealtimeList(String braceletId, String date) {
        try {
            ExpressionList<CraneData> expList = Ebean.find(CraneData.class).where();
            if (StringUtils.isNotEmpty(braceletId) && StringUtils.isNotEmpty(date)) {
                Date lastDate = DateUtils.parseDate(date, Constants.PATTERN_YYYYMMDDHHMMSS);
                Date startDate = DateUtils.addSeconds(lastDate, -5);
                expList.where().eq("braceletId", braceletId);
                expList.where().ge("createDate", startDate);
                expList.where().le("createDate", lastDate);
            }
            expList.orderBy("createDate desc");
            return expList.findList();
        } catch (Exception e) {
            logger.error("[findRealtimeList] -> [exception]", e);
        }
        return null;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getPlmn() {
        return plmn;
    }

    public void setPlmn(String plmn) {
        this.plmn = plmn;
    }

    public Integer getOrientation() {
        return orientation;
    }

    public void setOrientation(Integer orientation) {
        this.orientation = orientation;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
