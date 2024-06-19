//https://www.baeldung.com/manually-set-user-authentication-spring-security
	
package xyz.magicraft.longshort.ssf.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import jakarta.servlet.http.HttpSession;

public class SecurityUtil {

	public static void login(String principle, List<String> roles, HttpSession session) {
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}

        Authentication auth = new UsernamePasswordAuthenticationToken(principle, "^*%r80eru8rqreqr", authorities);
        
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

		
	} 
	
	public static void logout(HttpSession session) {

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(null);
        
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

		
	} 
}
