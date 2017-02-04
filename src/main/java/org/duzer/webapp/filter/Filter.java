package org.duzer.webapp.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Filter implements javax.servlet.Filter {

    //logging init
    final static Logger logger = LoggerFactory.getLogger(Filter.class);

    public void init(FilterConfig arg0) throws ServletException {}

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        HttpSession session = request.getSession(true);

        // проверяем и устанавливаем параметр сессии - столбец по которому сортируется вывод книг, по умолчанию - автор
        String param = (String) session.getAttribute("sesCurOrder");
        if (param == null) {
            session.setAttribute("sesCurOrder", "BookAuthor");
            logger.debug("Filter: null session parameter sesCurOrder in user-session. Set it to 'BookAuthor'");
            logger.debug("Filter: Test: sesCurOrder is "+((String) session.getAttribute("sesCurOrder")));
        } else {
            logger.debug("Filter: sesCurOrder is "+((String) session.getAttribute("sesCurOrder")));
        }

        // проверяем и устанавливаем параметр сессии - порядок отображения столбца авторов или наименований
        param = (String) session.getAttribute("sesOrder");
        if (param == null) {
            session.setAttribute("sesOrder", "ASC");
            logger.debug("Filter: null session parameter sesOrder in user-session. Set it to 'ASC'");
        } else {
            logger.debug("Filter: sesOrder is "+((String) session.getAttribute("sesOrder")));
        }

        // передаем дальше
        chain.doFilter(req, res);
    }

    public void destroy() {}

}
