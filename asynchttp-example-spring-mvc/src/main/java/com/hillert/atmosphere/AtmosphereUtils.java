package com.hillert.atmosphere;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereServlet;
import org.springframework.util.Assert;

public final class AtmosphereUtils {


       public static AtmosphereResource<HttpServletRequest, HttpServletResponse> getAtmosphereResource(HttpServletRequest request) {
           AtmosphereResource<HttpServletRequest, HttpServletResponse> resource = (AtmosphereResource<HttpServletRequest, HttpServletResponse>) request.getAttribute(AtmosphereServlet.ATMOSPHERE_RESOURCE);
           Assert.notNull(resource, "AtmosphereResource could not be located for the request. Check that AtmosphereServlet is configured correctly in web.xml");
           return resource;
       }

}
