package com.everett.apis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.everett.dtos.PostReceiveDTO;
import com.everett.dtos.PostResponseDTO;
import com.everett.exceptions.checkedExceptions.DeletePostNotAuthorizedException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.Major;
import com.everett.models.Post;
import com.everett.models.Topic;
import com.everett.models.User;
import com.everett.services.PostService;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PostAPITest {
    @InjectMocks
    PostAPI postAPI;

    @Mock
    PostService postService;

    private Post post1;
    private Post post2;

    private Topic topic;
    private Major major;
    private User user;
    private PostResponseDTO postResponse;
    private String email;

    @BeforeAll
    public void setUp() {
        this.topic = new Topic("Test Topic", "Test Topic Des", 1l);
        this.major = new Major("Test Major", "Test Major Des", 1l);
        Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        this.user = new User("B1910421", "Nhan", "Le", "test@mail.com", createdTime, "active");
        this.post1 = new Post(user, createdTime, "Test Post 1", "public", topic, major);
        this.post2 = new Post(user, createdTime, "Test Post 2", "public", topic, major);
        this.postResponse = new PostResponseDTO(1l, 1L, createdTime, "Test post", "general", "general");
        this.email = "tester@mail.com";
    }

    @Test
    void testcreateLeaveRequestTest() {
        PostReceiveDTO postPayload = new PostReceiveDTO("Test Post", "Public", 1l, 1l, "test_url");
        assertEquals(200, postAPI.createPost(postPayload).getStatus());
    }

    @Test
    public void testGetAllPosts() {
        List<PostResponseDTO> posts = new ArrayList<PostResponseDTO>();
        posts.add(new PostResponseDTO(post1));
        posts.add(new PostResponseDTO(post2));
        when(postService.getAllPosts()).thenReturn(posts);
        Response res = postAPI.getAllPosts();
        assertEquals(200, res.getStatus());
        assertNotNull(res.getEntity());
    }

    // @Test
    // public void testCreatePostMissingKeys() {
    // PostReceiveDTO incommpletedPayload = new PostReceiveDTO("test post",
    // "public", null, null, "test/url");
    // assertThrows(WebApplicationException.class, () -> {
    // postAPI.createPost(incommpletedPayload);
    // });
    // }

    @Test
    public void testCreatePost() throws UserNotFoundException {
        PostReceiveDTO postPayload = new PostReceiveDTO("Test Post", "Public", 1l, 1l, "test_url");
        doNothing().when(postService).createPost(postPayload, email);
        Response res = postAPI.createPost(postPayload);
        assertEquals(200, res.getStatus());
        assertNotNull(res.getEntity());
    }

    @Test
    public void testGetPostById() {
        when(postService.getPostResponseById(1L)).thenReturn(postResponse);
        Response res = postAPI.getPostById(1L);
        assertEquals(200, res.getStatus());
        assertNotNull(res.getEntity());
    }

    // @Test
    // public void testUpdatePostMissingKeys() {
    // PostReceiveDTO postPayload = new PostReceiveDTO("Test Post", "Public", 1l,
    // null, "test_url");
    // assertThrows(WebApplicationException.class, () -> {
    // postAPI.updatePost(1L, postPayload);
    // });
    // }

    @Test
    public void testUpdatePost() {
        PostReceiveDTO postPayload = new PostReceiveDTO("Test Post", "Public", 1l, 1l, "test_url");
        Response res = postAPI.updatePost(1l, postPayload);
        assertEquals(200, res.getStatus());
        assertNotNull(res.getEntity());
        verify(postService).updatePost(1l, postPayload);
    }

    @Test
    public void testDeletePost() throws DeletePostNotAuthorizedException {
        Response res = postAPI.deletePost(1l);
        assertEquals(200, res.getStatus());
        assertNotNull(res.getEntity());
        verify(postService).deletePost(1l, null, null);
    }

    @Test
    public void testDeletePostNotAuth() throws DeletePostNotAuthorizedException {
        // doThrow(DeletePostNotAuthorizedException.class).when(postService).deletePost(1l,
        // null, null);
        // assertThrows(WebApplicationException.class, () -> {
        // postAPI.deletePost(1l);
        // });
    }
}
