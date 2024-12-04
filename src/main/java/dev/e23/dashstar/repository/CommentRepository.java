package dev.e23.dashstar.repository;

import dev.e23.dashstar.model.Comment;
import dev.e23.dashstar.util.HibernateUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class CommentRepository {

    public List<Comment> findByArticleId(Integer id) throws PersistenceException {
        EntityManager em = HibernateUtil.getEntityManager();
        List<Comment> comments = null;
        try {
            em.getTransaction().begin();
            comments = em
                    .createQuery("SELECT c FROM Comment c WHERE c.articleId = :articleId", Comment.class)
                    .setParameter("articleId", id)
                    .getResultList();
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw new RuntimeException("", e);
        } finally {
            em.close();
        }
        return comments;
    }

    public void create(Comment comment) throws PersistenceException {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();  // 开始事务
            em.persist(comment);  // 保存评论
            em.getTransaction().commit();  // 提交事务
        } catch (PersistenceException e) {
            em.getTransaction().rollback();  // 当出了异常时回滚事务
            throw new RuntimeException("", e);
        } finally {
            em.close();  // 关闭 EntityManager
        }
    }
    public void delete(Integer id) throws PersistenceException {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Comment comment = em.find(Comment.class, id);
            em.remove(comment);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {  
            em.close();
        }
    }
}
