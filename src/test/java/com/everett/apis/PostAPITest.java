package com.everett.apis;

import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.Instant;
// import java.time.ZonedDateTime;
// import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

// import javax.ws.rs.WebApplicationException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.everett.dtos.PostDTO;
import com.everett.services.PostService;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS) // When using @BeforeAll method as non-static
public class PostAPITest {
    @InjectMocks
    PostAPI api;

    @Mock
    PostService service;

    private Timestamp createdTime;
    // private static final DateTimeFormatter DATE_TIME_FORMATTER =
    // DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    @BeforeAll
    public void setUp() {
        createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void testcreateLeaveRequestTest() {
        PostDTO payload = new PostDTO(1910421l, "Test Post", "public", 1l, 1l);
        assertEquals(200, api.createPost(payload).getStatus());
    }
}
