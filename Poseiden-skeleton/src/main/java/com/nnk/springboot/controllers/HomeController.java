package com.nnk.springboot.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {


	@RequestMapping("/")
	public String home(Model model, org.springframework.security.core.Authentication authentication) {

		if (authentication != null) {

			if (authentication instanceof UsernamePasswordAuthenticationToken) {
				if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
					return "redirect:/admin/home";
				} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("USER"))) {
					return "redirect:/user/home";
				}
			 }else if(authentication instanceof OAuth2AuthenticationToken) {
				OAuth2AuthenticationToken token =(OAuth2AuthenticationToken) authentication;
				System.out.println(token.getAuthorizedClientRegistrationId());
				System.out.println(token.getName());
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
