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
        // [START send_to_token]
        // This registration token comes from the client FCM SDKs.
        // String registrationToken = Messaging.getAccessToken();
        // String registrationToken =
        // "dUImDnPiDNpPEglIDdvQey:APA91bHZNGHMx9xZ_EcI8xzNqM5H4AViJIFLNTIDOlK4otTp86_KZ1p_KTmmMDb8WqtLpF37n6KrKoIkSdhC4NeYhVbpblTqRKRnvUZoKLslyS2y7KRP1T7rcqnWrzQ6Vj0rkTlJ8_MN";
        logger.debug("REGISTRATION TOKEN: " + token.substring(0, 10));

        try {
            // Send a message to the device corresponding to the provided registration
            // token.
            String response = FirebaseMessaging.getInstance()
                    .send(allPlatformsMessage(token, title, body, url, iconUrl));
            // Response is a message ID string.
            logger.debug("SUCCESSFULLY SENT MESSAGE: " + response);
        } catch (FirebaseMessagingException fme) {
            logger.error("FIREBASE TOKEN VERIFICATION EXCEPTION", fme);
        }
        // [END send_to_token]
    }

    // public void sendToTopic() throws FirebaseMessagingException {
    // // [START send_to_topic]
    // // The topic name can be optionally prefixed with "/topics/".
    // String topic = "highScores";

    // // See documentation on defining a message payload.
    // Message message = Message.builder()
    // .putData("score", "850")
    // .putData("time", "2:45")
    // .setTopic(topic)
    // .build();

    // // Send a message to the devices subscribed to the provided topic.
    // String response = FirebaseMessaging.getInstance().send(message);
    // // Response is a message ID string.
    // logger.debug("Successfully sent message: " + response);
    // // [END send_to_topic]
    // }

    // public void sendToCondition() throws FirebaseMessagingException {
    // // [START send_to_condition]
    // // Define a condition which will send to devices which are subscribed
    // // to either the Google stock or the tech industry topics.
    // String condition = "'stock-GOOG' in topics || 'industry-tech' in topics";

    // // See documentation on defining a message payload.
    // Message message = Message.builder()
    // .setNotification(Notification.builder()
    // .setTitle("$GOOG up 1.43% on the day")
    // .setBody("$GOOG gained 11.80 points to close at 835.67, up 1.43% on the
    // day.")
    // .build())
    // .setCondition(condition)
    // .build();

    // // Send a message to devices subscribed to the combination of topics
    // // specified by the provided condition.
    // String response = FirebaseMessaging.getInstance().send(message);
    // // Response is a message ID string.
    // logger.debug("Successfully sent message: " + response);
    // // [END send_to_condition]
    // }

    // public void sendDryRun() throws FirebaseMessagingException {
    // Message message = Message.builder()
    // .putData("score", "850")
    // .putData("time", "2:45")
    // .setToken("token")
    // .build();

    // // [START send_dry_run]
    // // Send a message in the dry run mode.
    // boolean dryRun = true;
    // String response = FirebaseMessaging.getInstance().send(message, dryRun);
    // // Response is a message ID string.
    // logger.debug("Dry run successful: " + response);
    // // [END send_dry_run]
    // }

    // public void sendAll() throws FirebaseMessagingException {
    // String registrationToken = "YOUR_REGISTRATION_TOKEN";

    // // [START send_all]
    // // Create a list containing up to 500 messages.
    // List<Message> messages = Arrays.asList(
    // Message.builder()
    // .setNotification(Notification.builder()
    // .setTitle("Price drop")
    // .setBody("5% off all electronics")
    // .build())
    // .setToken(registrationToken)
    // .build(),
    // // ...
    // Message.builder()
    // .setNotification(Notification.builder()
    // .setTitle("Price drop")
    // .setBody("2% off all books")
    // .build())
    // .setTopic("readers-club")
    // .build());

    // BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
    // // See the BatchResponse reference documentation
    // // for the contents of response.
    // logger.debug(response.getSuccessCount() + " messages were sent
    // successfully");
    // // [END send_all]
    // }

    // public void sendMulticast() throws FirebaseMessagingException {
    // // [START send_multicast]
    // // Create a list containing up to 500 registration tokens.
    // // These registration tokens come from the client FCM SDKs.
    // List<String> registrationTokens = Arrays.asList(
    // "YOUR_REGISTRATION_TOKEN_1",
    // // ...
    // "YOUR_REGISTRATION_TOKEN_n");

    // MulticastMessage message = MulticastMessage.builder()
    // .putData("score", "850")
    // .putData("time", "2:45")
    // .addAllTokens(registrationTokens)
    // .build();
    // BatchResponse response =
    // FirebaseMessaging.getInstance().sendMulticast(message);
    // // See the BatchResponse reference documentation
    // // for the contents of response.
    // logger.debug(response.getSuccessCount() + " messages were sent
    // successfully");
    // // [END send_multicast]
    // }

    public List<String> sendMulticastAndHandleErrors(List<String> registrationTokens, String title, String body,
            String url, String iconUrl) throws FirebaseMessagingException {
        // [START send_multicast_error]
        // These registration tokens come from the client FCM SDKs.
        // List<String> registrationTokens = Arrays.asList(
        // "YOUR_REGISTRATION_TOKEN_1",
        // // ...
        // "YOUR_REGISTRATION_TOKEN_n");

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

    // public Message androidMessage() {
    // // [START android_message]
    // Message message = Message.builder()
    // .setAndroidConfig(AndroidConfig.builder()
    // .setTtl(3600 * 1000) // 1 hour in milliseconds
    // .setPriority(AndroidConfig.Priority.NORMAL)
    // .setNotification(AndroidNotification.builder()
    // .setTitle("$GOOG up 1.43% on the day")
    // .setBody("$GOOG gained 11.80 points to close at 835.67, up 1.43% on the
    // day.")
    // .setIcon("stock_ticker_update")
    // .setColor("#f45342")
    // .build())
    // .build())
    // .setTopic("industry-tech")
    // .build();
    // // [END android_message]
    // return message;
    // }

    // public Message apnsMessage() {
    // // [START apns_message]
    // Message message = Message.builder()
    // .setApnsConfig(ApnsConfig.builder()
    // .putHeader("apns-priority", "10")
    // .setAps(Aps.builder()
    // .setAlert(ApsAlert.builder()
    // .setTitle("$GOOG up 1.43% on the day")
    // .setBody("$GOOG gained 11.80 points to close at 835.67, up 1.43% on the
    // day.")
    // .build())
    // .setBadge(42)
    // .build())
    // .build())
    // .setTopic("industry-tech")
    // .build();
    // // [END apns_message]
    // return message;
    // }

    // public Message webpushMessage() {
    // // [START webpush_message]
    // Message message = Message.builder()
    // .setWebpushConfig(WebpushConfig.builder()
    // .setNotification(new WebpushNotification(
    // "$GOOG up 1.43% on the day",
    // "$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day.",
    // "https://my-server/icon.png"))
    // .setFcmOptions(WebpushFcmOptions
    // .withLink("https://my-server/page-to-open-on-click"))
    // .build())
    // .setTopic("industry-tech")
    // .build();
    // // [END webpush_message]
    // return message;
    // }

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

    // public void subscribeToTopic() throws FirebaseMessagingException {
    // String topic = "highScores";
    // // [START subscribe]
    // // These registration tokens come from the client FCM SDKs.
    // List<String> registrationTokens = Arrays.asList(
    // "YOUR_REGISTRATION_TOKEN_1",
    // // ...
    // "YOUR_REGISTRATION_TOKEN_n");

    // // Subscribe the devices corresponding to the registration tokens to the
    // // topic.
    // TopicManagementResponse response =
    // FirebaseMessaging.getInstance().subscribeToTopic(
    // registrationTokens, topic);
    // // See the TopicManagementResponse reference documentation
    // // for the contents of response.
    // logger.debug(response.getSuccessCount() + " tokens were subscribed
    // successfully");
    // // [END subscribe]
    // }

    // public void unsubscribeFromTopic() throws FirebaseMessagingException {
    // String topic = "highScores";
    // // [START unsubscribe]
    // // These registration tokens come from the client FCM SDKs.
    // List<String> registrationTokens = Arrays.asList(
    // "YOUR_REGISTRATION_TOKEN_1",
    // // ...
    // "YOUR_REGISTRATION_TOKEN_n");

    // // Unsubscribe the devices corresponding to the registration tokens from
    // // the topic.
    // TopicManagementResponse response =
    // FirebaseMessaging.getInstance().unsubscribeFromTopic(
    // registrationTokens, topic);
    // // See the TopicManagementResponse reference documentation
    // // for the contents of response.
    // logger.debug(response.getSuccessCount() + " tokens were unsubscribed
    // successfully");
    // // [END unsubscribe]
    // }
}
