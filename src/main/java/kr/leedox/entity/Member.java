package kr.leedox.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "members", uniqueConstraints = { @UniqueConstraint(name = "UQ_01", columnNames = { "email" }) })
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String username;
    private String password;
    private String regDate;

}
