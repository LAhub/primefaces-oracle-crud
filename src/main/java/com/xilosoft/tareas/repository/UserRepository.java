
package com.xilosoft.tareas.repository;

import com.xilosoft.tareas.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UserRepository {

    @PersistenceContext(unitName = "primaryPU")
    private EntityManager em;

    public User save(User user) {
        if (user.getId() == null) {
            em.persist(user);
            return user;
        } else {
            return em.merge(user);
        }
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public List<User> findAll() {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u ORDER BY u.id", User.class);
        return query.getResultList();
    }

    public List<User> findByUsernameContainingIgnoreCase(String username) {
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(:username) ORDER BY u.id",
                User.class);
        query.setParameter("username", "%" + username + "%");
        return query.getResultList();
    }

    public void delete(User user) {
        if (!em.contains(user)) {
            user = em.merge(user);
        }
        em.remove(user);
    }

    public void deleteById(Long id) {
        User user = findById(id);
        if (user != null) {
            em.remove(user);
        }
    }

    public long count() {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(u) FROM User u", Long.class);
        return query.getSingleResult();
    }
}
