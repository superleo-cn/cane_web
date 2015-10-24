package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import utils.TokenUtils;

import java.util.HashMap;
import java.util.Map;

public class Basic extends Controller {

    final static Logger logger = LoggerFactory.getLogger(Basic.class);

    public Result defaults() {
        return ok("Works!");
    }

    public Result isValidToken(String deviceId) {
        String token = request().getQueryString("ak");
        if (!TokenUtils.compareToken(deviceId, token)) {
            final ObjectNode result = Json.newObject();
            Map<String, String> maps = new HashMap<>();
            maps.put(Constants.CODE, "100001");
            maps.put(Constants.MESSAGE, "denied");
            result.replace("status", Json.toJson(maps));
            result.replace("data", Json.toJson(null));
            return Results.ok(result);
        }
        return null;
    }

}
