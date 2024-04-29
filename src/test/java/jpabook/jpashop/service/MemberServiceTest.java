package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    //@Rollback(value = false) //test에서 Transactional은 rollback을 기본적으로 하기때문에 insert문을 보고싶으면 false해야함
    public void 회원가입() throws Exception {

        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        em.flush(); //@Rollback 설정없이 insert문을 보려면 flush 해주면 된다.(rollback은 당연히 한다)
        assertEquals(member, memberRepository.findById(saveId).get());
    }

    @Test
    public void 중복_회원_예외() throws Exception {

        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2); //예외가 발생해야한다!
        });

        //then
//        fail("예외가 발생해야 한다.");

    }

}