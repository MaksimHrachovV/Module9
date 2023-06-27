package goit;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@WebServlet("/time")
public class Thymeleaf extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("E:\\ProGOIT\\ServletThymeleafCookes\\templates\\");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TimeZoneUtil timeZoneUtil = new TimeZoneUtil();

        ZoneId zoneId = ZoneId.of(timeZoneUtil.parseTimeZone(req));

        String CookieZone= String.valueOf(zoneId);

        Cookie[] cookies = req.getCookies();
        if (cookies == null){
            System.out.println("Cookie is NULL, Save Cookie "+CookieZone);
            System.out.println("----------------------------------------------------");
            resp.addCookie(new Cookie("lastTimezone", CookieZone));

        }if(cookies != null && CookieZone.equals("UTC")){
            for (Cookie cookie : cookies) {
                CookieZone=cookie.getValue();
                System.out.println("Actual=UTC but Cookie equals "+CookieZone);
                System.out.println("----------------------------------------------------");
            }

        }if (cookies != null && !CookieZone.equals("UTC")){
            System.out.println("Cookie Not NULL, Save new Cookie "+CookieZone);
            System.out.println("----------------------------------------------------");
            resp.addCookie(new Cookie("lastTimezone", CookieZone));

        }

        Clock clock = Clock.system(zoneId);
        String timeFormat = LocalDateTime.now(clock).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss "));
        resp.setContentType("text/html; charset=utf-8");

        Context simpleContext = new Context();
        simpleContext.setVariable("Time",timeFormat);

        simpleContext.setVariable("Zone", CookieZone);

        engine.process("test", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
