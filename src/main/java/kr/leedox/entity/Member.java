package kr.leedox.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "members")
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
