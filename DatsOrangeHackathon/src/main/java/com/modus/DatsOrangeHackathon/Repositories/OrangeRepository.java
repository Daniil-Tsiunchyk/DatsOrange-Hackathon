package com.modus.DatsOrangeHackathon.Repositories;

import com.modus.DatsOrangeHackathon.Models.Orange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface OrangeRepository extends JpaRepository<Orange, Integer> {
}