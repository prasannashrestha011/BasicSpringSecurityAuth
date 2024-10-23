package application.source.backend.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppControllers {
    @GetMapping("/home")
    public String getMethodName() {
        return "home page ";
    }
}
