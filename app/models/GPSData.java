package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.PagedList;
import constants.Constants;
import forms.GPSForm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Pagination;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "gps_collection")
public class GPSData {

    final static Logger logger = LoggerFactory.getLogger(GPSData.class);

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

    private Integer flag;

    private Date created;

    public static Pagination findGPSByDate(Pagination pagination, String deviceId, String date) {
        try {
            pagination = pagination == null ? new Pagination() : pagination;
            ExpressionList<GPSData> expList = Ebean.find(GPSData.class).where();
            PagedList<GPSData> pagingList = expList.findPagedList(pagination.currentPage - 1, pagination.pageSize);
            if (StringUtils.isNotEmpty(deviceId) && StringUtils.isNotEmpty(date)) {
                Date startDate = DateUtils.parseDate(date, Constants.PATTERN_YYYYMMDDHHMMSS);
                expList.where().eq("deviceId", deviceId);
                expList.where().ge("created", startDate);
            } else {
                return pagination;
            }
            expList.orderBy("created asc");
            pagination.recordList = pagingList.getList();
            pagination.pageCount = pagingList.getTotalPageCount();
            pagination.recordCount = pagingList.getTotalRowCount();
            return pagination;
        } catch (Exception e) {
            logger.error("[findGPSByDate] -> [exception]", e);
        }
        return null;
    }

    public static GPSData findGPSByLatest(String deviceId) {
        try {
            ExpressionList<GPSData> expList = Ebean.find(GPSData.class).where();
            if (StringUtils.isNotEmpty(deviceId)) {
                expList.where().eq("deviceId", deviceId);
                expList.orderBy("created desc");
            } else {
                return null;
            }
            return expList.setMaxRows(1).findUnique();
        } catch (Exception e) {
            logger.error("[findGPSByLatest] -> [exception]", e);
        }
        return null;
    }

    public static boolean save(GPSForm form) {
        try {
            GPSData db = new GPSData();
            Integer val = form.getOrientation();
            Integer flag = 0;
            if (val == null) {
                val = form.getBattery() == null ? form.getBattery() : 0;
            }
            if (form.getLatitude() != null || form.getLongitude() != null) {
                flag = 1;
            }
            db.setCreated(new Date());
            db.setDeviceId(form.getDeviceId());
            db.setAcc(form.getAcc());
            db.setCellId(form.getCellId());
            db.setFlag(form.getFlag());
            db.setLac(form.getLac());
            db.setLatitude(form.getLatitude());
            db.setLongitude(form.getLongitude());
            db.setOrientation(val);
            db.setPlmn(form.getPlmn());
            db.setFlag(flag);
            Ebean.save(db);
            return true;
        } catch (Exception e) {
            logger.error("[save] -> [exception]", e);
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

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
