package ru.otus.hw.hw13.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String fullName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Author(String fullName, User user) {
        this.id = 0;
        this.fullName = fullName;
        this.user = user;
    }

    public Author(String fullName) {
        this(fullName, null);
    }
}
