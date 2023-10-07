package com.everett.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.everett.daos.DeviceDAO;
import com.everett.exceptions.checkedExceptions.DeviceNotFoundException;
import com.everett.firebase.FirebaseMessagingSnippets;
import com.everett.models.Device;
import com.everett.models.User;
import com.google.firebase.messaging.FirebaseMessagingException;

@Stateless
public class PushNotificationService {
    private static final Logger logger = LogManager.getLogger(CommentServiceImp.class);

    @Inject
    @ConfigProperty(name = "default_ava_url")
    private String defaultAvaUrl;

    @Inject
    @ConfigProperty(name = "default_app_url")
    private String defaultAppUrl;

    @Inject
    DeviceDAO deviceDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void sendCommentAddedMessage(User postOwner, Long postId, User commenter) {
        if (postOwner.getUserId() == commenter.getUserId()) {
            return;
        }
        String commenterName = commenter.getGivenName() + " " + commenter.getFamilyName();
        String commenterAvatar = commenter.getAvatars().iterator().next().getAvaUrl();
        if (commenterAvatar == "") {
            commenterAvatar = defaultAvaUrl;
        }

        Set<Device> devices = postOwner.getDevices();
        if (devices.isEmpty() || devices.size() == 0) {
            return;
        }
        List<String> tokens = new ArrayList<String>();
        List<String> failedTokens = new ArrayList<>();
        FirebaseMessagingSnippets fbms = new FirebaseMessagingSnippets();
        for (Device device : devices) {
            tokens.add(device.getFcmToken());
        }

        String title = commenterName + " just left a comment on your post";
        String toPostUrl = defaultAppUrl + "post/" + postId;
        try {
            failedTokens = fbms.sendMulticastAndHandleErrors(tokens, title,
                    "Tap to view your post", toPostUrl, commenterAvatar);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }

        if (failedTokens.size() == 0) {
            return;
        }

        for (String failedToken : failedTokens) {
            logger.info("DELETE TOKEN: " + failedToken);
            try {
                deviceDAO.deleteDeviceByToken(failedToken);
            } catch (DeviceNotFoundException e) {
                System.err.println("FAILED TOKEN: " + failedToken.substring(0, 10));
            }
        }
    }
}
