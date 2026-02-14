package com.example.UniCricle.Auth;

import com.example.UniCricle.Repository.ClubMemberRepository;
import com.example.UniCricle.Repository.ClubRepository; // Add this
import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.model.ClubMember;
import com.example.UniCricle.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserAccountService userAccountService;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubRepository clubRepository; // Inject your ClubRepository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (userAccountService.isAccountLocked(user)) {
            throw new LockedException("Your account is locked for 3 hours.");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        // 1. Add main user role (e.g., ROLE_USER)
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // 2. Add roles from ClubMember table (Memberships)
        List<ClubMember> memberships = clubMemberRepository.findByUser(user);
        for (ClubMember cm : memberships) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + cm.getRole().name()));
        }

        // 3. NEW: Check if this user is a leader in the CLUB table
        // This ensures even if they aren't in 'ClubMember', they get Leader status
        boolean isManagingAnyClub = !clubRepository.findByLeaderId(user.getId()).isEmpty();
        if (isManagingAnyClub) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CLUB_LEADER"));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                !user.isAccountLocked(),
                true,
                true,
                true,
                authorities
        );
    }
}