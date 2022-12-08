package com.nnk.springboot.controllers;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String home(Model model, org.springframework.security.core.Authentication authentication) {
		if (authentication != null) {
			if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
				return "redirect:/admin/home";
			}
			if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("USER"))) {
				return "redirect:/user/home";
			}
		}
		return "home";
	}

	@RequestMapping("/user/home")
	public String userHome(Model model) {
		return "redirect:/bidList/list";
	}

	@RequestMapping("/admin/home")
	public String adminHome(Model model) {
		return "redirect:/bidList/list";
	}

}
