package com.hillert.atmosphere;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.util.XSSHtmlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String home() {
        logger.info("Welcome home!");
        return "home";
    }

    /**
     * Simply selects the home view to render by returning its name.
     *
     * FIXME: it would be nice to inject the AtmosphereResource through Spring e.g.
     *
     * public void websockets(AtmosphereResource<HttpServletRequest,HttpServletResponse> event) {...}
     *
     */
    @RequestMapping(value="/websockets", method=RequestMethod.GET)
    @ResponseBody
    public void websockets(HttpServletRequest request) {

        final AtmosphereResource<HttpServletRequest,HttpServletResponse> event = getAtmosphereResource(request);

        final HttpServletRequest req = event.getRequest();
        final HttpServletResponse res = event.getResponse();

        event.suspend();

        final Broadcaster bc = event.getBroadcaster();
        bc.getBroadcasterConfig().addFilter(new XSSHtmlFilter());

        Future<String> f =	bc.broadcast( event.getAtmosphereConfig().getWebServerName()
        + "**has suspended a connection from " + req.getRemoteAddr());

        try { // Wait for the push to occurs. // This block the current Thread
            f.get();
        } catch (Throwable t) {

        }

        bc.scheduleFixedBroadcast(req.getRemoteAddr()
              + "**is connected", 10, TimeUnit.SECONDS); bc.delayBroadcast("Underlying Response now suspended");

    }


    /**
    *
    * @param request
    * @return
    */
   @SuppressWarnings("unchecked")
   private AtmosphereResource<HttpServletRequest, HttpServletResponse> getAtmosphereResource(HttpServletRequest request) {
       AtmosphereResource<HttpServletRequest, HttpServletResponse> resource = (AtmosphereResource<HttpServletRequest, HttpServletResponse>) request.getAttribute(AtmosphereServlet.ATMOSPHERE_RESOURCE);
       Assert.notNull(resource, "AtmosphereResource could not be located for the request. Check that AtmosphereServlet is configured correctly in web.xml");
       return resource;
   }



}

