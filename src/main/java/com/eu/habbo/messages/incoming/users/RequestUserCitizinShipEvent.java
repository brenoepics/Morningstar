package com.eu.habbo.messages.incoming.users;

import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.users.TalentTrackLevelMessageEvent;

public class RequestUserCitizinShipEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new TalentTrackLevelMessageEvent(this.packet.readString()));
    }
}
