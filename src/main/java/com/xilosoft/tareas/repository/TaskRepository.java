package com.xilosoft.tareas.repository;

import com.xilosoft.tareas.model.Task;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Stateless
public class TaskRepository {

    @PersistenceContext(unitName = "primaryPU")
    private EntityManager em;

    public Task save(Task task) {
        if (task.getId() == null) {
            em.persist(task);
            return task;
        } else {
            return em.merge(task);
        }
    }

    public Task findById(Long id) {
        return em.find(Task.class, id);
    }

    public List<Task> findAll() {
        TypedQuery<Task> query = em.createQuery(
                "SELECT t FROM Task t ORDER BY t.dueDate ASC NULLS LAST, t.createdDate DESC",
                Task.class
        );
        return query.getResultList();
    }

    public List<Task> findByTitleContainingIgnoreCase(String title) {
        TypedQuery<Task> query = em.createQuery(
                "SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(:title) ORDER BY t.dueDate ASC NULLS LAST",
                Task.class
        );
        query.setParameter("title", "%" + title + "%");
        return query.getResultList();
    }

    public List<Task> findByCompleted(Boolean completed) {
        TypedQuery<Task> query = em.createQuery(
                "SELECT t FROM Task t WHERE t.completed = :completed ORDER BY t.dueDate ASC NULLS LAST",
                Task.class
        );
        query.setParameter("completed", completed);
        return query.getResultList();
    }

    public List<Task> findOverdueTasks() {
        TypedQuery<Task> query = em.createQuery(
                "SELECT t FROM Task t WHERE t.completed = false AND t.dueDate < CURRENT_DATE ORDER BY t.dueDate ASC",
                Task.class
        );
        return query.getResultList();
    }

    public List<Task> findTasksDueToday() {
        TypedQuery<Task> query = em.createQuery(
                "SELECT t FROM Task t WHERE t.completed = false AND t.dueDate = CURRENT_DATE ORDER BY t.createdDate DESC",
                Task.class
        );
        return query.getResultList();
    }

    public List<Task> findPendingTasks() {
        return findByCompleted(false);
    }

    public List<Task> findCompletedTasks() {
        return findByCompleted(true);
    }

    public void delete(Task task) {
        if (!em.contains(task)) {
            task = em.merge(task);
        }
        em.remove(task);
    }

    public void deleteById(Long id) {
        Task task = findById(id);
        if (task != null) {
            em.remove(task);
        }
    }

    public long count() {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(t) FROM Task t",
                Long.class
        );
        return query.getSingleResult();
    }

    public long countByCompleted(Boolean completed) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(t) FROM Task t WHERE t.completed = :completed",
                Long.class
        );
        query.setParameter("completed", completed);
        return query.getSingleResult();
    }

    public long countOverdue() {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(t) FROM Task t WHERE t.completed = false AND t.dueDate < CURRENT_DATE",
                Long.class
        );
        return query.getSingleResult();
    }
}