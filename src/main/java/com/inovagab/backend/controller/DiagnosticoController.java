package com.inovagab.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class DiagnosticoController {

    @GetMapping("/api/diagnostico")
    public String diagnostico(Authentication authentication) {

        if (authentication == null) {
            return "Authentication = null";
        }

        return "Usuario: " + authentication.getName()
                + " | Authorities: " + authentication.getAuthorities();
    }
}