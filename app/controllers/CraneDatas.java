package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import constants.Messages;
import inteceptors.TokenInterceptor;
import models.Crane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.With;

@With(TokenInterceptor.class)
public class CraneDatas extends Basic {

    final static Logger logger = LoggerFactory.getLogger(CraneDatas.class);

    // 查询状态信息接口
    public Result getGzData(String ak) {
        String deviceId = Form.form().bindFromRequest().get("deviceId");
        ObjectNode result = Json.newObject();
        try {
            Crane data = Crane.findCraneById(deviceId);
            if (data != null) {
                result.put("sos_one", data.getSosOne());
                result.put("sos_two", data.getSosTwo());
                result.put("phone_num", "12345678");
                result.put("has_new_data", data.getHasNewData());
                result.put("gps_switch", data.getGpsSwitch());
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
