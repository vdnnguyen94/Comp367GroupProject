package com.example.demo;

import java.time.LocalTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebAppController {
	
	@GetMapping("/")
	public String Index(Model model) {
        LocalTime now = LocalTime.now();
        String greeting;

        if (now.getHour() < 12) {
            greeting = "Good morning, Van Nguyen";
        } else if (now.getHour() < 18) {
            greeting = "Good afternoon, Van Nguyen";
        } else {
            greeting = "Good evening, Van Nguyen";
        }

        model.addAttribute("greetingMessage", greeting);
        return "index"; 
    }
		
}
