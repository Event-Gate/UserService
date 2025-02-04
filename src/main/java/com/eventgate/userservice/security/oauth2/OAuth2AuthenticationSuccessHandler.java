package com.eventgate.userservice.security.oauth2;

import com.eventgate.userservice.dtos.UserResult;
import com.eventgate.userservice.security.jwt.JwtTokenProvider;
import com.eventgate.userservice.services.interfaces.UserService;
import com.eventgate.userservice.utils.KafkaUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component @RequiredArgsConstructor @Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${redirect.url}")
    private String redirectURL;
    private final JwtTokenProvider tokenProvider;
    private final KafkaUtils kafkaUtils;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");

            UserResult result = userService.processOAuth2User(oAuth2User);

            String token = tokenProvider.generateToken(
                    result.userResponse().id(),
                    email,
                    Collections.emptyList()
            );
            if (result.isNewUser()) {
                kafkaUtils.sendMessage(result.userResponse(), token);
            }
            log.error(token);
            String targetUrl = determineTargetUrl(request, response, authentication)
                    + "?token=" + token;

            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } catch (Exception ex) {
            log.error("Error during OAuth2 authentication success", ex);
            getRedirectStrategy().sendRedirect(request, response, redirectURL + "?error=true");
        }
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return redirectURL;
    }
}