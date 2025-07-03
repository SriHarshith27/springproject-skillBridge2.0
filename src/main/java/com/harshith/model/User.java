package com.harshith.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "enrolledCourses")
@ToString(exclude = "enrolledCourses")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role = "USER";

    @Column(nullable = true)
    private String phone;

    @ManyToMany(mappedBy = "enrolledUsers", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Course> enrolledCourses = new HashSet<>();

    public User(Long id, String username, String password, String email, String role, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phone = phone;
    }

    // --- UserDetails Methods Implementation ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security expects roles to start with "ROLE_"
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    // Note: getUsername() is provided by Lombok's @Data

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
}
