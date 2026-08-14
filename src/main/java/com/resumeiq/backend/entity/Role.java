package com.resumeiq.backend.entity;

import com.resumeiq.backend.entity.enums.RoleType;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType name;

    public Role() {
    }

    public Role(Long id, RoleType name) {
        this.id = id;
        this.name = name;
    }

    // Getter
    public Long getId() {
        return id;
    }

    // Setter
    public void setId(Long id) {
        this.id = id;
    }

    // Getter
    public RoleType getName() {
        return name;
    }

    // Setter
    public void setName(RoleType name) {
        this.name = name;
    }

}