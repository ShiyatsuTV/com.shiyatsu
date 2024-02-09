package com.shiyatsu.example.twitch.client.irc.app;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.shiyatsu.logger.ILoggerService;
import com.shiyatsu.logger.impl.LoggerService;
import com.shiyatsu.twitch.client.irc.TwitchIRCClient;

public class App {

	private static ILoggerService logger = LoggerService.getLoggingService();

	public static void main(String[] args) {
		String channelToJoin = "shiyatsutv";
		TwitchIRCClient client = TwitchIRCClient.getInstance("","", "");
		client.init(App::onMessage);
		client.joinChannel(channelToJoin);
		client.sendMessage(channelToJoin, "Test");
	}

	public static void onMessage(ChannelMessageEvent event) {
		String broadcasterName = event.getChannel().getName();
		String broadcasterId = event.getChannel().getId();
		String viewerName = event.getUser().getName();
		String viewerId = event.getUser().getId();
		String message = event.getMessage();

		String logMessage = String.format("New message on %s [%s] from %s [%s] [ PERM : %s] (first : %s) : %s",
				broadcasterName, broadcasterId, viewerName, viewerId, event.getPermissions(),
				event.isDesignatedFirstMessage(), message);
		logger.info(App.class, logMessage);
	}

}
