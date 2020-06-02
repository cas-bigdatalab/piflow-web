package cn.cnic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ReturnPageCtrl {

    @RequestMapping("/bootPage/index")
    public String index() {
        return "bootPage";
    }

}
