package com.saif.logerroranalyzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebViewController {

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("pageTitle", "Log Error Analyzer");
        return "index";
    }

    @GetMapping("/error-codes")
    public String errorCodes(Model model){
        model.addAttribute("pageTitle","Error Code Management");
        return "error-codes";
    }

    @GetMapping("/reports")
    public String reports(Model model){
        model.addAttribute("pageTitle","Analysis Reports");
        return "reports";
    }
}
