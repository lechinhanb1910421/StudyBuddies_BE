package com.everett.daos;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.everett.exceptions.EmptyEntityException;
import com.everett.models.Post;

@Stateless(name = "PostDAO")
public class PostDAO {
    @PersistenceContext(unitName = "primary")
    EntityManager entityManager;

    public void createPost(Post newPost) {
        try {
            entityManager.persist(newPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Post getPostById(Long id) throws EmptyEntityException {
        Post post = entityManager.find(Post.class, id);
        if (post == null) {
            throw new EmptyEntityException(id);
        } else {
            return post;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Post> seachPostsByKeywords(String keywords) {
        List<Post> list = null;
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Post.class)
                .get();
        String keywordMatch = "";
        for (String word : keywords.split(" ")) {
            keywordMatch += word + "* ";
        }
        Query searchKeyword = queryBuilder
                .simpleQueryString()
                .onField("content")
                // .boostedTo(5f)
                // .andFields("userId")
                // .boostedTo(2f)
                .withAndAsDefaultOperator()
                .matching(keywordMatch)
                .createQuery();
        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(searchKeyword, Post.class);
        list = jpaQuery.getResultList();
        return list;
    }

    public List<Post> getAllPosts() {
        List<Post> resList = null;
        try {
            TypedQuery<Post> postQuery = entityManager
                    .createQuery("FROM Posts p ORDER BY p.postId", Post.class);
            resList = postQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resList;
    }

    public void deletePost(Long id) {
        try {
            entityManager.remove(entityManager.find(Post.class, id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePost(Post post) {
        try {
            entityManager.merge(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
