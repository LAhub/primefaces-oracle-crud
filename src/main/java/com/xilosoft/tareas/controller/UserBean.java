package com.xilosoft.tareas.controller;

import com.xilosoft.tareas.model.User;
import com.xilosoft.tareas.service.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "userBean")
@ViewScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UserService service;

    private User user = new User();
    private List<User> users;
    private String filter;
    private User selectedUser;

    @PostConstruct
    public void init() {
        loadUsers();
    }

    private void loadUsers() {
        users = service.findAll();
    }

    public void save() {
        try {
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                addMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "El nombre de usuario es obligatorio");
                return;
            }
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                addMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "El email es obligatorio");
                return;
            }
            
            service.save(user);
            user = new User();
            loadUsers();
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario guardado correctamente");
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar el usuario: " + e.getMessage());
        }
    }

    public void delete(User u) {
        try {
            service.delete(u);
            loadUsers();
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario eliminado correctamente");
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el usuario: " + e.getMessage());
        }
    }

    public void search() {
        try {
            users = service.search(filter);
            if (users.isEmpty()) {
                addMessage(FacesMessage.SEVERITY_INFO, "Información", "No se encontraron usuarios");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al buscar: " + e.getMessage());
        }
    }

    public void clear() {
        user = new User();
        filter = null;
        loadUsers();
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }
}