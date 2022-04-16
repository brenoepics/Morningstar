package com.eu.habbo.messages.incoming.inventory;

import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.inventory.BadgesComposer;

public class RequestInventoryBadgesEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new BadgesComposer(this.client.getHabbo()));
    }
}
