package com.galvan.security.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin-controller")
public class AdminController {

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> decirHolaSeguroUser(){
        return ResponseEntity.ok("Hola, soy un endpoint de usuario");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> decirHolaSeguroAdmin(){
        return ResponseEntity.ok("Hola, soy un endpoint de administrador");
    }
    
}
