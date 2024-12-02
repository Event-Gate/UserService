package com.eventgate.userservice.security.oauth2;

import com.eventgate.userservice.entities.Customer;
import com.eventgate.userservice.repositories.CustomerRepository;
import com.eventgate.userservice.security.jwt.JwtTokenProvider;
import com.eventgate.userservice.services.implementations.CustomerDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component @RequiredArgsConstructor @Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${redirect.url}")
    private String redirectURL;
    private final JwtTokenProvider tokenProvider;
    private final CustomerRepository customerRepository;
    private final CustomerDetailsServiceImpl customerDetailsServiceImpl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("given_name") + " " + oAuth2User.getAttribute("family_name");

        Customer customer = customerRepository.findByEmail(email)
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setEmail(email);
                    newCustomer.setFullName(fullName);
                    newCustomer.setLastLogin(LocalDateTime.now());
                    return customerRepository.save(newCustomer);
                });

        UserDetails userDetails = customerDetailsServiceImpl.loadUserByUsername(email);
        String token = tokenProvider.generateToken(customer.getId(), email, userDetails.getAuthorities());
        log.error(token);

        String url = determineTargetUrl(request, response, authentication);
        String targetUrl = url + "?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return redirectURL;
    }
}