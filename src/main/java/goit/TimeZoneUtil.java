package goit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.time.ZonedDateTime;

public class TimeZoneUtil {
    public String parseTimeZone(HttpServletRequest request) {

        if (request.getParameterMap().containsKey("timezone")) {
            return (request.getParameter("timezone").replace(" ", "+").length() < 1) ?
                    "UTC" : request.getParameter("timezone").replace(" ", "+").toUpperCase();
        }
        return "UTC" ;
    }
}

