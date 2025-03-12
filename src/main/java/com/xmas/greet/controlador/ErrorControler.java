package com.xmas.greet.controlador;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorControler implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == 404) {
                return "error-404"; 
            } else if (statusCode == 500) {
                return "error-500";
            } else if (statusCode == 403) {
                return "error-403"; 
            }
        }
        return "error";
    }
}
