package com.sms.student_management.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {
    @GetMapping({"/login", "/signup", "/dashboard", "/dashboard/**"})
    public String forwardSpaRoutes() {
        return "forward:/index.html";
    }
}
