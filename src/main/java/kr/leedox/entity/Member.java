package kr.leedox.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "members", uniqueConstraints = { @UniqueConstraint(name = "UQ_01", columnNames = { "email" }) })
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Setter
    private String email;
    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private String regDate;
    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Authority> authorities = new ArrayList<>();

    public void setAuthorities(List<Authority> authorities) {
        this.authorities.addAll(authorities);
    }

    @Transient
    @Setter
    private List<Role> roles;

    @Builder
    public Member(String email, String username) {
        this.email = email;
        this.username = username;
    }

    /*
    public List<Role> getRoles() {
        List<Role> userRoles = new ArrayList<>();
        for(Authority authority : getAuthorities()) {
            userRoles.add(authority.getRole());
        }
        return userRoles;
    }
    */
    @Override
    public String toString() {
        return username;
    }
}
