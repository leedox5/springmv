package kr.leedox.service;

import kr.leedox.UserRole;
import kr.leedox.entity.Authority;
import kr.leedox.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemberAdapter implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Member member;

    public MemberAdapter(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // [2023.09.04] authority table에서 가져오기
        for (Authority authority : this.member.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority((authority.getRole().getName())));
        }

        /* ---
        if("leedox@naver.com".equals(this.member.getEmail())) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        --- */

        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Member getMember() {
        return this.member;
    }
}
