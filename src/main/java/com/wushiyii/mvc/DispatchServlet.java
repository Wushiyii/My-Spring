package com.wushiyii.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author wgq
 * @date 2020/4/4 8:05 下午
 */
public class DispatchServlet extends HttpServlet {

    private ControllerHandler controllerHandler = new ControllerHandler();

    private ResultRender resultRender = new ResultRender();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String requestMethod = req.getMethod();
        String requestPath = req.getPathInfo();
        if (requestPath.endsWith("/")) {
            requestPath = requestPath.substring(0, requestPath.length() - 1);
        }
        ControllerInfo controllerInfo = controllerHandler.getControllerInfo(requestMethod, requestPath);
        if (Objects.isNull(controllerInfo)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resultRender.invokeController(controllerInfo, req, resp);
    }
}
