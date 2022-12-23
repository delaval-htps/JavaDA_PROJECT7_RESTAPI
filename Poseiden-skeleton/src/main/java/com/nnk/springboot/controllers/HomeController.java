package com.nnk.springboot.controllers;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to manage home page for user in function of his authorities
 */
@Controller
public class HomeController {
	
	/**
	 * endpoint to show the home page for user in function of his authorities
	 * 
	 * @param model
	 * @param authentication
	 * @return view admin/home if his ADMIN or user/home if he is USER
	 */
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

	/**
	 * endpoint to redirect to home page for USER
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/user/home")
	public String userHome(Model model) {
		return "redirect:/bidList/list";
	}

	/**
	 * endpoint ton redirect to home page for ADMIN
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin/home")
	public String adminHome(Model model) {
		return "redirect:/bidList/list";
	}

}
