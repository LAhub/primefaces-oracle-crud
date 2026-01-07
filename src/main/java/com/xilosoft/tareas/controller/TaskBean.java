
package com.xilosoft.tareas.controller;

import com.xilosoft.tareas.model.Task;
import com.xilosoft.tareas.service.TaskService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "taskBean")
@ViewScoped
public class TaskBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private TaskService service;

    private Task task = new Task();
    private List<Task> tasks;
    private String filter;
    private Task selectedTask;
    private String filterType = "ALL"; // ALL, PENDING, COMPLETED, OVERDUE, TODAY

    @PostConstruct
    public void init() {
        loadTasks();
    }

    /**
     * Cargar todas las tareas
     */
    private void loadTasks() {
        switch (filterType) {
            case "PENDING":
                tasks = service.findPendingTasks();
                break;
            case "COMPLETED":
                tasks = service.findCompletedTasks();
                break;
            case "OVERDUE":
                tasks = service.findOverdueTasks();
                break;
            case "TODAY":
                tasks = service.findTasksDueToday();
                break;
            default:
                tasks = service.findAll();
                break;
        }
    }

    /**
     * Guardar una nueva tarea
     */
    public void save() {
        try {
            if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
                addMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "El título es obligatorio");
                return;
            }

            service.save(task);
            task = new Task();
            loadTasks();
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Tarea guardada correctamente");
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar la tarea: " + e.getMessage());
        }
    }

    /**
     * Actualizar una tarea existente
     */
    public void update() {
        try {
            if (selectedTask == null) {
                addMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No hay tarea seleccionada");
                return;
            }

            service.save(selectedTask);
            selectedTask = null;
            loadTasks();
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Tarea actualizada correctamente");
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar la tarea: " + e.getMessage());
        }
    }

    /**
     * Eliminar una tarea
     */
    public void delete(Task t) {
        try {
            service.delete(t);
            loadTasks();
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Tarea eliminada correctamente");
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar la tarea: " + e.getMessage());
        }
    }

    /**
     * Buscar tareas por título
     */
    public void search() {
        try {
            System.out.println("=== DEBUG SEARCH ===");
            System.out.println("Filter value: " + filter);
            
            if (filter == null || filter.trim().isEmpty()) {
                System.out.println("Filter is empty, loading all tasks");
                loadTasks();
            } else {
                System.out.println("Searching tasks with filter: " + filter);
                tasks = service.search(filter);
                System.out.println("Found " + tasks.size() + " tasks");
                
                if (tasks.isEmpty()) {
                    addMessage(FacesMessage.SEVERITY_INFO, "Información", "No se encontraron tareas");
                } else {
                    addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Se encontraron " + tasks.size() + " tareas");
                }
            }
        } catch (Exception e) {
            System.err.println("Error searching: " + e.getMessage());
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al buscar: " + e.getMessage());
        }
    }

    /**
     * Marcar/desmarcar tarea como completada
     */
    public void toggleCompleted(Task t) {
        try {
            service.toggleCompleted(t);
            loadTasks();
            String status = t.getCompleted() ? "completada" : "pendiente";
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Tarea marcada como " + status);
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar la tarea: " + e.getMessage());
        }
    }

    /**
     * Filtrar por tipo (todas, pendientes, completadas)
     */
    public void filterByType() {
        loadTasks();
    }

    /**
     * Limpiar el formulario
     */
    public void clear() {
        task = new Task();
        filter = null;
        selectedTask = null;
        filterType = "ALL";
        loadTasks();
    }

    /**
     * Preparar para edición
     */
    public void prepareEdit(Task t) {
        this.selectedTask = t;
    }

    /**
     * Cancelar edición
     */
    public void cancelEdit() {
        this.selectedTask = null;
    }

    /**
     * Obtener estadísticas
     */
    public long getTotalCount() {
        return service.count();
    }

    public long getPendingCount() {
        return service.countPending();
    }

    public long getCompletedCount() {
        return service.countCompleted();
    }

    public long getOverdueCount() {
        return service.countOverdue();
    }

    /**
     * Obtener fecha mínima (hoy) para el date picker
     */
    public Date getMinDate() {
        return new Date();
    }

    /**
     * Agregar mensaje de FacesMessage
     */
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    // Getters and Setters
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Task getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }
}