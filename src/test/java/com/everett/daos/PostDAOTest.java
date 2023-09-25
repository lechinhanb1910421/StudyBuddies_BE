// package com.everett.daos;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.sql.Timestamp;
// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
// import java.util.ArrayList;
// import java.util.List;

// import javax.persistence.EntityManager;
// import javax.persistence.TypedQuery;

// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import org.junit.jupiter.api.TestInstance.Lifecycle;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import com.everett.exceptions.checkedExceptions.EmptyEntityException;
// import com.everett.models.Major;
// import com.everett.models.Post;
// import com.everett.models.Topic;
// import com.everett.models.User;

// @ExtendWith(MockitoExtension.class)
// @TestInstance(Lifecycle.PER_CLASS)
// public class PostDAOTest {
//     @InjectMocks
//     PostDAO postDAO;

//     @Mock
//     EntityManager entityManager;

//     @Mock
//     TypedQuery<Post> typedQueryPost;

//     @Mock
//     TypedQuery<Long> typedQueryLong;

//     private Post post1;
//     private Post post2;

//     private Topic topic;
//     private Major major;
//     private User user;

//     @BeforeAll
//     public void setUp() {
//         this.topic = new Topic("Test Topic", "Test Topic Des", 1l);
//         this.major = new Major("Test Major", "Test Major Des", 1l);
//         Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
//         this.user = new User("B1910421", "Nhan", "Le", "test@mail.com", createdTime, "active");
//         this.post1 = new Post(user, createdTime, "Test Post 1", "public", topic, major);
//         this.post2 = new Post(user, createdTime, "Test Post 2", "public", topic, major);
//     }

//     // @Test
//     // public void testCreatePost() {
//     // postDAO.createPost(post1);
//     // verify(entityManager, times(1)).persist(post1);
//     // }

//     @Test
//     public void testgetAllPostsSize() {
//         List<Post> posts = new ArrayList<Post>();
//         posts.add(post1);
//         posts.add(post2);
//         when(entityManager.createQuery("FROM posts p ORDER BY p.post_id DESC", Post.class)).thenReturn(typedQueryPost);
//         when(typedQueryPost.getResultList()).thenReturn(posts);
//         List<Post> actualPosts = postDAO.getAllPosts();
//         assertEquals(posts.size(), actualPosts.size());
//         verify(entityManager, times(1)).createQuery("FROM posts p ORDER BY p.post_id DESC", Post.class);
//         verify(typedQueryPost, times(1)).getResultList();
//     }

//     @Test
//     public void testgetAllPostsActualData() {
//         List<Post> posts = new ArrayList<Post>();
//         posts.add(post1);
//         posts.add(post2);
//         when(entityManager.createQuery("FROM posts p ORDER BY p.post_id DESC", Post.class)).thenReturn(typedQueryPost);
//         when(typedQueryPost.getResultList()).thenReturn(posts);
//         List<Post> actualPosts = postDAO.getAllPosts();
//         assertEquals(posts.size(), actualPosts.size());
//         Post actualFirst = actualPosts.get(0);
//         assertEquals(actualFirst.getContent(), post1.getContent());
//         assertEquals(actualFirst.getCreatedTime(), post1.getCreatedTime());
//         verify(entityManager, times(1)).createQuery("FROM posts p ORDER BY p.post_id DESC", Post.class);
//         verify(typedQueryPost, times(1)).getResultList();
//     }

//     @Test
//     public void testgetUserPostsSize() {
//         String testMail = "test@mail.com";
//         List<Post> posts = new ArrayList<Post>();
//         posts.add(post1);
//         posts.add(post2);
//         when(entityManager.createQuery("FROM posts p WHERE p.user.email = :email ORDER BY p.post_id DESC", Post.class))
//                 .thenReturn(typedQueryPost);
//         when(typedQueryPost.setParameter("email", testMail)).thenReturn(typedQueryPost);
//         when(typedQueryPost.getResultList()).thenReturn(posts);
//         List<Post> actual = postDAO.getAllUserPosts(testMail);
//         assertEquals(posts.size(), actual.size());
//         verify(entityManager, times(1)).createQuery("FROM posts p WHERE p.user.email = :email ORDER BY p.post_id DESC",
//                 Post.class);
//         verify(typedQueryPost, times(1)).getResultList();
//     }

