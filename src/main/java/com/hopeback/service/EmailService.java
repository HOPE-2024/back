package com.hopeback.service;

import com.hopeback.dto.member.PasswordResetDto;
import com.hopeback.entity.member.Member;
import com.hopeback.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender emailSender;

    private String pwCode;

    // 인증번호 생성 전체 페이지 (보안상의 이유로 외부에서 직접 접근되어서는 안됨. 해당 클래스 내에서만 호출 되어야 하므로 private)
    private MimeMessage createMessage(String to) throws Exception {
        pwCode = createKey();

        log.info("보내는 대상 : " + to);
        log.info("인증 번호 : " + pwCode);

        // JavaMailSender 인터페이스에 정의된 메소드 사용 createMimeMessage, addRecipients, setSubject
        // 이메일 메세지 생성
        MimeMessage message = emailSender.createMimeMessage();
        // 수신자 설정
        message.addRecipients(Message.RecipientType.TO, to);
        // 이메일 제목 설정
        message.setSubject("HOPE 인증번호 발송");

        // 이메일 내용 작성
        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1>👼🏻안녕하세요 HOPE 입니다. </h1>";
        msgg += "<br>";
        msgg += "<h4>아래 코드를 입력해 건강한 헬스 케어 함께 이어가 보아요 !</h4>";
        msgg += "<br>";
        msgg += "<div style='display: flex; font-family: verdana; width: 500px; height: 400px; background-image: url(\"https://firebasestorage.googleapis.com/v0/b/mini-project-1f72d.appspot.com/o/medicine.jpg?alt=media&token=c75b8fcd-128f-4162-baea-7c8e30559b1f\"); background-size: cover; position: relative; text-align: center; justify-content: center; '>";
        msgg += "<div style='display: flex; text-align: center; justify-content: center; align-items: center; flex-direction: column; '>";
        msgg += "<div style='font-size: 130%;'>";
        msgg += "인증 코드: <strong>";
        msgg += pwCode + "</strong></div><br/>";
        msgg += "</div>";
        msgg += "</div>";
        msgg += "</div>";

        // 이메일 내용 및 보내는 사람 설정
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("hopeproject2024@gmail.com", "HOPE Corp."));

        // 생성된 이메일 주소 반환
        return message;
    }


    // 랜덤 인증번호 생성
    public static String createKey() {
        // StringBuffer : 문자열을 동적으로 생성하고 수정할 수 있는 클래스
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            int index = rnd.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }

        }
        return key.toString();
    }

    // 이메일 전송 메소드
    public String sendSimpleMessage(String to) throws Exception {
        // 이메일 메세지 생성
        MimeMessage message = createMessage(to);

        try {
            // 이메일 전송
            emailSender.send(message);

            // 생성된 랜덤 코드 반환
            return pwCode;
        } catch(MailException e) {
            // 이메일 전송 실패시 예외를 던짐
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to send email: " + e.getMessage(), e);
        }
    }

    // 이메일로 아이디 찾아주기
    public  String findIdByEmail (String email) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if(memberOptional.isPresent()) {
            Member member = memberOptional.get();
            return member.getMemberId();
        } else {
            throw new EntityNotFoundException("해당 이메일로 등록된 회원을 찾을 수 없습니다.");
        }
    }

    // 토큰 없이 회원 아이디 조회  isPresent() : 객체에 값이 존재하는지 여부를 확인. 존재하면 true,  없으면 false를 반환
    public boolean memberIdExists(String memberId) {
        Optional<Member> member = memberRepository.findByMemberId(memberId);
        return member.isPresent();
    }

    // 이메일 인증 확인 후 비밀번호 재설정
    public boolean resetPassword(PasswordResetDto passwordResetDto) {
        Optional<Member> optionalMember = memberRepository.findByMemberId(passwordResetDto.getMemberId());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            // 비밀번호 암호화하여 저장
            String encodedPassword = passwordEncoder.encode(passwordResetDto.getPassword());
            member.setPassword(encodedPassword);
            memberRepository.save(member);

            return true;
        } else {
            return false;
        }
    }
}
