package com.fundoonotes.fundoo_notes.filter;
import com.fundoonotes.fundoo_notes.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;


// This class is a custom security filter..
@Component  // Spring Bean banao
@Slf4j      // Logging ke liye
public class JwtFilter extends OncePerRequestFilter {      // Har request pe filter sirf ek baar chalega
    @Autowired
    private JwtUtil jwtUtil;       // inject jwtUtil object..

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,     // incoming client request.. --> Header, Token , URL check
            HttpServletResponse response,  //  client ko response bhejna
            FilterChain filterChain)      // forward request to next filter or controller..
            throws ServletException, IOException {
        // Extract the token..from authrization header..
        String authHeader = request.getHeader("Authorization");
        log.debug("Auth header: {}", authHeader);

        String token = null;
        Long userId = null;

        //  Check the format of Bearer Token..
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // "Bearer " ke baad ka part token hai
            token = authHeader.substring(7);
            log.debug("Token extracted: {}", token);

            // Token validate kiya..
            if (jwtUtil.validateToken(token)) {
                // Token se userId nikali..
                userId = jwtUtil.getUserIdFromToken(token);
                log.debug("UserId from token: {}", userId);
                //UsernamePasswordAuthenticationToken  tells spring security this class is authenticated
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userId,   // principal = userId
                                null,     // credentials = null  (no need of password, bcz user is already authenticated )
                                new ArrayList<>() // authorities = empty  (we do not have role based authorization) user, admin
                        );
                // SecurityContextHolder -> current user storage  or global security storage
                //authentication -> Logged in user info..
                //context -> get the security data of current request..
                SecurityContextHolder.getContext().setAuthentication(authentication);  //It stores authentication information of the currently authenticated user.
                log.debug("Authentication set for userId: {}", userId);
            } else {
                log.warn("Invalid JWT token!");
            }
        }
        // Aage jayega controller pr.. controller execute hoga..
        filterChain.doFilter(request, response);    // internally --> currentFilter.doFilter();,  nextFilter.doFilter();, controller.execute();
    }
}