package com.example.inna.exampleweb.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "todo")
public class Todo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created", nullable = false)
  private Date created;

  @Column(name = "date_of_completion")
  private Date dateOfCompletion;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "comments", length = 4000)
  private String comments;

  public Todo() {
  }

  public Todo(User user, String comments) {
    this.user = user;
    this.comments = comments;
  }

  public Todo(Date created, User user, String comments) {
    this.created = created;
    this.user = user;
    this.comments = comments;
  }

  public Todo(Long id, Date created, Date dateOfCompletion, User user, String comments) {
    this.id = id;
    this.created = created;
    this.dateOfCompletion = dateOfCompletion;
    this.user = user;
    this.comments = comments;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getDateOfCompletion() {
    return dateOfCompletion;
  }

  public void setDateOfCompletion(Date dateOfCompletion) {
    this.dateOfCompletion = dateOfCompletion;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Todo todo = (Todo) o;
    return Objects.equals(id, todo.id) && Objects.equals(created, todo.created) && Objects.equals(dateOfCompletion, todo.dateOfCompletion) && Objects.equals(user, todo.user) && Objects.equals(comments, todo.comments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, created, dateOfCompletion, user, comments);
  }
}
