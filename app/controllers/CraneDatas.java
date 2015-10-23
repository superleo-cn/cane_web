package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import constants.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;


public class CraneDatas extends Controller {

    final static Logger logger = LoggerFactory.getLogger(CraneDatas.class);

    // history
    public Result getGzData() {
        String deviceId = Form.form().bindFromRequest().get("deviceId");
        ObjectNode result = Json.newObject();
        try {
            result.put(Constants.CODE, Constants.SUCCESS);
            result.put(Constants.DATAS, deviceId);
        } catch (Exception e) {
            result.put(Constants.CODE, Constants.ERROR);
            result.put(Constants.MESSAGE, Messages.HEALTH_DATA_LIST_ERROR);
            logger.error(Messages.HEALTH_DATA_LIST_ERROR_MESSAGE, new Object[]{deviceId, e});
        }
        return ok(result);
    }

    // interface
    public Result findByBraceletAndDate(String braceletId, String date) {
        ObjectNode result = Json.newObject();
        try {
            result.put(Constants.CODE, Constants.SUCCESS);
            result.put(Constants.DATAS, "");
        } catch (Exception e) {
            result.put(Constants.CODE, Constants.ERROR);
            result.put(Constants.MESSAGE, Messages.HEALTH_DATA_LIST_ERROR);
            logger.error(Messages.HEALTH_DATA_LIST_ERROR_MESSAGE, new Object[]{braceletId, e});
        }
        return ok(result);
    }

}
