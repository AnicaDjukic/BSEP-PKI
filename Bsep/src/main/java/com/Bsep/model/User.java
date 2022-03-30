package com.Bsep.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "mySeqGenUsers", sequenceName = "mySeqUsers", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenUsers")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", unique = false, nullable = false)
    private String password;

    @Column(name = "firstName", unique = false, nullable = false)
    private String firstName;

    @Column(name = "lastName", unique = false, nullable = false)
    private String lastName;

    @Column(name = "phoneNumber", unique = false, nullable = false)
    private String phoneNumber;

    @OneToOne(targetEntity = Role.class, cascade = CascadeType.MERGE)
    private Role role;


    public User() {
    }


    public User(Long id, String username, String password, String firstName, String lastName, String phoneNumber,
                Role userType) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = userType;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles;
    }
}
