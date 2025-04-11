package com.example.demo;

import java.time.LocalTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import io.github.cdimascio.dotenv.Dotenv;

@Controller
public class WebAppController {
	
	@GetMapping("/")
	public String Index(Model model) {
        LocalTime now = LocalTime.now();
        String greeting;
        Dotenv dotenv = Dotenv.load(); 
        String name = dotenv.get("STUDENT1_NAME", "Van Nguyen DEFAULT");
        String id = dotenv.get("STUDENT1_ID", "301289600 DEFAULT");
        String student2Name = dotenv.get("STUDENT2_NAME", "Seyeon Jo");
        String stage = dotenv.get("STAGE", "DEVELOPMENT");
        String port = System.getenv().getOrDefault("APP_PORT", "5173");
        if (now.getHour() < 12) {
            greeting = "Good morning, " + name;
        } else if (now.getHour() < 18) {
            greeting = "Good afternoon, " + name;
        } else {
            greeting = "Good evening, " + name;
        }

        model.addAttribute("greetingMessage", greeting);
        model.addAttribute("studentName", name); 
        model.addAttribute("studentId", id);     
        model.addAttribute("student2Name", student2Name);
        model.addAttribute("stage", stage);
        model.addAttribute("port", port);
        return "index2"; 
    }
		
}
