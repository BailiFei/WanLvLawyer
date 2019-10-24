package com.m7.imkfsdk.chat.chatrow;

import com.moor.imkf.model.entity.FromToMessage;

/**
 * Created by longwei on 2016/3/9.
 */
public class ChatRowUtils {

    /**
     * 获取聊天消息类型
     */
    public static int getChattingMessageType(FromToMessage msg) {
        if(FromToMessage.MSG_TYPE_TEXT.equals(msg.msgType)) {
            return 200;
        }else if(FromToMessage.MSG_TYPE_IMAGE.equals(msg.msgType)) {
            return 300;
        }else if(FromToMessage.MSG_TYPE_AUDIO.equals(msg.msgType)) {
            return 400;
        }else if(FromToMessage.MSG_TYPE_INVESTIGATE.equals(msg.msgType)) {
            return 500;
        }else if(FromToMessage.MSG_TYPE_FILE.equals(msg.msgType)) {
            return 600;
        }else if(FromToMessage.MSG_TYPE_IFRAME.equals(msg.msgType)) {
            return 700;
        }else if(FromToMessage.MSG_TYPE_BREAK_TIP.equals(msg.msgType)) {
            return 800;
        }else if(FromToMessage.MSG_TYPE_TRIP.equals(msg.msgType)) {
            return 900;
        }else if(FromToMessage.MSG_TYPE_RICHTEXT.equals(msg.msgType)){
            return 1300;
        }else if(FromToMessage.MSG_TYPE_CARDINFO.equals(msg.msgType)){
            return 1400;
        }else if(FromToMessage.MSG_TYPE_CARD.equals(msg.msgType)){
            return 1500;
        }
        return 0;
    }
}
