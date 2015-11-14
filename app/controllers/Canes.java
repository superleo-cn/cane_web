package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import constants.Messages;
import forms.CaneForm;
import inteceptors.TokenInterceptor;
import models.Cane;
import models.Contact;
import models.Device;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.With;
import utils.MyUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Canes extends Basic {

    final static Logger logger = LoggerFactory.getLogger(Canes.class);


    /**
     * [APP] - [Interface] - [查询状态信息接口]
     *
     * @return
     */
    @With(TokenInterceptor.class)
    public Result getGzData() {
        String deviceId = Form.form().bindFromRequest().get("device_id");
        ObjectNode result = Json.newObject();
        try {
            Cane data = Cane.findCraneById(deviceId);
            if (data != null) {
                result.put("sos_one", data.getSosOne());
                result.put("sos_two", data.getSosTwo());
                result.put("has_new_data", data.getHasNewData());
                result.put("gps_switch", data.getGpsSwitch());
                List<Contact> list = Contact.findContactByDeviceId(deviceId);
                if (CollectionUtils.isNotEmpty(list)) {
                    String phoneNums = list.stream().map(contact -> contact.getName() + ":" + contact.getCellNumber() + ",").collect(Collectors.joining());
                    result.put("phone_num", phoneNums);
                } else {
                    result.put("phone_num", "");
                }
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
     * [APP] - [Interface] - [更新信息接口]
     *
     * @return
     */
    @With(TokenInterceptor.class)
    public Result updatedata() {
        CaneForm form = Form.form(CaneForm.class).bindFromRequest().get();
        ObjectNode result = Json.newObject();
        try {
            if (form != null) {
                Cane dbCane = Cane.findCraneById(form.getDevice_id());
                if (dbCane != null) {
                    if (Cane.update(form)) {
                        result.put(Constants.STATUS, Constants.MSG_SUCCESS);
                    } else {
                        result.put(Constants.STATUS, Constants.FAILURE);
                    }
                } else {
                    result.put(Constants.STATUS, Constants.MSG_NO_REGISTER);
                }
            } else {
                result.put(Constants.STATUS, Constants.FAILURE);
            }
        } catch (Exception e) {
            result.put(Constants.STATUS, Constants.ERROR);
            logger.error(Messages.CANE_DATA_LIST_ERROR_MESSAGE, new Object[]{form.getDevice_id(), e});
        }
        return ok(result);
    }

    /**
     * [APP] - [Interface] - [查询用户联系人信息接口]
     *
     * @return
     */
    @With(TokenInterceptor.class)
    public Result getContacts() {
        CaneForm form = Form.form(CaneForm.class).bindFromRequest().get();
        ObjectNode result = Json.newObject();
        try {
            if (form != null) {
                Cane dbCane = Cane.findCraneById(form.getDevice_id());
                if (dbCane != null) {
                    // sos phones
                    Map sosPhones = new HashMap<>();
                    sosPhones.put("sos_one", dbCane.getSosOne());
                    sosPhones.put("sos_two", dbCane.getSosTwo());
                    result.replace("sos_phone", Json.toJson(sosPhones));

                    // contact list
                    List<Contact> list = Contact.findContactByDeviceId(form.getDevice_id());
                    List<Map> phones = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (Contact contact : list) {
                            Map contactMap = new HashMap<>();
                            contactMap.put("phone", contact.getCellNumber());
                            contactMap.put("name", contact.getName());
                            phones.add(contactMap);
                        }
                    }
                    result.replace("phone_num", Json.toJson(phones));

                    // status
                    result.put(Constants.STATUS, Constants.MSG_SUCCESS);
                } else {
                    result.put(Constants.STATUS, Constants.MSG_NO_REGISTER);
                }
            } else {
                result.put(Constants.STATUS, Constants.FAILURE);
            }
        } catch (Exception e) {
            result.put(Constants.STATUS, Constants.ERROR);
            logger.error(Messages.CANE_DATA_LIST_ERROR_MESSAGE, new Object[]{form.getDevice_id(), e});
        }
        return ok(result);
    }


    /**
     * [APP] - [Interface] - [更新联系人信息接口]
     *
     * @return
     */
    @With(TokenInterceptor.class)
    public Result setContacts() {
        CaneForm form = Form.form(CaneForm.class).bindFromRequest().get();
        ObjectNode result = Json.newObject();
        try {
            if (form != null) {
                Cane dbCane = Cane.findCraneById(form.getDevice_id());
                if (dbCane != null) {
                    if (Contact.update(form)) {
                        result.put(Constants.STATUS, Constants.MSG_SUCCESS);
                    } else {
                        result.put(Constants.STATUS, Constants.FAILURE);
                    }
                } else {
                    result.put(Constants.STATUS, Constants.MSG_NO_REGISTER);
                }
            } else {
                result.put(Constants.STATUS, Constants.FAILURE);
            }
        } catch (Exception e) {
            result.put(Constants.STATUS, Constants.ERROR);
            logger.error(Messages.CANE_DATA_LIST_ERROR_MESSAGE, new Object[]{form.getDevice_id(), e});
        }
        return ok(result);
    }


    /**
     * [CANE] - [Interface] - [手杖开机上传imsi和iccid接口]
     *
     * @return
     */
    public Result firstUpload() {
        CaneForm form = Form.form(CaneForm.class).bindFromRequest().get();
        ObjectNode result = Json.newObject();
        try {
            if (form != null) {
                Cane dbCane = Cane.findCraneById(form.getDevice_id());
                if (dbCane == null) {
                    // 第一次注册
                    Device dbDevice = Device.findDeviceById(form.getDevice_id());
                    if (dbDevice != null) {
                        if (Cane.save(form)) {
                            // sos phones
                            dbCane = Cane.findCraneById(form.getDevice_id());
                            Map sosPhones = new HashMap<>();
                            sosPhones.put("sos_one", dbCane.getSosOne());
                            sosPhones.put("sos_two", dbCane.getSosTwo());
                            sosPhones.put("gps_switch", dbCane.getGpsSwitch());
                            result.replace(Constants.DATA, Json.toJson(sosPhones));
                            // Time
                            result.replace("time_init", Json.toJson(DateFormatUtils.format(new Date(), Constants.PATTERN_YYYYMMDDHHMMSS_LONG)));
                            // status
                            result.put(Constants.STATUS, Constants.MSG_SUCCESS);
                            // imsiback
                            result.put(Constants.SIGN, "imsiback");
                        }
                    } else {
                        // 发送失败，设备号不存在
                        // status
                        result.put(Constants.STATUS, Constants.MSG_ILLEGAL);
                        // imsiback
                        result.put(Constants.SIGN, "imsiback");
                    }
                } else {
                    // 否则直接返回第一次开机数据
                    // status
                    result.put(Constants.STATUS, Constants.MSG_SUCCESS);
                    // imsiback
                    result.put(Constants.SIGN, "imsiback");
                    // Time
                    result.replace("time_init", Json.toJson(DateFormatUtils.format(new Date(), Constants.PATTERN_YYYYMMDDHHMMSS_LONG)));
                }
            } else {
                result.put(Constants.STATUS, Constants.FAILURE);
            }
        } catch (Exception e) {
            result.put(Constants.STATUS, Constants.ERROR);
            logger.error(Messages.CANE_DATA_LIST_ERROR_MESSAGE, new Object[]{form.getDevice_id(), e});
        }
        return ok(result);
    }

    /**
     * [CANE] - [Interface] - [拐杖对时接口 ]
     *
     * @return
     */
    public Result currenttime() {
        return ok("2015-01-01 00:00:00");
    }

    /**
     * [CANE] - [Interface] - [手杖查询接口]
     *
     * @return
     */
    public Result getStatus() {
        CaneForm form = Form.form(CaneForm.class).bindFromRequest().get();
        ObjectNode result = Json.newObject();
        String finalVal;

        logger.info("=========START===========");
        logger.info(form.getDevice_id());
        logger.info("=========END===========");

        try {
            if (form != null) {
                Cane dbCane = Cane.findCraneById(form.getDevice_id());
                if (dbCane != null) {
                    List<Contact> contacts = null;
                    if (dbCane != null) {
                        contacts = Contact.findContactByDeviceId(form.getDevice_id());
                    }
                    Map sosPhones = new HashMap<>();
                    sosPhones.put("sos_one", dbCane.getSosOne());
                    sosPhones.put("sos_two", dbCane.getSosTwo());
                    sosPhones.put("gps_switch", dbCane.getGpsSwitch());
                    result.replace(Constants.DATA, Json.toJson(sosPhones));
                    // status
                    result.put(Constants.STATUS, Constants.MSG_SUCCESS);
                    // heart
                    result.put(Constants.SIGN, "heart");
                    // Phone num
                    String phoneCon = "";
                    if (contacts != null) {
                        phoneCon = contacts.stream().map(c -> MyUtils.getUnicode(c.getName()) + ":" + c.getCellNumber()).collect(Collectors.joining(","));
                        result.put("Phone_num", contacts.size());
                    } else {
                        result.put("Phone_num", 0);
                    }
                    finalVal = "Phone_con:" + phoneCon + "," + Json.toJson(result).toString();
                } else {
                    // 发送失败，设备号不存在
                    // status
                    result.put(Constants.STATUS, Constants.MSG_NO);
                    // imsiback
                    result.put(Constants.SIGN, "hrt_no");
                    finalVal = Json.toJson(result).toString();
                }
            } else {
                result.put(Constants.STATUS, Constants.FAILURE);
                finalVal = Json.toJson(result).toString();
            }
        } catch (Exception e) {
            result.put(Constants.STATUS, Constants.ERROR);
            logger.error(Messages.CANE_DATA_LIST_ERROR_MESSAGE, new Object[]{form.getDevice_id(), e});
            finalVal = Json.toJson(result).toString();
        }
        return ok(finalVal);
    }
}
