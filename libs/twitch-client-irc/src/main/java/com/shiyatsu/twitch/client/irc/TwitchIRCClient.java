package com.shiyatsu.twitch.client.irc;

import java.util.function.Consumer;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.shiyatsu.logger.ILoggerService;
import com.shiyatsu.logger.impl.LoggerService;

/**
 * Represents a singleton client for interacting with Twitch IRC, encapsulating authentication and communication logic.
 * 
 * @author Shiyatsu
 */
public class TwitchIRCClient {

	private static ILoggerService logger = LoggerService.getLoggingService();
	private static TwitchIRCClient instance = null;

	private String clientId = null;
	private String clientSecret = null;
	private String tmiToken = null;
	private TwitchClient twitchClient = null;

	/**
     * Private constructor for TwitchIRCClient to prevent external instantiation.
     *
     * @param clientId     The client ID for Twitch API access.
     * @param clientSecret The client secret for Twitch API access.
     * @param tmiToken     The TMI token for accessing Twitch chat functionalities. https://twitchapps.com/tmi/
     */
	private TwitchIRCClient(String clientId, String clientSecret, String tmiToken) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.tmiToken = tmiToken;
	}

	/**
     * Returns the singleton instance of TwitchIRCClient, creating it if necessary.
     *
     * @param clientId     The client ID for Twitch API access.
     * @param clientSecret The client secret for Twitch API access.
     * @param tmiToken     The TMI token for accessing Twitch chat functionalities. https://twitchapps.com/tmi/
     * @return The singleton instance of TwitchIRCClient.
     */
	public static synchronized TwitchIRCClient getInstance(String clientId, String clientSecret, String tmiToken) {
		if (instance == null) {
			instance = new TwitchIRCClient(clientId, clientSecret, tmiToken);
		}
		return instance;
	}

	/**
     * Returns the already-initialized singleton instance of TwitchIRCClient.
     *
     * @return The singleton instance of TwitchIRCClient.
     * @throws IllegalStateException if the instance has not been initialized yet.
     */
	public static synchronized TwitchIRCClient getInstance() {
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
		CredentialManager credentialManager = CredentialManagerBuilder.builder().build();
		credentialManager.registerIdentityProvider(new TwitchIdentityProvider(clientId, clientSecret, ""));
		OAuth2Credential oAuthCredential = new OAuth2Credential("twitch", tmiToken);
		twitchClient = TwitchClientBuilder.builder().withClientId(clientId).withClientSecret(clientSecret)
				.withEnableChat(true).withChatAccount(oAuthCredential).build();
		SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);
		if (messageEventHandler != null) {
			eventHandler.onEvent(ChannelMessageEvent.class, messageEventHandler);
		} else {
			eventHandler.onEvent(ChannelMessageEvent.class, this::onMessage);
		}
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
	
	// Getter and setter methods...

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public TwitchClient getTwitchClient() {
		return twitchClient;
	}

	public void setTwitchClient(TwitchClient twitchClient) {
		this.twitchClient = twitchClient;
	}

}