//     @Test
//     public void testgetUserPostsActualData() {
//         String testMail = "test@mail.com";
//         List<Post> posts = new ArrayList<Post>();
//         posts.add(post1);
//         posts.add(post2);
//         when(entityManager.createQuery("FROM posts p WHERE p.user.email = :email ORDER BY p.post_id DESC", Post.class))
//                 .thenReturn(typedQueryPost);
//         when(typedQueryPost.setParameter("email", testMail)).thenReturn(typedQueryPost);
//         when(typedQueryPost.getResultList()).thenReturn(posts);
//         List<Post> actual = postDAO.getAllUserPosts(testMail);
//         Post actualFirst = actual.get(0);
//         assertEquals(actualFirst.getContent(), post1.getContent());
//         assertEquals(actualFirst.getCreatedTime(), post1.getCreatedTime());
//         verify(entityManager, times(1)).createQuery("FROM posts p WHERE p.user.email = :email ORDER BY p.post_id DESC",
//                 Post.class);
//         verify(typedQueryPost, times(1)).getResultList();
//     }

//     @Test
//     public void testGetPostById() throws EmptyEntityException{
//         when(entityManager.find(Post.class, 1L)).thenReturn(post1);
//         assertEquals(post1, postDAO.getPostById(1L));
//         verify(entityManager, times(1)).find(Post.class, 1L);
//     }

//     @Test
//     public void testGetPostByIdReturnNullResult() {
//         when(entityManager.find(Post.class, 1L)).thenReturn(null);
//         assertThrows(EmptyEntityException.class, ()->{
//             postDAO.getPostById(1L);
//         });
//         verify(entityManager, times(1)).find(Post.class, 1L);

//     }

//     @Test
//     public void testDeletePost() {
//         when(entityManager.find(Post.class, 1L)).thenReturn(post1);
//         postDAO.deletePost(1L);
//         verify(entityManager, times(1)).remove(post1);
//     }

//     @Test
//     public void testUpdatePost() {
//         postDAO.updatePost(post1);
//         verify(entityManager, times(1)).merge(post1);
//     }

//     @Test
//     public void testReactPost() {
//         when(entityManager.find(Post.class, 1L)).thenReturn(post1);
//         postDAO.reactPost(1L, user);
//         verify(entityManager, times(1)).merge(post1);
//     }

//     @Test
//     public void testRemoveReactPost() {
//         when(entityManager.find(Post.class, 1L)).thenReturn(post1);
//         postDAO.reactPost(1L, user);
//         verify(entityManager, times(1)).merge(post1);

//     }

//     @Test
//     public void testGetAllPostReactionsCountData() {
//         String queryString = "SELECT COUNT(*) FROM posts p JOIN p.reactions r WHERE p.post_id = :postId";
//         when(entityManager.createQuery(queryString, Long.class)).thenReturn(typedQueryLong);
//         when(typedQueryLong.setParameter("postId", 1L)).thenReturn(typedQueryLong);
//         when(typedQueryLong.getSingleResult()).thenReturn(27L);
//         Long result = postDAO.getAllPostReactionsCount(1L);
//         assertEquals(result, 27L);
//     }

//     @Test
//     public void testGetAllPostReactionsCountFuncsCall() {
//         String queryString = "SELECT COUNT(*) FROM posts p JOIN p.reactions r WHERE p.post_id = :postId";
//         when(entityManager.createQuery(queryString, Long.class)).thenReturn(typedQueryLong);
//         when(typedQueryLong.setParameter("postId", 1L)).thenReturn(typedQueryLong);
//         when(typedQueryLong.getSingleResult()).thenReturn(27L);
//         postDAO.getAllPostReactionsCount(1L);
//         verify(entityManager, times(1)).createQuery(queryString, Long.class);
//         verify(typedQueryLong, times(1)).setParameter("postId", 1L);
//         verify(typedQueryLong, times(1)).getSingleResult();
//     }

//     @Test
//     public void testGetAllPostCommentsCountData() {
//         String queryString = "SELECT COUNT(*) FROM posts p JOIN p.comments c WHERE p.post_id = :postId";
//         when(entityManager.createQuery(queryString, Long.class)).thenReturn(typedQueryLong);
//         when(typedQueryLong.setParameter("postId", 1L)).thenReturn(typedQueryLong);
//         when(typedQueryLong.getSingleResult()).thenReturn(27L);
//         Long result = postDAO.getAllPostCommentsCount(1L);
//         assertEquals(result, 27L);
//     }

//     @Test
//     public void testGetAllPostCommentsCountFuncsCall() {
//         String queryString = "SELECT COUNT(*) FROM posts p JOIN p.comments c WHERE p.post_id = :postId";
//         when(entityManager.createQuery(queryString, Long.class)).thenReturn(typedQueryLong);
//         when(typedQueryLong.setParameter("postId", 1L)).thenReturn(typedQueryLong);
//         when(typedQueryLong.getSingleResult()).thenReturn(27L);
//         postDAO.getAllPostCommentsCount(1L);
//         verify(entityManager, times(1)).createQuery(queryString, Long.class);
//         verify(typedQueryLong, times(1)).setParameter("postId", 1L);
//         verify(typedQueryLong, times(1)).getSingleResult();
//     }
// }
