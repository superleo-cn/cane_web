package inteceptors;

import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import utils.TokenUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by superleo on 24/10/15.
 */
public class TokenInterceptor extends Action.Simple {

    @Override
    public F.Promise<Result> call(Http.Context ctx) throws Throwable {
        String deviceId = Form.form().bindFromRequest().get("deviceId");
        String token = ctx.request().getQueryString("ak");
        if (!TokenUtils.compareToken(deviceId, token)) {
            final ObjectNode result = Json.newObject();
            Map<String, String> maps = new HashMap<>();
            maps.put(Constants.CODE, "100001");
            maps.put(Constants.MESSAGE, "denied");
            result.replace("status", Json.toJson(maps));
            result.replace("data", Json.toJson(null));
            return F.Promise.<Result>pure(Results.ok(result));
        }
        return delegate.call(ctx);
    }

}