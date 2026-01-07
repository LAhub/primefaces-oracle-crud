
package com.xilosoft.tareas.service;

import com.xilosoft.tareas.model.User;
import com.xilosoft.tareas.repository.UserRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class UserService {

    @EJB
    private UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public User findById(Long id) {
        return repository.findById(id);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public List<User> search(String filter) {
        if (filter == null || filter.trim().isEmpty()) {
            return findAll();
        }
        return repository.findByUsernameContainingIgnoreCase(filter);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public long count() {
        return repository.count();
    }
}
