package com.shiyatsu.twitch.client.irc;

import java.util.Objects;
import java.util.function.Consumer;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.shiyatsu.logger.ILoggerService;
import com.shiyatsu.logger.impl.LoggerService;

/**
 * @author Shiyatsu
 */
public class TwitchTmiClient {

	private static final ILoggerService logger = LoggerService.getLoggingService();
	private static TwitchTmiClient instance = null;

	private String tmiToken = null;
	private TwitchClient twitchClient = null;

	/**
     * Private constructor for TwitchTmiClient to prevent external instantiation.
     *
     * @param tmiToken The TMI token for accessing Twitch chat functionalities.
     */
	private TwitchTmiClient(String tmiToken) {
		this.tmiToken = tmiToken;
	}

	/**
     * Returns the singleton instance of TwitchTmiClient, creating it if necessary.
     *
     * @param tmiToken The TMI token for accessing Twitch chat functionalities.
     * @return The singleton instance of TwitchTmiClient.
     */
	public static synchronized TwitchTmiClient getInstance(String tmiToken) {
		if (instance == null) {
			instance = new TwitchTmiClient(tmiToken);
		}
		return instance;
	}

	/**
     * Returns the already-initialized singleton instance of TwitchTmiClient.
     *
     * @return The singleton instance of TwitchTmiClient.
     * @throws IllegalStateException if the instance has not been initialized yet.
     */
	public static synchronized TwitchTmiClient getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Instance not initialized, use getInstance(String, String) in first.");
		}
		return instance;
	}

	/**
     * Initializes the Twitch client and sets up the message event handler.
     *
     * @param messageEventHandler The consumer to handle message events. If null, a default handler is used.
     */
	public void init(Consumer<ChannelMessageEvent> messageEventHandler) {
		OAuth2Credential oAuthCredential = new OAuth2Credential("twitch", tmiToken);
		twitchClient = TwitchClientBuilder.builder().withEnableChat(true).withChatAccount(oAuthCredential).build();
		SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);
		eventHandler.onEvent(ChannelMessageEvent.class, Objects.requireNonNullElseGet(messageEventHandler, () -> this::onMessage));
	}

	/**
     * Default message event handler that logs incoming messages.
     *
     * @param event The channel message event to handle.
     */
	public void onMessage(ChannelMessageEvent event) {
		String broadcasterName = event.getChannel().getName();
		String broadcasterId = event.getChannel().getId();
		String viewerName = event.getUser().getName();
		String viewerId = event.getUser().getId();
		String message = event.getMessage();

		String logMessage = String.format("New message on %s [%s] from %s [%s] [ PERM : %s] (first : %s) : %s",
				broadcasterName, broadcasterId, viewerName, viewerId, event.getPermissions(),
				event.isDesignatedFirstMessage(), message);
		logger.info(this, logMessage);
	}

	/**
     * Joins a Twitch channel to start listening to messages.
     *
     * @param channel The name of the channel to join.
     */
	public void joinChannel(String channel) {
		twitchClient.getChat().joinChannel(channel);
	}

	/**
     * Sends a message to a specified Twitch channel.
     *
     * @param channel The name of the channel to send the message to.
     * @param message The message to be sent.
     */
	public void sendMessage(String channel, String message) {
		twitchClient.getChat().sendMessage(channel, message);
	}
	
	public TwitchClient getTwitchClient() {
		return twitchClient;
	}

	public void setTwitchClient(TwitchClient twitchClient) {
		this.twitchClient = twitchClient;
	}

}
