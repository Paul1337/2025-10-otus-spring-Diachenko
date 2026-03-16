package ru.otus.hw.hw13.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw13.repositories.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
        return new AppUserDetails(user.getId(), user.getUsername(), user.getPasswordHash(), authorities);
    }
}
