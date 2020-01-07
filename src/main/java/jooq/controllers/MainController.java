package jooq.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/orgss")
    public String orgss() {
        return "orgss";
    }

    @RequestMapping("/tree1")
    public String tree1() {
        return "tree1";
    }

    @RequestMapping("/tree2")
    public String tree2() { return "tree2"; }

    @RequestMapping("/item1")
    public String item1() {
        return "item1";
    }
}
