package com.order.repository;

import com.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.member WHERE o.member.id = :memberId ORDER BY o.createdAt DESC")
    List<Order> findByMemberIdOrderByCreatedAtDesc(@Param("memberId") Long memberId);
}
