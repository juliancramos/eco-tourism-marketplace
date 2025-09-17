package com.marketplace.userprofileservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 320, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "img_url", length = 500)
    private String imgUrl;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "USER_ROLE",
        joinColumns = @JoinColumn(name = "USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Client client;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Provider provider;
}
