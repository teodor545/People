package com.task.third.service.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "t_people_id", nullable = false)
    private Person person;

    @Column(name = "addr_type", nullable = false, length = 5)
    private String addressType;

    @Column(name = "addr_info", length = 300)
    private String addressInfo;
}
