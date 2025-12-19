package com.example.uniCircle.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "User")

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name",nullable = false,length =255 )
    private String fullName;

    @Column(unique = true,nullable = false, length = 255)
    private String email;

    @Column(nullable = false,length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "bio",length = 255)
    private String bio;

    @Column(name = "created_At", updatable = false)
    private LocalDateTime createdAt;

    @Column(name ="created_By" ,length = 255)
    private String createdBy;

    @Column(name = "updated_At",updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_At_Time")
    private LocalDateTime updatedAtTime;

    @Column(name = "updated_By",length = 255)
    private String updatedBy;

    public enum Role{
        ADMIN,
        USER,
        MODERATOR
    }

    public User(){

    }
    public User(String fullName,String email,String password){
        this.fullName=fullName;
        this.email=email;
        this.password=password;
        this.role=Role.USER;
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
        this.updatedAtTime=LocalDateTime.now();
    }
    @PrePersist
    protected void onCreate(){
        LocalDateTime now=LocalDateTime.now();
        if(createdAt==null) createdAt =now;
        if(updatedAt==null) updatedAt=now;
        if(updatedAtTime==null) updatedAtTime=now;
        if (role==null) role=Role.USER;
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt=LocalDateTime.now();
        updatedAtTime=LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.singletonList( new SimpleGrantedAuthority(
                "ROLE_" +role.name()));
    }
    @Override
    public String getUsername(){
        return email;
    }
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }
    @Override
    public boolean isEnabled(){
        return true;
    }
    public Long getId(){
        return id;
    }
    public void setId(long id){
        this.id=id;
    }
    public String getFullName(){
        return fullName;
    }
    public void setFullName(String fullName){
        this.fullName=fullName;
    }
    public String getEmail(){
        return email;
    }
    public void  setEmail(String email){
        this.email=email;
    }
    @Override
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public Role getRole(){
        return role;
    }
    public void setRole(Role role){
        this.role=role;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public String getBio(){
        return bio;
    }
    public void setBio(String bio){
        this.bio=bio;
    }
    public void SetCreatedAt(LocalDateTime createdAt){
        this.createdAt=createdAt;
    }
    public String getCreatedBy(){
        return createdBy;
    }
    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt){
        this.updatedAt=updatedAt;
    }
    public LocalDateTime getUpdatedAtTime(){
        return updatedAtTime;
    }
    public void setUpdatedAtTime(LocalDateTime updatedAtTime){
        this.updatedAtTime=updatedAtTime;
    }
    public String getUpdatedBy(){
        return updatedBy;
    }
    public void  setUpdatedBy(String updatedBy){
        this.updatedBy=updatedBy;
    }

    public void setRoleFromString(String roleString){
        if(roleString !=null){
            try {
                this.role=Role.valueOf(roleString.toUpperCase());
            }
            catch (IllegalArgumentException e){
                this.role=Role.USER;
            }
        }
    }
    @Override
    public boolean equals(Object o){
        if (this==o) return true;
        if (o==null || getClass() !=o.getClass()) return false;

        User user=(User) o;
        return id !=null && id.equals(user.id);
    }
    @Override
    public int hashCode(){
        return id != null ? id.hashCode() : 0;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", bio='" + bio + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedAt=" + updatedAt +
                ", updatedAtTime=" + updatedAtTime +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
