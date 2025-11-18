package com.likelion.likelionmail.mail.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final TaskScheduler taskScheduler;
    private final JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "your_email@gmail.com"; // 발신자

    public void sendTestMail(String to) {
        String subject = "[Likelion JWT] 이메일 전송 테스트";
        String body = """
            <div style="font-family: Arial, sans-serif; padding: 20px;">
                <h2>안녕하세요 🦁</h2>
                <p>이 메일은 <strong>Likelion JWT 프로젝트</strong>의 SMTP 테스트 메일입니다.</p>
                <p style="color: gray;">메일이 정상적으로 도착했다면 SMTP 설정이 성공한 것입니다!</p>
                <hr>
                <p style="font-size: 12px; color: #888;">© 2025 Likelion Project</p>
            </div>
            """;

        try {
            sendHtmlMail(to, subject, body);
            System.out.println("메일 전송 성공! 대상: " + to);
        } catch (MessagingException e) {
            e.printStackTrace(); // 로그 출력
            throw new RuntimeException("메일 전송 실패ㅜ: " + e.getMessage());
        }
    }


    //여기에다가 코드 추가하세용
    private void sendHtmlMail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true,"utf-8");

        helper.setFrom(FROM_ADDRESS);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }

    public void sendScheduledMail(String to){
        String subject = "[Likelion JWT] 이메일 전송 테스트";
        String body = """
            <div style="font-family: Arial, sans-serif; padding: 20px;">
                <h2>안녕하세요 🦁</h2>
                <p>이 메일은 <strong>Likelion JWT 프로젝트</strong>의 SMTP 테스트 메일입니다.</p>
                <p style="color: gray;">메일이 정상적으로 도착했다면 SMTP 설정이 성공한 것입니다!</p>
                <hr>
                <p style="font-size: 12px; color: #888;">© 2025 Likelion Project</p>
            </div>
            """;

        Instant sendTime = Instant.now().plusSeconds(60);

        // 스케줄 등록
        taskScheduler.schedule(() -> {
            try {
                sendHtmlMail(to, subject, body);
                log.info("✅ [예약 메일 발송 성공!] 대상: {}, 시간: {}", to, Instant.now());
            } catch (MessagingException e) {
                log.error("❌ [예약 메일 발송 실패] 대상: {}, 에러: {}", to, e.getMessage(), e);
            }
        }, sendTime);

        log.info("📧 [메일 예약 완료] 대상: {}, 발송 예정 시간: {}", to, sendTime);
    }


}
