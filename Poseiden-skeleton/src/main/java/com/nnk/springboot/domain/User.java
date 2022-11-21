package com.nnk.springboot.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.nnk.springboot.security.AuthProvider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "FullName is mandatory")
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    private String role;

    @Enumerated(EnumType.STRING)
    private AuthProvider authenticationProvider;

    private Integer idProvider;

    public User(int id, String username, String email, String password, String fullname, String userRole) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.role = userRole;
    }

    public User(String username, String email, String fullname, String role, AuthProvider provider,
            Integer idProvider) {
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.role = role;
        this.authenticationProvider = provider;
        this.idProvider = idProvider;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
                + ", fullname=" + fullname + ", role=" + role + ", authenticationProvider=" + authenticationProvider
                + ", idProvider=" + idProvider + "]";
    }

}
