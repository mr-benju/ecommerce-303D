package dev.rampmaster.ecommerce.users.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class JwtFilter extends GenericFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String header = req.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                Claims claims = JwtUtil.validateToken(token);
                req.setAttribute("role", claims.get("role"));
            } catch (Exception e) {
                ((HttpServletResponse) response).sendError(401, "Token inválido");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}