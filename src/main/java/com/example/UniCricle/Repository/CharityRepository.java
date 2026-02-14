package com.example.UniCricle.Repository;

import com.example.UniCricle.model.Charity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharityRepository extends JpaRepository<Charity, Long> {
}