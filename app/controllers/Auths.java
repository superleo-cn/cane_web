package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import constants.Messages;
import models.Crane;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import utils.TokenUtils;

public class Auths extends Basic {

    final static Logger logger = LoggerFactory.getLogger(Auths.class);

    public Result loginJson() {
        ObjectNode result = Json.newObject();
        String deviceId = null;
        try {
            deviceId = Form.form().bindFromRequest().get("device_id");
            if (StringUtils.isNotEmpty(deviceId)) {
                Crane data = Crane.findCraneById(deviceId);
                if (data != null) {
                    String token = TokenUtils.getToken(deviceId);
                    logger.info("Token====>" + token);
                    result.put(Constants.STATUS, Constants.SUCCESS);
                    result.put(Constants.TOKEN, token);
                } else {
                    result.put(Constants.STATUS, Constants.FAILURE);
                }
            } else {
                result.put(Constants.STATUS, Constants.FAILURE);
            }
        } catch (Exception e) {
            result.put(Constants.STATUS, Constants.ERROR);
            logger.error(Messages.LOGIN_ERROR_MESSAGE, new Object[]{deviceId, e});
        }
        return ok(result);
    }


}
