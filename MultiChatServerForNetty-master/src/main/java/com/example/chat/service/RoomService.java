package com.example.chat.service;

import com.example.chat.domain.UserInfo;
import com.example.chat.repository.*;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService implements ILoginService{

    private final UserIdChannelRepository userIdChannelRepository;
    private final ChannelIdUserIdRepository channelIdUserIdRepository;
    private final UserIdRoomIdRepository userIdRoomIdRepository;
    private final RoomIdUserIdRepository roomIdUserIdRepository;
    private final UserRepository userRepository;
    private final LoginService loginService;
    private final MessageSendService messageSendService;

    public void createRoom(Channel channel, String task, Map<String, Object> responseData,int resultCode) throws Exception {
        String userId = channelIdUserIdRepository.getChannelIdUserIdMap().get(channel.id());
        responseData.put("task", task);

        if(userIdRoomIdRepository.getUserIdRoomIdMap().containsKey(userId)) {

            responseData.put("error", "Already entered user");
            messageSendService.sendMessage(channel, responseData, new Exception("这是进入房间的用户."), 3006);
            return;
        }

        String roomId = UUID.randomUUID().toString();

        roomIdUserIdRepository.getRoomIdUserIdMap().add(roomId, userId);
        userIdRoomIdRepository.getUserIdRoomIdMap().put(userId, roomId);

        responseData.put("roomId", roomId);

        messageSendService.sendMessage(channel, responseData, task,resultCode);
    }

    public void enterRoom(Channel channel, String task, Map<String, Object> requestData, Map<String, Object> responseData,int resultCode) throws Exception {
        String userId = channelIdUserIdRepository.getChannelIdUserIdMap().get(channel.id());

        if(userIdRoomIdRepository.getUserIdRoomIdMap().containsKey(userId)) {
            messageSendService.sendMessage(channel, responseData, new Exception("这是进入房间的用户."), 4006);
            return;
        }

        String roomId = (String) requestData.get("roomId");

        if(!roomIdUserIdRepository.getRoomIdUserIdMap().containsKey(roomId)){
            messageSendService.sendMessage(channel, responseData, new Exception("房间ID不存在。.."), 1007);
            return;
        }

        roomIdUserIdRepository.getRoomIdUserIdMap().add(roomId, userId);
        userIdRoomIdRepository.getUserIdRoomIdMap().put(userId, roomId);

        responseData.put("task", task);
        responseData.put("roomId", roomId);

        messageSendService.sendMessage(channel, responseData, task,resultCode);
    }

    public void exitRoom(Channel channel, String task, Map<String, Object> responseData,int resultCode) throws Exception {

        String userId = channelIdUserIdRepository.getChannelIdUserIdMap().get(channel.id());

        if (!userIdRoomIdRepository.getUserIdRoomIdMap().containsKey(userId)) {
            messageSendService.sendMessage(channel, responseData, new Exception("房间内不存在."), 1008);
            return;
        }

        String roomId = userIdRoomIdRepository.getUserIdRoomIdMap().get(userId);

        roomIdUserIdRepository.getRoomIdUserIdMap().remove(roomId, userId);
        userIdRoomIdRepository.getUserIdRoomIdMap().remove(userId);

        responseData.put("task", task);

        messageSendService.sendMessage(channel, responseData, task,resultCode);

    }

    public void sendRoom(Channel channel, String task, Map<String, Object> requestData, Map<String, Object> responseData,int resultCode) throws Exception {

        String userId = channelIdUserIdRepository.getChannelIdUserIdMap().get(channel.id());

        if (!userIdRoomIdRepository.getUserIdRoomIdMap().containsKey(userId)) {
            messageSendService.sendMessage(channel, responseData, new Exception("房间内不存在."), 1008);
            return;
        }

        UserInfo user = findById(userId);
        if (user == null) {
            messageSendService.sendMessage(channel, responseData, new Exception("无法检索用户信息.."), 1009);
            return;
        }

        String userName = user.getUserName();

        //创建要传递的消息内容
        responseData.put("task", task);
        responseData.put("userId", userId);
        responseData.put("userName", userName);
        responseData.put("msg", requestData.get("msg"));

        String roomId = userIdRoomIdRepository.getUserIdRoomIdMap().get(userId);

        // 发送消息到房间
        roomIdUserIdRepository.getRoomIdUserIdMap().get(roomId).parallelStream().forEach(otherUserId -> {

            Channel otherChannel = userIdChannelRepository.getUserIdChannelMap().get(otherUserId);

            // 如果频道不活跃，则删除用户
            if (!otherChannel.isActive()) {

                loginService.logout(otherChannel);
                return;

            }

            messageSendService.sendMessage(otherChannel, responseData, task,resultCode);
        });
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
