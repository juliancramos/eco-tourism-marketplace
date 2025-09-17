package com.marketplace.userprofileservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PROVIDER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private User user;

    @Column(name = "trade_name", length = 160)
    private String tradeName;

    @Column(length = 2000)
    private String description;

    @Column(name = "phone_number", length = 40)
    private String phoneNumber;

    @Column(length = 500)
    private String webpage;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();
}
