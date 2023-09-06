package kr.leedox.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private int id;

    @Column(length = 50)
    private String name;

    @OneToMany(mappedBy = "role")
    private List<Authority> authorities;

    @Builder
    public Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;

        return id == role.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, authorities);
    }
}
