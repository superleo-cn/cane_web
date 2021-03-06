package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import constants.Messages;
import forms.GPSForm;
import forms.SSOForm;
import models.Cane;
import models.GPSData;
import models.SSOData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import utils.Pagination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPSDatas extends Basic {

    final static Logger logger = LoggerFactory.getLogger(GPSDatas.class);

    /**
     * [APP] - [Interface] - [查询GPS信息]
     *
     * @return
     */
    public Result findGPSByDate(String deviceId, String time, Integer page) {
        // token checking
        Result tokenResult = isValidToken(deviceId);
        if (tokenResult != null) {
            return tokenResult;
        }

        // Normal processing
        ObjectNode result = Json.newObject();
        try {
            Pagination<GPSData> pagination = new Pagination<>(page);
            pagination = GPSData.findGPSByDate(pagination, deviceId, time);
            if (pagination != null) {
                // header
                Map<String, String> headers = new HashMap<>();
                headers.put(Constants.CODE, "20061");
                headers.put(Constants.MESSAGE, Constants.MSG_SUCCESS);
                result.replace(Constants.STATUS, Json.toJson(headers));

                // data
                List<JsonNode> data = new ArrayList<>();
                List<GPSData> list = pagination.getRecordList();
                Integer currentPage = pagination.getCurrentPage();
                if (CollectionUtils.isNotEmpty(list)) {
                    for (GPSData gps : list) {
                        Map gpsMap = new HashMap<>();
                        gpsMap.put("latitude", gps.getLatitude());
                        gpsMap.put("longitude", gps.getLongitude());
                        gpsMap.put("created", DateFormatUtils.format(gps.getCreated(), Constants.PATTERN_YYYYMMDDHHMMSS_LONG));
                        gpsMap.put("orientation", gps.getOrientation());
                        gpsMap.put("acc", gps.getAcc());
                        gpsMap.put("flag", gps.getFlag());
                        gpsMap.put("cell_id", gps.getCellId());
                        gpsMap.put("lac", gps.getLac());
                        data.add(Json.toJson(gpsMap));
                    }
                    result.replace(Constants.DATA, Json.toJson(data));
                } else {
                    result.replace(Constants.DATA, Json.toJson(null));
                }

                // pageCount
                result.replace(Constants.PAGE_COUNT, Json.toJson(pagination.getPageCount()));

            } else {
                result.put(Constants.STATUS, Constants.FAILURE);
            }
        } catch (Exception e) {
            result.put(Constants.STATUS, Constants.ERROR);
            logger.error(Messages.CANE_DATA_LIST_ERROR_MESSAGE, new Object[]{deviceId, e});
        }
        return ok(result);
    }

    public Result findGPSByDateBak(String deviceId, String time, Integer page) {
        // token checking
        Result tokenResult = isValidToken(deviceId);
        if (tokenResult != null) {
            return tokenResult;
        }

        // Normal processing
        ObjectNode result = Json.newObject();
        try {
            Pagination<GPSData> pagination = new Pagination<>(page);
            pagination = GPSData.findGPSByDate(pagination, deviceId, time);
            if (pagination != null) {
                // header
                Map<String, String> headers = new HashMap<>();
                headers.put(Constants.CODE, "20061");
                headers.put(Constants.MESSAGE, Constants.MSG_SUCCESS);
                result.replace(Constants.STATUS, Json.toJson(headers));

                // data
                Map<String, JsonNode> data = new HashMap<>();
                List<GPSData> list = pagination.getRecordList();
                Integer currentPage = pagination.getCurrentPage();
                if (CollectionUtils.isNotEmpty(list)) {
                    for (GPSData gps : list) {
                        Map gpsMap = new HashMap<>();
                        gpsMap.put("latitude", gps.getLatitude());
                        gpsMap.put("longitude", gps.getLongitude());
                        gpsMap.put("created", DateFormatUtils.format(gps.getCreated(), Constants.PATTERN_YYYYMMDDHHMMSS_LONG));
                        gpsMap.put("orientation", gps.getOrientation());
                        gpsMap.put("acc", gps.getAcc());
                        gpsMap.put("flag", gps.getFlag());
                        gpsMap.put("cell_id", gps.getCellId());
                        gpsMap.put("lac", gps.getLac());
                        data.put(String.valueOf(currentPage++), Json.toJson(gpsMap));
                    }
                    result.replace(Constants.DATA, Json.toJson(data));
                } else {
                    result.replace(Constants.DATA, Json.toJson(null));
                }

                // pageCount
                result.replace(Constants.PAGE_COUNT, Json.toJson(pagination.getPageCount()));

            } else {
                result.put(Constants.STATUS, Constants.FAILURE);
            }
        } catch (Exception e) {
            result.put(Constants.STATUS, Constants.ERROR);
            logger.error(Messages.CANE_DATA_LIST_ERROR_MESSAGE, new Object[]{deviceId, e});
        }
        return ok(result);
    }

    /**
     * [APP] - [Interface] - [查询GPS信息(最近一条)]
     *
     * @return
     */
    public Result findGPSByLatest(String deviceId) {
        // token checking
        Result tokenResult = isValidToken(deviceId);
        if (tokenResult != null) {
            return tokenResult;
        }

        // Normal processing
        ObjectNode result = Json.newObject();
        try {
            GPSData gps = GPSData.findGPSByLatest(deviceId);
            if (gps != null) {
                // header
                Map<String, String> headers = new HashMap<>();
                headers.put(Constants.CODE, "20061");
                headers.put(Constants.MESSAGE, Constants.MSG_SUCCESS);
                result.replace(Constants.STATUS, Json.toJson(headers));

                // data
                Map gpsMap = new HashMap<>();
                gpsMap.put("latitude", gps.getLatitude());
                gpsMap.put("longitude", gps.getLongitude());
                gpsMap.put("created", DateFormatUtils.format(gps.getCreated(), Constants.PATTERN_YYYYMMDDHHMMSS_LONG));
                gpsMap.put("orientation", gps.getOrientation());
                gpsMap.put("acc", gps.getAcc());
                gpsMap.put("flag", gps.getFlag());
                gpsMap.put("cell_id", gps.getCellId());
                gpsMap.put("lac", gps.getLac());
                result.replace(Constants.DATA, Json.toJson(gpsMap));

                // pageCount
                result.replace(Constants.PAGE_COUNT, Json.toJson(0));

            } else {
                result.put(Constants.STATUS, Constants.FAILURE);
            }
        } catch (Exception e) {
            result.put(Constants.STATUS, Constants.ERROR);
            logger.error(Messages.CANE_DATA_LIST_ERROR_MESSAGE, new Object[]{deviceId, e});
        }
        return ok(result);
    }


    /**
     * [CANE] - [Interface] - [GPS位置信息上传接口]
     *
     * @return
     */
    public Result addGPSData() {
        ObjectNode result = Json.newObject();
        GPSForm form = null;
        try {
            logger.info("=========START===========");
            form = Form.form(GPSForm.class).bindFromRequest().get();
            logger.info(form.getDeviceId());
            logger.info(form.getAcc());
            logger.info(form.getCellId());
            logger.info(form.getLac());
            logger.info(form.getLatitude());
            logger.info(form.getLongitude());
            logger.info(form.getPlmn());
            logger.info(form.getFlag() + "");
            logger.info(form.getOrientation() + "");
            logger.info("=========END===========");

            Cane dbCane = Cane.findCraneById(form.getDeviceId());
            if (dbCane != null) {
                // save GPS data
                GPSData.save(form);
                if (dbCane.getHasNewData() == 1) {
                    // header
                    result.replace(Constants.SIGN, Json.toJson("heart"));
                    result.replace(Constants.STATUS, Json.toJson(Constants.MSG_SUCCESS));

                    // sos phones
                    Map sosPhones = new HashMap<>();
                    sosPhones.put("sos_one", dbCane.getSosOne());
                    sosPhones.put("sos_two", dbCane.getSosTwo());
                    sosPhones.put("gps_switch", dbCane.getGpsSwitch());
                    result.replace(Constants.DATA, Json.toJson(sosPhones));

                    // update to no new data
                    Cane.updateStatus(dbCane, Constants.HAS_NEW_DATA_NO);
                } else {
                    result.replace(Constants.SIGN, Json.toJson("hrt_no"));
                    result.replace(Constants.STATUS, Json.toJson(Constants.MSG_NO));
                }
            } else {
                result.replace(Constants.SIGN, Json.toJson(Constants.MSG_ERROR));
                result.replace(Constants.STATUS, Json.toJson(Constants.MSG_ILLEGAL));
            }
        } catch (Exception e) {
            result.replace(Constants.SIGN, Json.toJson(Constants.MSG_ERROR));
            result.replace(Constants.STATUS, Json.toJson(Constants.MSG_ILLEGAL));
            logger.error(Messages.CANE_DATA_LIST_ERROR_MESSAGE, new Object[]{form.getDeviceId(), e});
        }
        return ok(result);
    }


    /**
     * [CANE] - [Interface] - [上传紧急呼叫位置信息接口]
     *
     * @return
     */
    public Result addSSOData() {
        ObjectNode result = Json.newObject();
        SSOForm form = null;
        try {
            logger.info("=========START===========");
            form = Form.form(SSOForm.class).bindFromRequest().get();
            logger.info(form.getDeviceId());
            logger.info(form.getAcc());
            logger.info(form.getCellId());
            logger.info(form.getLac());
            logger.info(form.getLatitude());
            logger.info(form.getLongitude());
            logger.info(form.getPlmn());
            logger.info(form.getBattery() + "");
            logger.info(form.getImsi());
            logger.info(form.getState());
            logger.info(form.getTime());
            logger.info("=========END===========");

            Cane dbCane = Cane.findCraneById(form.getDeviceId());
            if (dbCane != null) {
                // save SSO data
                if (SSOData.save(form)) {
                    result.replace(Constants.SIGN, Json.toJson(Constants.MSG_SUCCESS));
                } else {
                    result.replace(Constants.SIGN, Json.toJson(Constants.MSG_ERROR));
                }
            } else {
                result.replace(Constants.SIGN, Json.toJson(Constants.MSG_ERROR));
            }
        } catch (Exception e) {
            result.replace(Constants.SIGN, Json.toJson(Constants.MSG_ERROR));
            logger.error(Messages.CANE_DATA_LIST_ERROR_MESSAGE, new Object[]{form.getDeviceId(), e});
        }
        return ok(result);
    }

}
