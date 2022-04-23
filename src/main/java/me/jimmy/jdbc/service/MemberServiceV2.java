package me.jimmy.jdbc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jimmy.jdbc.domain.Member;
import me.jimmy.jdbc.repository.MemberRepositoryV2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/*
 * 트랜잭션 - 파라미터연동
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false); // 트랜잭션 시작
            bizLogic(fromId, toId, money, con);
            con.commit(); // 성공시 커밋
        } catch (Exception e) {
            con.rollback(); // 실패시 롤백
            throw new IllegalStateException(e);
        } finally {
            releaseConnection(con);
        }
    }

    private void bizLogic(String fromId, String toId, int money, Connection con) throws SQLException {
        // 비즈니스 로직
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private void releaseConnection(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true); // 커넥션 풀을 고려해서 다시 자동 커밋으로 만들어야한다.
                con.close();
            } catch (Exception e) {
                log.error("error", e);
            }
        }
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) throw new IllegalStateException("이체중 예외 발생");
    }
}
