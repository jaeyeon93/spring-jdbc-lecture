package me.jimmy.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import me.jimmy.jdbc.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("memberV1", 10000);
        repository.save(member);

        // findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember : {}", findMember);

        assertThat(findMember).isEqualTo(member);

        // update money=10000 => 20000

        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        // delete
        repository.delete("memberV1");
        assertThatThrownBy(() -> repository.findById("memberV1"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void delete() throws SQLException {
        repository.delete("memberV1");
    }
}