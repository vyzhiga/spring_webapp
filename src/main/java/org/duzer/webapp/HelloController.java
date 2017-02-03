package org.duzer.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.ModelMap;

@Controller
public class HelloController{

    private final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String printHello(ModelMap model) {

        logger.debug("Returned Hello string");

        return "redirect:books";

    }

    @RequestMapping(value="/books", method = RequestMethod.GET)
    public String viewBooks(ModelMap model) {

        logger.debug("Returned Hello string");

        return "books";

    }

}