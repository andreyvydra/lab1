package itmo.is.lab1.services.jwt;

import itmo.is.lab1.models.user.enums.Role;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.services.user.requests.UserSignInRequest;
import itmo.is.lab1.services.user.requests.UserSignUpRequest;
import itmo.is.lab1.services.user.responses.JwtAuthenticationResponse;
import itmo.is.lab1.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(UserSignUpRequest request) {

        var user = new User()
                .setUsername(request.getUsername())
                .setPassword(passwordEncoder.encode(request.getPassword()))
                .setRole(Role.ROLE_USER);

        try {
            userService.create(user);
        } catch (Exception e) {
            return new JwtAuthenticationResponse("", e.getMessage());
        }

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt, "OK");
    }

    public JwtAuthenticationResponse signIn(UserSignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .getByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt, "OK");
    }
}
