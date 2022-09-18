package kr.leedox.entity;

<<<<<<< HEAD
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 50)
    private String userId;

    @Column(nullable = false, length = 100)
    private String name;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
=======
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
    private String userId;
    private String name;
    private String password;

>>>>>>> fa9d3e5c47e552a7b04961f1c8a03284c770f892
}
