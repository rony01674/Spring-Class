package com.springthymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping(value = "/")
    public String displayIndex(){
        return "index";
    }
    @GetMapping(value = "/about")
    public String displayAbout(){
        return "about";
    }
    @GetMapping(value = "/team")
    public String displayTeam(){
        return "team";
    }
}
