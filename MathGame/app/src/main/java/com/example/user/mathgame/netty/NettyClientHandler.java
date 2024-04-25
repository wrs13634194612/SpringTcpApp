package com.example.user.mathgame.netty;

/**
 * Created by wrs on 2019/6/26,11:44
 * projectName: Testz
 * packageName: com.example.administrator.testz
 */


import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.example.user.mathgame.MainActivity;
import com.example.user.mathgame.data.Messages;
import com.example.user.mathgame.listener.MessageListener;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

//wangnetty
public class NettyClientHandler extends SimpleChannelInboundHandler {
    private ChannelHandlerContext context;
    private MessageListener listener;
    private NettyClient nettyClinet;
    private String tenantId;
    private int attempts = 0;
    private static final String TAG = NettyClientHandler.class.getSimpleName();

    //wangnetty
    public NettyClientHandler(MessageListener messageListener, NettyClient nettyClinet) {
        this.listener = messageListener;
        this.nettyClinet = nettyClinet;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        //Log.i(TAG, "channelRead0..."+o.toString());
        //在这里 回调接收 服务端 返回的消息
        listener.onGetServerMessage(o.toString());
    }

    // 建立连接就发送消息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("output connected!!");
        this.context = ctx;
        attempts = 0;
    }

    //断开netty连接
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("offline。。。。。。");
        //使用过程中断线重连
        final EventLoop eventLoop = ctx.channel().eventLoop();
        if (attempts < 12) {
            attempts++;
        }
        int timeout = 2 << attempts;
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                nettyClinet.start();
            }
        }, timeout, TimeUnit.SECONDS);
        ctx.fireChannelInactive();

    }

    //连接中
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                System.out.println("READER_IDLE");
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                /*向服务端发送心跳包,保持长连接*/
                Messages heartBeatBean = new Messages("HEARTBEAT", "");
                Gson gson = new Gson();
                String userJson = gson.toJson(heartBeatBean);
                // 发送心跳 System.out.println("wrs:" + userJson);
                ctx.channel().writeAndFlush(userJson);
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                System.out.println("ALL_IDLE");
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        //Log.e(TAG, "channelReadComplete");
    }

    /*
    要发送新消息，我们需要分配一个包含消息的新缓冲区。
    我们要写一个32位整数，因此我们需要一个容量至少为4个字节的ByteBuf。
    通过ChannelHandlerContext.alloc（）
    获取当前的ByteBufAllocator并分配一个新的缓冲区。
     */
    public void sendData(String userJson) {
        if (null != context && context.channel().isActive()) {
            context.channel().writeAndFlush(userJson + System.getProperty("line.separator"));
        }
    }

    private void writeToFile(String s) {
        byte[] buf = s.getBytes();
    }

}
