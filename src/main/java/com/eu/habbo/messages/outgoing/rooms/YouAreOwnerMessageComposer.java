package com.eu.habbo.messages.outgoing.rooms;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class YouAreOwnerMessageComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.YouAreOwnerMessageComposer);
        return this.response;
    }
}
