package com.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.user.entity.Member;
import com.user.entity.QMember;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public void join(Member member) {
        entityManager.persist(member);
    }

    public Member findByLoginId(String loginId) {
        QMember member = QMember.member;

        return queryFactory
                .selectFrom(member)
                .where(member.loginId.eq(loginId))
                .fetchOne();
    }
}
