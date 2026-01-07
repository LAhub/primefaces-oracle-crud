package com.xilosoft.tareas.service;

import com.xilosoft.tareas.model.Task;
import com.xilosoft.tareas.repository.TaskRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;

@Stateless
public class TaskService {

    @EJB
    private TaskRepository repository;

    public Task save(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        return repository.save(task);
    }

    public Task findById(Long id) {
        return repository.findById(id);
    }

    public List<Task> findAll() {
        return repository.findAll();
    }

    public List<Task> search(String filter) {
        if (filter == null || filter.trim().isEmpty()) {
            return findAll();
        }
        return repository.findByTitleContainingIgnoreCase(filter);
    }

    public void delete(Task task) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("La tarea no es válida");
        }
        repository.delete(task);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        repository.deleteById(id);
    }

    public Task toggleCompleted(Task task) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("La tarea no es válida");
        }
        Task existingTask = repository.findById(task.getId());
        if (existingTask != null) {
            existingTask.setCompleted(!existingTask.getCompleted());
            return repository.save(existingTask);
        }
        return null;
    }

    public List<Task> findPendingTasks() {
        return repository.findPendingTasks();
    }

    public List<Task> findCompletedTasks() {
        return repository.findCompletedTasks();
    }

    public List<Task> findOverdueTasks() {
        return repository.findOverdueTasks();
    }

    public List<Task> findTasksDueToday() {
        return repository.findTasksDueToday();
    }

    public long count() {
        return repository.count();
    }

    public long countPending() {
        return repository.countByCompleted(false);
    }

    public long countCompleted() {
        return repository.countByCompleted(true);
    }

    public long countOverdue() {
        return repository.countOverdue();
    }
}