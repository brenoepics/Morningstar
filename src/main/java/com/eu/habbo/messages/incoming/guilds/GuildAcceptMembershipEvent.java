package com.eu.habbo.messages.incoming.guilds;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.guilds.Guild;
import com.eu.habbo.habbohotel.guilds.GuildMember;
import com.eu.habbo.habbohotel.guilds.GuildRank;
import com.eu.habbo.habbohotel.permissions.Permission;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.guilds.GuildMemberMgmtFailedMessageComposer;
import com.eu.habbo.messages.outgoing.guilds.GuildInfoComposer;
import com.eu.habbo.messages.outgoing.guilds.GuildMembershipRejectedMessageComposer;
import com.eu.habbo.plugin.events.guilds.GuildAcceptedMembershipEvent;

public class GuildAcceptMembershipEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int guildId = this.packet.readInt();
        int userId = this.packet.readInt();

        Guild guild = Emulator.getGameEnvironment().getGuildManager().getGuild(guildId);

        if (guild == null || (guild.getOwnerId() != this.client.getHabbo().getHabboInfo().getId() && !Emulator.getGameEnvironment().getGuildManager().getOnlyAdmins(guild).containsKey(this.client.getHabbo().getHabboInfo().getId()) && !this.client.getHabbo().hasPermission(Permission.ACC_GUILD_ADMIN)))
            return;

        Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(userId);

        if (habbo != null) {
            if (habbo.getHabboStats().hasGuild(guild.getId())) {
                this.client.sendResponse(new GuildMemberMgmtFailedMessageComposer(guild.getId(), GuildMemberMgmtFailedMessageComposer.ALREADY_ACCEPTED));
                return;
            } else {
                //Check the user has requested
                GuildMember member = Emulator.getGameEnvironment().getGuildManager().getGuildMember(guild, habbo);
                if (member == null || member.getRank().type != GuildRank.REQUESTED.type) {
                    this.client.sendResponse(new GuildMemberMgmtFailedMessageComposer(guild.getId(), GuildMemberMgmtFailedMessageComposer.NO_LONGER_MEMBER));
                    return;
                } else {
                    GuildAcceptedMembershipEvent event = new GuildAcceptedMembershipEvent(guild, userId, habbo);
                    Emulator.getPluginManager().fireEvent(event);
                    if (!event.isCancelled()) {
                        habbo.getHabboStats().addGuild(guild.getId());
                        Emulator.getGameEnvironment().getGuildManager().joinGuild(guild, this.client, habbo.getHabboInfo().getId(), true);
                        guild.decreaseRequestCount();
                        guild.increaseMemberCount();
                        this.client.sendResponse(new GuildMembershipRejectedMessageComposer(guild));
                        Room room = habbo.getHabboInfo().getCurrentRoom();
                        if (room != null) {
                            if (room.getGuildId() == guildId) {
                                habbo.getClient().sendResponse(new GuildInfoComposer(guild, habbo.getClient(), false, Emulator.getGameEnvironment().getGuildManager().getGuildMember(guildId, userId)));
                                room.refreshRightsForHabbo(habbo);
                            }
                        }
                    }
                }
            }
        } else {
            Emulator.getGameEnvironment().getGuildManager().joinGuild(guild, this.client, userId, true);
        }
    }
}
