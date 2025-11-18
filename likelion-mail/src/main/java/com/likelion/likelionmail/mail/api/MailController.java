package com.likelion.likelionmail.mail.api;

import com.likelion.likelionmail.mail.api.dto.request.MailRequest;
import com.likelion.likelionmail.mail.api.dto.response.MailResponse;
import com.likelion.likelionmail.mail.application.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/mail/send")
    public MailResponse sendTestMail(@Valid @RequestBody MailRequest request){
        mailService.sendTestMail(request.getTo());

        return new MailResponse("메일 전송에 성공했습니다.");
    }

    @GetMapping("/mail/send-login-user")
    public MailResponse sendmailToLoginUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null){
            throw new RuntimeException("로그인된 사용자가 없습니다.");
        }

        String email = authentication.getName();

        mailService.sendTestMail(email);

        return new MailResponse("로그인된 사용자에게 메일 전송에 성공했습니다.");
    }

    @PostMapping("/scheduled-mail/send")
    public MailResponse sendScheduledMail(@Valid @RequestBody MailRequest request){
        mailService.sendScheduledMail(request.getTo());

        return new MailResponse("스케줄링된 메일 전송에 성공했습니다.");
    }
}
