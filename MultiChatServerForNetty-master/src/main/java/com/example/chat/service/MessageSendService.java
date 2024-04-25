package com.example.chat.service;

import com.example.chat.domain.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageSendService {
    private final ObjectMapper objectMapper;

    public void sendMessage(Channel channel, Map<String, Object> responseData, Throwable throwable, int resultCode) throws Exception{
        responseData.put("resultCode", resultCode);
        responseData.put("msg", "heart client tcp task_error");

        channel.writeAndFlush(sendMessage(responseData));
    }

    public void sendMessage(Channel channel, Map<String, Object> responseData, String task,  int resultCode, List<UserInfo> userList) throws Exception{
        responseData.put("resultCode", resultCode);

        responseData.put("task", task);

        responseData.put("list", userList);

        channel.writeAndFlush(sendMessage(responseData));
    }

    void sendMessage(Channel channel, Map<String, Object> responseData, String task,int resultCode) {

        responseData.put("resultCode", resultCode);
        responseData.put("task", task);

        try {

            channel.writeAndFlush(sendMessage(responseData));

        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    Object sendMessage(Map<String, Object> responseData) throws Exception {
        return objectMapper.writeValueAsString(responseData) + System.lineSeparator();
    }

    public Object sendMessage(String message) {
        return message + System.lineSeparator();
    }

}
