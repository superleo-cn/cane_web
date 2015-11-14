package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.PagedList;
import constants.Constants;
import forms.GPSForm;
import forms.SSOForm;
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
@Table(name = "sos_location")
public class SSOData {

    final static Logger logger = LoggerFactory.getLogger(SSOData.class);

    @Id
    private Long id;

    private String deviceId;

    private String latitude;

    private String longitude;

    private String acc;

    private String cellId;

    private String lac;

    private String plmn;

    private Integer battery;

    private String imsi;

    private String state;

    private String time;

    public static Pagination findSSOByDate(Pagination pagination, String deviceId, String date) {
        try {
            pagination = pagination == null ? new Pagination() : pagination;
            ExpressionList<SSOData> expList = Ebean.find(SSOData.class).where();
            PagedList<SSOData> pagingList = expList.findPagedList(pagination.currentPage - 1, pagination.pageSize);
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

    public static SSOData findSSOByLatest(String deviceId) {
        try {
            ExpressionList<SSOData> expList = Ebean.find(SSOData.class).where();
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

    public static boolean save(SSOForm form) {
        try {
            SSOData db = new SSOData();
            db.setDeviceId(form.getDeviceId());
            db.setAcc(form.getAcc());
            db.setCellId(form.getCellId());
            db.setLac(form.getLac());
            db.setLatitude(form.getLatitude());
            db.setLongitude(form.getLongitude());
            db.setPlmn(form.getPlmn());
            db.setBattery(form.getBattery());
            db.setImsi(form.getImsi());
            db.setState(form.getState());
            db.setTime(form.getTime());
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

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
