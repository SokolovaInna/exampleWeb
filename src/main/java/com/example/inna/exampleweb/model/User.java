package com.example.inna.exampleweb.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
   Long id;

  @Column(name = "email", unique = true, length = 64)
  String email;

  @Column(name = "password", length = 50)
  String password;

  @Column(name = "msisdn", length = 50)
  String msisdn;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  Set<Role> roleSet;

  public User() {
  }

  public User(String email, String password, String msisdn, Set<Role> roleSet) {
    this.email = email;
    this.password = password;
    this.msisdn = msisdn;
    this.roleSet = roleSet;
  }

  public User(Long id, String email, String password, String msisdn, Set<Role> roleSet) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.msisdn = msisdn;
    this.roleSet = roleSet;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getMsisdn() {
    return msisdn;
  }

  public void setMsisdn(String msisdn) {
    this.msisdn = msisdn;
  }

  public Set<Role> getRoleSet() {
    return roleSet;
  }

  public void setRoleSet(Set<Role> roleSet) {
    this.roleSet = roleSet;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(msisdn, user.msisdn) && Objects.equals(roleSet, user.roleSet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, password, msisdn, roleSet);
  }
}
