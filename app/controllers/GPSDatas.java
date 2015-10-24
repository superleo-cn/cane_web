package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import constants.Messages;
import models.GPSData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Result;
import utils.Pagination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPSDatas extends Basic {

    final static Logger logger = LoggerFactory.getLogger(GPSDatas.class);

    // 查询状态信息接口
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
            logger.error(Messages.CRANE_DATA_LIST_ERROR_MESSAGE, new Object[]{deviceId, e});
        }
        return ok(result);
    }

    // 查询状态信息接口
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
            logger.error(Messages.CRANE_DATA_LIST_ERROR_MESSAGE, new Object[]{deviceId, e});
        }
        return ok(result);
    }

}
