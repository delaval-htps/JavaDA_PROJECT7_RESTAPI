package com.nnk.springboot.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Custom Oauht2 user redefine a Oauth2user for the specific authentication with github provider.
 */
public class CustomOAuth2User implements OAuth2User {

	private Set<GrantedAuthority> authorities = new LinkedHashSet<>();
	private Map<String, Object> attributes;
	private String name;
	private String username;
	private String email;
	private String fullname;
	private AuthProvider clientProvider;
	private Integer providerId;

	public CustomOAuth2User(OAuth2User user, Set<GrantedAuthority> authorities, String clientProvider,
			String username) {

		// login of github's oAuth2User is the username of user in github
		this.username = user.getAttribute("login");
		this.email = user.getAttribute("email");
		this.fullname = user.getAttribute("name");
		this.clientProvider = AuthProvider.valueOf(clientProvider.toUpperCase());
		this.providerId = user.getAttribute("id");

		this.authorities = authorities;
		this.attributes = user.getAttributes();
		this.name = username;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public Map<String, Object> getAttributes() {
		if (attributes == null) {
			this.attributes = new HashMap<>();
			this.attributes.put("username", this.getUsername());
			this.attributes.put("email", this.getEmail());
			this.attributes.put("fullname", this.getFullname());
			this.attributes.put("clientProvider", this.getClientProvider());
			this.attributes.put("providerId", this.getproviderId());
		}
		return attributes;
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @return the fullname
	 */
	public String getFullname() {
		return this.fullname;
	}

	/**
	 * @return the clientProvider
	 */
	public AuthProvider getClientProvider() {
		return this.clientProvider;
	}

	/**
	 * @return the idFromProvider
	 */
	public Integer getproviderId() {
		return this.providerId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return "CustomOAuth2User [ username=" + username
				+ ", email=" + email + ", fullname=" + fullname + ", clientProvider=" + clientProvider.toString()
				+ ", providerId="
				+ providerId + " ,authorities=" + authorities + ", attributes=" + attributes + "]";
	}

}