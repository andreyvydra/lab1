package itmo.is.lab1.controllers;

import itmo.is.lab1.services.user.requests.UserSignInRequest;
import itmo.is.lab1.services.user.requests.UserSignUpRequest;
import itmo.is.lab1.services.user.responses.JwtAuthenticationResponse;
import itmo.is.lab1.services.jwt.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid UserSignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/signin")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid UserSignInRequest request) {
        return authenticationService.signIn(request);
    }
}
