package com.example.user.mathgame;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.user.mathgame.data.BatteryBox;
import com.example.user.mathgame.data.ServerMessage;
import com.example.user.mathgame.data.SlotDynamicInfo;
import com.example.user.mathgame.data.SlotStaticInfo;
import com.example.user.mathgame.listener.MessageListener;
import com.example.user.mathgame.listener.NettyListener;
import com.example.user.mathgame.netty.NettyClient;
import com.google.gson.Gson;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements MessageListener, NettyListener, View.OnClickListener {
    private static final String HOST = "192.168.0.188";  //服务器的ip地址
    private static final int PORT = 60149;              ///指定的端口号
    private NettyClient mNettyClient;
    private static final String TAG = MainActivity.class.getSimpleName();

    HashMap<String, String> roomIdMap = new HashMap<String, String>();


    private Button btn_login, btn_create_room, btn_room_in, btn_room_out, btn_send_room, btn_login_all, btn_login_add, btn_login_update, btn_login_delete;

    /*127.0.0.1:60148 P服务端地址 IP: 8.135.10.183 端口: 35782*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNettyClient = new NettyClient(HOST, PORT);
        mNettyClient.setNettyListener(this);
        mNettyClient.setMessageListener(this);
        if (!mNettyClient.isConnected()) {
            mNettyClient.connect();
        }
        btn_login = findViewById(R.id.btn_login);
        btn_create_room = findViewById(R.id.btn_create_room);
        btn_room_in = findViewById(R.id.btn_room_in);
        btn_room_out = findViewById(R.id.btn_room_out);
        btn_send_room = findViewById(R.id.btn_send_room);
        btn_login_all = findViewById(R.id.btn_login_all);
        btn_login_delete = findViewById(R.id.btn_login_delete);

        btn_login_add = findViewById(R.id.btn_login_add);
        btn_login_update = findViewById(R.id.btn_login_update);

        btn_login.setOnClickListener(this);
        btn_create_room.setOnClickListener(this);
        btn_room_in.setOnClickListener(this);
        btn_send_room.setOnClickListener(this);
        btn_room_out.setOnClickListener(this);
        btn_login_all.setOnClickListener(this);
        btn_login_add.setOnClickListener(this);
        btn_login_update.setOnClickListener(this);
        btn_login_delete.setOnClickListener(this);

    }

    @Override
    public void onConnected() {
        Log.e(TAG, "onConnected...");
    }

    @Override
    public void onDisConnect() {
        Log.e(TAG, "onDisConnect...");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mNettyClient.sendLogin();
                break;
            case R.id.btn_login_all:
                mNettyClient.sendLoginAll();
                break;
            case R.id.btn_create_room:
                mNettyClient.sendCreateRoom();
                break;
            case R.id.btn_room_in:
                mNettyClient.sendRoomIN(roomIdMap.get("roomId"));
                break;
            case R.id.btn_room_out:
                mNettyClient.sendRoomOut(roomIdMap.get("roomId"));
                break;
            case R.id.btn_send_room:
                mNettyClient.sendRoomMessage("message");
                break;
            case R.id.btn_login_add:
                mNettyClient.sendLoginAdd();
                break;
            case R.id.btn_login_update:
                mNettyClient.sendLoginUpdate();
                break;
            case R.id.btn_login_delete:
                mNettyClient.sendLoginDelete();
                break;
        }
    }

    @Override
    public void onGetServerMessage(String msg) {
        Log.i(TAG, "onGetServerMessage..." + msg);
        Gson gson = new Gson();
        ServerMessage serverMessage = gson.fromJson(msg, ServerMessage.class);
        if (serverMessage.getResultCode() == 2002) {
            roomIdMap.put("roomId", serverMessage.getRoomId());
        }
    }
}
