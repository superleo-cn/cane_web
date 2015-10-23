package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Controller;
import play.mvc.Result;

public class Basic extends Controller {

    final static Logger logger = LoggerFactory.getLogger(Basic.class);

    public Result defaults() {
        return ok("Works!");
    }

}
