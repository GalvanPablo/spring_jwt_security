package com.galvan.security.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // El Jwt Token se envía en el header de la petición
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // Bearer es un tipo de token que se usa para
                                                                       // autenticar a un usuario
            // Si el header no existe o no empieza con Bearer
            filterChain.doFilter(request, response);
            return;
        }

        // Se obtiene el token
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt); // Es indiferente que se obtenga el email o el username, ya que en este caso son lo mismo (simplemnte se usa el email como username)

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); // Se obtienen los datos del usuario desde la base de datos

            if (jwtService.isTokenValid(jwt, userDetails)) { // Se verifica que el token sea válido
                String userRole = jwtService.extractUserRole(jwt); // Método para extraer el rol del token JWT
                if(userRole != null){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContext context = SecurityContextHolder.createEmptyContext();

                    userRole = "ROLE_" + userRole.toUpperCase();
                    Authentication authentication = new TestingAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userRole);
                    context.setAuthentication(authentication);

                    SecurityContextHolder.setContext(context);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}
