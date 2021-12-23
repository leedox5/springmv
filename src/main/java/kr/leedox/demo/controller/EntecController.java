package kr.leedox.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class EntecController {
    @RequestMapping("home")
    public ModelAndView home(@RequestParam("name")String name) {
        ModelAndView mv =new ModelAndView();
        mv.addObject("name", name);
        mv.setViewName("hello");
        return mv;
    }
}
