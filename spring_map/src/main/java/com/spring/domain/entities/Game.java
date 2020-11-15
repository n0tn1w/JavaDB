package com.spring.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title",nullable = false,unique = true)
    private String title;
    @Column(name = "trailer",nullable = false)
    private String trailer;
    @Column(name = "image_thumbnail",nullable = false)
    private String imageThumbnail;
    @Column(name = "size",nullable = false)
    private double size;
    @Column(name = "price",nullable = false)
    private BigDecimal price;
    @Column(name = "description",nullable = false)
    private String description;
    @Column(name = "release_date",nullable = false)
    private LocalDate releaseDate;
    @ManyToMany(mappedBy = "games",fetch = FetchType.EAGER)
    private Set<User> users;
}
