package com.example.uniCircle.model;

import jakarta.persistence.*;
import org.apache.catalina.Role;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "email",nullable = false,unique = true,length = 255)
    private String email;

    @Column(name = "password",nullable = false,length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false,columnDefinition = "Enum('SUPER_ADMIN','ADMIN','USER' )")
    private Role role;

    @Column(name = "bio",length = 255)
    private String bio;

    @Column(name = "created_at",updatable = false,columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @Column(name = "created_by",length = 255)
    private String createdBy;

    @Column(name = "enabled",nullable = false,
    columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean enabled=false;

    @Column(name = "account_non_locked", nullable = false,
    columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean accountNonLocked=true;

    @Column(name = "failed_login_attempts",nullable = false,
    columnDefinition = "INT DEFAULT 0")
    private int failedLoginAttempts=0;

    @Column(name = "email_verified", nullable = false,
    columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean emailVerified=false;

    @Column(name = "email_verification_token",length = 100)
    private String emailVerificationToken;

    @Column(name = "verification_token_expiry",columnDefinition = "TIMESTAMP")
    private LocalDateTime verificationTokenExpiry;

    public enum Role{
        SUPER_ADMIN,ADMIN,USER,CLUB_LEADER
    }
    public User(){

    }
    public User(String fullName,String email,String password,Role role,
                String createdBy){
        this.fullName=fullName;
        this.email=email.toLowerCase();
        this.password=password;
        this.role=role !=null ? role : Role.USER;
        this.createdBy=createdBy;
    }
    @PrePersist
    protected void onCreate(){
        if(createdAt==null){
            createdAt=LocalDateTime.now();
        }
        if(updatedAt==null){
            updatedAt=LocalDateTime.now();
        }
        if(email !=null){
            email=email.toLowerCase();
        }
        if(emailVerificationToken==null && !emailVerified){
            generateEmailVerificationToken();
        }
    }
    @PreUpdate
    protected void onUpdate(){
        updatedAt=LocalDateTime.now();
        if(email !=null){
            email=email.toLowerCase();
        }
    }
    public void generateEmailVerificationToken(){
        this.emailVerificationToken=java.util.UUID.randomUUID().toString();
        this.verificationTokenExpiry=LocalDateTime.now().plusHours(12);
    }
    public boolean verifyEmail(String token){
        if(emailVerified)
            return true;

        if (emailVerificationToken ==null || verificationTokenExpiry ==null){
            return false;
        }
        if(verificationTokenExpiry.isBefore(LocalDateTime.now())){
            return false;
        }
        if(emailVerificationToken.equals(token)){
            this.emailVerified=true;
            this.enabled=true;
            this.emailVerificationToken=null;
            this.verificationTokenExpiry=null;

            return true;
        }
        return false;
    }
    public void recordFailedLoginAttempt(){
        this.failedLoginAttempts++;

        if(this.failedLoginAttempts>=5){
            this.accountNonLocked=false;
        }
    }
    public void recordSuccessfulLogin(){
        this.failedLoginAttempts=0;
        this.accountNonLocked=true;
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
    public void setEmail(String email){
        this.email=email !=null ? email.toLowerCase() : null;
    }
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
    public String getBio(){
        return bio;
    }
    public void setBio(String bio){
        this.bio=bio;
    }
    public boolean isEnabled(){
        return enabled;
    }
    public void setEnabled(boolean enabled){
        this.enabled=enabled;
    }
    public boolean isAccountNonLocked(){
        return accountNonLocked;
    }
    public void setAccountNonLocked(boolean accountNonLocked){
        this.accountNonLocked=accountNonLocked;
    }
    public int getFailedLoginAttempts(){
        return failedLoginAttempts;
    }
    public void setFailedLoginAttempts(int failedLoginAttempts){
        this.failedLoginAttempts=failedLoginAttempts;
    }
    public boolean isEmailVerified(){
        return emailVerified;
    }
    public  void setEmailVerified(boolean emailVerified){
        this.emailVerified=emailVerified;
    }
    public String getEmailVerificationToken(){
        return emailVerificationToken;
    }
    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }
    public LocalDateTime getVerificationTokenExpiry(){
        return verificationTokenExpiry;
    }
    public void setVerificationTokenExpiry(LocalDateTime verificationTokenExpiry) {
        this.verificationTokenExpiry = verificationTokenExpiry;
    }
    public LocalDateTime getCreatedAt(){
        return  createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt=createdAt;
    }
    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt){
        this.updatedAt=updatedAt;
    }
    public String getCreatedBy(){
        return createdBy;
    }
    public void setCreatedBy(String createdBy){
        this.createdBy=createdBy;
    }
    public String getUpdatedBy(){
        return updatedBy;
    }
    public void setUpdatedBy(String updatedBy){
        this.updatedBy=updatedBy;
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
                ", updatedAt=" + updatedAt +
                ", updatedBy='" + updatedBy + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", enabled=" + enabled +
                ", accountNonLocked=" + accountNonLocked +
                ", failedLoginAttempts=" + failedLoginAttempts +
                ", emailVerified=" + emailVerified +
                ", emailVerificationToken='" + emailVerificationToken + '\'' +
                ", verificationTokenExpiry=" + verificationTokenExpiry +
                '}';
    }
}
