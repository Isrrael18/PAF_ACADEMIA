package com.academia.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {
    
	@ModelAttribute
    public void addAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<String> roles = auth.getAuthorities().stream()
                                  .map(GrantedAuthority::getAuthority)
                                  .collect(Collectors.toList());
        
        model.addAttribute("usuario", username);
        model.addAttribute("roles", roles);
    }
	
    @GetMapping("/carrera")
    public String carrera() {
        return "carrera";
    }
    
    @GetMapping("/curso")
    public String curso() {
        return "curso";
    }
    
    @GetMapping("/docente")
    public String docente() {
        return "docente";
    }
    
    @GetMapping("/alumno")
    public String alumno() {
        return "alumno";
    }
    
    @GetMapping("/docAsistencia")
    public String docAsistencia() {
        return "docAsistencia";
    }
    
    @GetMapping("/aluAsistencia")
    public String aluAsistencia() {
        return "aluAsistencia";
    }
    
    @GetMapping("/usuarios")
    public String usuarios() {
        return "usuarios";
    }
	@GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    
    @GetMapping("/acercaDe")
    public String acercaDe() {
        return "acercaDe";
    }
   
}
