package com.galvan.security.demo;

import org.springframework.http.ResponseEntity;

import com.galvan.security.user.Role;
import com.galvan.security.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {
    
    @GetMapping
    public ResponseEntity<User> decirHola(){
        return ResponseEntity.ok(new User(1, "Pepito", "Perez", "ejemplo@correo.com", "1234", Role.USER));
    }

}
