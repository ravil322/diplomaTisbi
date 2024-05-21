package ru.ravnasybullin.DoiReg.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ravnasybullin.DoiReg.PageConstants.PageConstants;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;

@Controller
public class LoginController {

    @Resource
    private AuthenticationManager authManager;

    @Value("${server.servlet.session.cookie.path:/}")
    private String cookiePath;

    @GetMapping(PageConstants.LOGIN_PAGE)
    public String getLoginPage() {
        return PageConstants.LOGIN_TEMPLATE_NAME;
    }

    @PostMapping(PageConstants.LOGIN_PAGE)
    public String authorize(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());

        try {
            authentication = authManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            return "redirect:" + PageConstants.LOGIN_ERROR_PAGE;
        }
        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            return "redirect:" + PageConstants.VIEW_ARTICLES_PAGE;
        } else {
            return "redirect:" + PageConstants.LOGIN_ERROR_PAGE;
        }
    }

    @RequestMapping(value = "/views/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:" + PageConstants.LOGIN_PAGE + "?logout";
    }
}
