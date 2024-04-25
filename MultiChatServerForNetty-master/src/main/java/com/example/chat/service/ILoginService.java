package com.example.chat.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import com.example.chat.domain.UserInfo;



public interface ILoginService {

    public List<UserInfo> finAll();

    public Page<UserInfo> finAll(Pageable pageable);

    public UserInfo findById(String id);

    public UserInfo save(UserInfo course);

    public void delete(String id);

}
