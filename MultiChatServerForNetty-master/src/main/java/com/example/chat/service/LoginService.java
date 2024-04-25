package com.example.chat.service;

import com.example.chat.domain.UserInfo;
import com.example.chat.repository.*;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService implements ILoginService {
    private final UserRepository userRepository;
    private final ChannelIdUserIdRepository channelIdUserIdRepository;
    private final UserIdChannelRepository userIdChannelRepository;
    private final UserIdRoomIdRepository userIdRoomIdRepository;
    private final RoomIdUserIdRepository roomIdUserIdRepository;
    private final MessageSendService messageSendService;

    public void loginAll(Channel channel, String task, Map<String, Object> requestData, Map<String, Object> responseData,int resultCode) throws Exception {
        List<UserInfo> userList = finAll();
        messageSendService.sendMessage(channel, responseData, task, resultCode,userList);
    }


    public void loginSave(Channel channel, String task, Map<String, Object> requestData, Map<String, Object> responseData,int resultCode) throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId((String) requestData.get("userId"));
        userInfo.setUserName((String) requestData.get("userName"));
        userInfo.setPassword((String) requestData.get("password"));
        save(userInfo);
        messageSendService.sendMessage(channel, responseData, task, resultCode);
    }

    public void loginDelete(Channel channel, String task, Map<String, Object> requestData, Map<String, Object> responseData, int resultCode) throws Exception {
        String userId = (String) requestData.get("userId");
        delete(userId);
        messageSendService.sendMessage(channel, responseData, task, resultCode);
    }



    public void login(Channel channel, String task, Map<String, Object> requestData, Map<String, Object> responseData, int resultCode) throws Exception {
        String userId = (String) requestData.get("userId");
        String password = (String) requestData.get("password");
        if (userId == null || password == null) {
            messageSendService.sendMessage(channel, responseData, new Exception("请输入您的ID或密码."), 1002);
            return;
        }
        UserInfo user = findById(userId);
        if (user == null) {
            messageSendService.sendMessage(channel, responseData, new Exception("用户ID不存在."), 1003);
            return;
        } else if (!StringUtils.equals(password, user.getPassword())) {
            messageSendService.sendMessage(channel, responseData, new Exception("密码不匹配."), 1004);
            return;
        }
        channelIdUserIdRepository.getChannelIdUserIdMap().put(channel.id(), userId);
        userIdChannelRepository.getUserIdChannelMap().put(userId, channel);
        responseData.put("result", "登录成功");
        messageSendService.sendMessage(channel, responseData, task, resultCode);
    }

    public void logout(Channel channel) {
        String userId = channelIdUserIdRepository.getChannelIdUserIdMap().get(channel.id());
        if (!StringUtils.isEmpty(userId)) {
            userIdChannelRepository.getUserIdChannelMap().remove(userId);
            String roomId = userIdRoomIdRepository.getUserIdRoomIdMap().get(userId);
            if (!StringUtils.isEmpty(roomId)) {
                roomIdUserIdRepository.getRoomIdUserIdMap().remove(roomId, userId);
                userIdRoomIdRepository.getUserIdRoomIdMap().remove(userId);
            }
            channelIdUserIdRepository.getChannelIdUserIdMap().remove(channel.id());
        }
    }

    @Override
    public List<UserInfo> finAll() {
        return (List<UserInfo>) userRepository.findAll();
    }

    @Override
    public Page<UserInfo> finAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public UserInfo findById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserInfo save(UserInfo course) {
        return userRepository.save(course);
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
    }
}
