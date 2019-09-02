package ru.zimin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.zimin.model.Dish;


import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CrudDishRepository extends JpaRepository<Dish, Integer> {

    @Query("SELECT d FROM Dish d where d.restaurant.id=:restId ")
    Optional<List<Dish>> getAll(@Param("restId")int restId);

    @Override
    @Transactional
    Dish save(Dish entity);

    @Override
    @Transactional
    void deleteById(Integer id);

    @Override
    Optional<Dish> findById(Integer id);

}
