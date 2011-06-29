/**
 *
 */
package com.hillert.atmosphere;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereResource;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author ghillert
 *
 */
public class AtmosphereResourceArgumentResolver implements WebArgumentResolver {

    /**
     *
     */
    public AtmosphereResourceArgumentResolver() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.springframework.web.bind.support.WebArgumentResolver#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.context.request.NativeWebRequest)
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

        if (AtmosphereResource.class.isAssignableFrom(methodParameter.getParameterType())) {
            return AtmosphereUtils.getAtmosphereResource(webRequest.getNativeRequest(HttpServletRequest.class));
        } else {
            return WebArgumentResolver.UNRESOLVED;
        }

    }

}
