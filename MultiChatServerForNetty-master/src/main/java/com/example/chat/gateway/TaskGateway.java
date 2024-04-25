package com.example.chat.gateway;

import com.example.chat.service.LoginService;
import com.example.chat.service.MessageSendService;
import com.example.chat.service.RoomService;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class TaskGateway {
    private final LoginService loginService;
    private final RoomService roomService;
    private final MessageSendService messageSendService;

    public void execute(Channel channel, Map<String, Object> requestData, Map<String, Object> responseData) throws Exception {

        String task = (String) requestData.getOrDefault("task", "");

        switch (task) {
            case "login":
                // 사용자 인증 처리 resultCode 2001
                loginService.login(channel, task, requestData, responseData,2001);
                break;
            case "loginAll":
                // 사용자 인증 처리 resultCode 2001
                loginService.loginAll(channel, task, requestData, responseData,2011);
                break;
            case "loginAdd":
                loginService.loginSave(channel, task, requestData, responseData,2012);
                break;
            case "loginUpdate":
                loginService.loginSave(channel, task, requestData, responseData,2013);
                break;
            case "loginDelete":
                loginService.loginDelete(channel, task, requestData, responseData,2014);
                break;
            case "createRoom":
                // 룸 생성 resultCode 2002
                roomService.createRoom(channel, task, responseData,2002);
                break;
            case "enterRoom":
                // 룸 입장 resultCode 2003
                roomService.enterRoom(channel, task, requestData, responseData,2003);
                break;
            case "exitRoom":
                // 룸 퇴장 resultCode 2004
                roomService.exitRoom(channel, task, responseData,2004);
                break;
            case "sendRoom":
                // 룸에 메세지 전송 resultCode 2005
                roomService.sendRoom(channel, task, requestData, responseData,2005);
                break;
            default:
                // 룸에 메세지 전송 resultCode 1005
                messageSendService.sendMessage(channel, responseData, new Exception("邮件分类不正确."), 1005);
        }

    }

}
