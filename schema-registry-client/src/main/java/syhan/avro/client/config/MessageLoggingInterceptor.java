package syhan.avro.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public class MessageLoggingInterceptor extends HandlerInterceptorAdapter {
    //
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpServletRequest cachedRequest = new ContentCachingRequestWrapper(request);
        logger.info(getRequestPayload(cachedRequest));
        return true;
    }

    private String getRequestPayload(HttpServletRequest request) {
        //ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        //System.out.println("wrapper:"+wrapper);
        if (request != null) {
            byte[] buf = ((ContentCachingRequestWrapper)request).getContentAsByteArray();
            System.out.println("buf:"+buf.length);
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, ((ContentCachingRequestWrapper)request).getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return "[unknown]";
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("poseHandle........................................................................");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("afterCompletion........................................................................");
    }
}
