package com.everett.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.everett.daos.PostDAO;
import com.everett.exceptions.checkedExceptions.EmptyEntityException;
import com.everett.models.Major;
import com.everett.models.Post;
import com.everett.models.Topic;
import com.everett.models.User;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PostServiceImpTest {
    @InjectMocks
    PostServiceImp postService;

    @Mock
    PostDAO postDAO;

    private Post post1;
    // private Post post2;

    private Topic topic;
    private Major major;
    private User user;

    @BeforeAll
    public void setUp() {
        this.topic = new Topic("Test Topic", "Test Topic Des", 1l);
        this.major = new Major("Test Major", "Test Major Des", 1l);
        Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        this.user = new User("B1910421", "Nhan", "Le", "test@mail.com", createdTime, "active");
        this.post1 = new Post(user, createdTime, "Test Post 1", "public", topic, major);
        // this.post2 = new Post(user, createdTime, "Test Post 2", "public", topic,
        // major);
    }

    @Test
    public void testGetPostById() throws EmptyEntityException {
        Long id = 1L;
        when(postDAO.getPostById(id)).thenReturn(post1);
        Post actual = postService.getPostById(id);
        assertEquals(post1, actual);
    }

    @Test
    public void testGetPostByIdNotFound() {
        assertThrows(EmptyEntityException.class, () -> {
            postService.getPostById(-1L);
        });
    }

    @Test
    public void testGetPostByIdNotFound2() throws EmptyEntityException {
        Long id = 1L;
        when(postDAO.getPostById(id)).thenThrow(EmptyEntityException.class);
        assertThrows(EmptyEntityException.class, () -> {
            postService.getPostById(id);
        });
    }
}
