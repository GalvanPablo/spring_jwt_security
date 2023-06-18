package com.galvan.security.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name="_user")
public class User implements UserDetails { // Se implementa UserDetails para que Spring Security pueda obtener los datos del usuario
    
    @Id
    @GeneratedValue
    private Integer id;

    private String nombre;

    private String apellido;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // Retorna la lista de roles del usuario
        // Al solo tener un rol, se retorna una lista con un solo rol
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email; // Se retorna el email del usuario
    }

    @Override
    public String getPassword() {
        return password; // Se retorna la contraseña del usuario
    }

    @Override
    public boolean isAccountNonExpired() {
        // true -> la cuenta no ha expirado
        // false -> la cuenta ha expirado
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // true -> la cuenta no está bloqueada
        // false -> la cuenta está bloqueada
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // true -> las credenciales no han expirado
        // false -> las credenciales han expirado
        return true;
    }

    @Override
    public boolean isEnabled() {
        // true -> la cuenta está habilitada
        // false -> la cuenta está deshabilitada
        return true;
    }

}
