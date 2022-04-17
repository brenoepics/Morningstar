package com.eu.habbo.messages.incoming.navigator;

import com.eu.habbo.Emulator;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.navigator.PopularRoomTagsResultComposer;

public class RequestTagsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new PopularRoomTagsResultComposer(Emulator.getGameEnvironment().getRoomManager().getTags()));
    }
}
