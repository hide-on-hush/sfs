package dev.e23.dashstar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
//import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Data
@Table(name = "users")
public class User implements Serializable {

    @Id  // 表示这个字段是主键
    private Integer id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "nickname")
    private String nickname;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role = "user";

}