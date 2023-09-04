package com.modus.DatsOrangeHackathon.Models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Entity
@Table(name = "Oranges")
public class Orange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orangeID;

    @Column(unique = true, nullable = false)
    private String username;

}
