package com.bookbook.booklink.common.config.security;

import com.bookbook.booklink.member.model.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Principal extends User {
    
    private String accessToken;
    
    public Principal(String username, String password,
                     Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
    
    public static Principal createPrincipal(Member member,
                                            List<SimpleGrantedAuthority> authorities){
        return new Principal(member.getEmail(), member.getPassword(), authorities);
    }
    
    public Optional<String> getAccessToken() {
        return Optional.of(accessToken);
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
