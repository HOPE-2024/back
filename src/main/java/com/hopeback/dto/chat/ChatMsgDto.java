package com.hopeback.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMsgDto {
    public enum MsgType {
        ENTER, TAKE, CLOSE
    }
    private MsgType type;
    private Long id;
    private String roomId;
    private String sender;
    private String msg;
    private String active;
}
