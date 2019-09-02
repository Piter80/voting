package ru.zimin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.zimin.model.Vote;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {

    Optional<List<Vote>> getAllByUserId(int userId);

    Optional<List<Vote>> getAllByRestaurantId(int restId);

    @Query("SELECT v from Vote v WHERE v.dateTime BETWEEN :startDate AND :endDate")
    Optional<List<Vote>> getAllBetweenDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.dateTime BETWEEN :startDate AND :endDate")
    Optional<List<Vote>> getAllBetweenDateWithUserId(@Param("userId") int userId, @Param("startDate") LocalDateTime startDate,@Param("endDate") LocalDateTime endDate);


    @Override
    @Transactional
    Vote save(Vote entity);

    @Override
    @Transactional
    void deleteById(Integer id);


    @Override
    Optional<Vote> findById(Integer id);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.dateTime BETWEEN :startDate AND :endDate")
    List<Vote> getAllBetweenDateWithUserIdUtil(@Param("userId") int userId, @Param("startDate") LocalDateTime of, @Param("endDate") LocalDateTime of1);
}
