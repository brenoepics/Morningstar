package com.eu.habbo.messages.incoming.guilds.forums;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.guilds.Guild;
import com.eu.habbo.habbohotel.guilds.forums.ForumThread;
import com.eu.habbo.habbohotel.guilds.forums.ForumThreadState;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.generic.alerts.NotificationDialogMessageComposer;
import com.eu.habbo.messages.outgoing.guilds.forums.ThreadMessagesMessageComposer;
import com.eu.habbo.messages.outgoing.guilds.forums.ForumDataMessageComposer;
import com.eu.habbo.messages.outgoing.handshake.ErrorReportComposer;

public class GuildForumThreadsMessagesEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int guildId = packet.readInt();
        int threadId = packet.readInt();
        int index = packet.readInt(); // 40
        int limit = packet.readInt(); // 20

        Guild guild = Emulator.getGameEnvironment().getGuildManager().getGuild(guildId);
        ForumThread thread = ForumThread.getById(threadId);

        if (guild == null || thread == null) {
            this.client.sendResponse(new ErrorReportComposer(404));
            return;
        }

        if ((thread.getState() == ForumThreadState.HIDDEN_BY_ADMIN || thread.getState() == ForumThreadState.HIDDEN_BY_STAFF) && guild.getOwnerId() != this.client.getHabbo().getHabboInfo().getId()) {
            this.client.sendResponse(new NotificationDialogMessageComposer("oldforums.error.access_denied"));
        } else {
            this.client.sendResponse(new ThreadMessagesMessageComposer(guildId, threadId, index, thread.getComments(limit, index)));
        }

        this.client.sendResponse(new ForumDataMessageComposer(guild, this.client.getHabbo()));
    }
}