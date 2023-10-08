package com.everett.firebase;
/*
 * Copyright 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.SendResponse;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;

/**
 * Firebase Cloud Messaging (FCM) snippets for documentation.
 */

public class FirebaseMessagingSnippets {

	private static final Logger logger = LogManager.getLogger(FirebaseMessagingSnippets.class);

	public void sendToToken(String token, String title, String body, String url, String iconUrl) {
		logger.debug("REGISTRATION TOKEN: " + token.substring(0, 10));
		try {
			String response = FirebaseMessaging.getInstance()
					.send(allPlatformsMessage(token, title, body, url, iconUrl));
			logger.debug("SUCCESSFULLY SENT MESSAGE: " + response);
		} catch (FirebaseMessagingException fme) {
			logger.error("FIREBASE TOKEN VERIFICATION EXCEPTION", fme);
		}
	}

	public List<String> sendMulticastAndHandleErrors(List<String> registrationTokens, String title, String body,
			String url, String iconUrl) throws FirebaseMessagingException {
		List<String> failedTokens = new ArrayList<>();

		MulticastMessage message = MulticastMessage.builder()
				.setWebpushConfig(WebpushConfig.builder()
						.setFcmOptions(WebpushFcmOptions.withLink(url))
						.putData("title", title)
						.putData("body", body)
						.putData("icon", iconUrl)
						.putData("url", url)
						.build())
				.setAndroidConfig(AndroidConfig.builder()
						.setTtl(3600 * 1000)
						.setNotification(AndroidNotification.builder()
								.setTitle(title)
								.setBody(body)
								.setClickAction(url)
								.setIcon(iconUrl)
								.setColor("#f45342")
								.build())
						.build())
				.setApnsConfig(ApnsConfig.builder()
						.setAps(Aps.builder()
								.setBadge(42)
								.build())
						.build())
				.addAllTokens(registrationTokens)
				.build();
		BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
		if (response.getFailureCount() > 0) {
			List<SendResponse> responses = response.getResponses();
			for (int i = 0; i < responses.size(); i++) {
				if (!responses.get(i).isSuccessful()) {
					MessagingErrorCode errorCode = responses.get(i).getException()
							.getMessagingErrorCode();
					String errorMessage = responses.get(i).getException().getMessage();
					// System.out.println("Error code: " + errorCode);
					if (errorCode.equals(MessagingErrorCode.UNREGISTERED)) {
						failedTokens.add(registrationTokens.get(i));
						logger.debug("FOUND INVALID FCM TOKEN");
					} else {
						logger.debug("FCM error: " + errorMessage);
					}
				}

			}
			// logger.debug("List of tokens that caused failures: " + failedTokens);
		}
		return failedTokens;
		// [END send_multicast_error]
	}

	public Message allPlatformsMessage(String registrationToken, String title, String body, String url,
			String iconUrl) {
		// [START multi_platforms_message]
		Message message = Message.builder()
				.setWebpushConfig(WebpushConfig.builder()
						// .setNotification(new WebpushNotification(
						// title,
						// body,
						// "https://fms-laravel-images.s3.ap-southeast-1.amazonaws.com/images/logoAxonActive.png"))
						.setFcmOptions(WebpushFcmOptions.withLink(url))
						.putData("title", title)
						.putData("body", body)
						.putData("icon", iconUrl)
						.putData("url", url)
						.build())
				.setAndroidConfig(AndroidConfig.builder()
						.setTtl(3600 * 1000)
						.setNotification(AndroidNotification.builder()
								.setTitle(title)
								.setBody(body)
								.setClickAction(url)
								.setIcon(iconUrl)
								.setColor("#f45342")
								.build())
						.build())
				.setApnsConfig(ApnsConfig.builder()
						.setAps(Aps.builder()
								.setBadge(42)
								.build())
						.build())
				// .setTopic("approveme-reminder")
				.setToken(registrationToken)
				.build();
		// [END multi_platforms_message]
		return message;
	}
}
