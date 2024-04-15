package com.dsi.todo.controller;

import com.dsi.todo.dto.TodoCreateDto;
import com.dsi.todo.dto.TodoUpdateDto;
import com.dsi.todo.model.Todo;
import com.dsi.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller()
@RequestMapping(value = "/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @GetMapping("/{id}")
    public String getTodoById(@PathVariable("id") Long id, Model model) {
        Optional<Todo> optionalTodo = todoService.findById(id);
        if (optionalTodo.isEmpty()) {
            return "error";
        }
        Todo todo = optionalTodo.get();
        model.addAttribute("todo", todo);
        return "todo/details";
    }

    @GetMapping
    public String findAll(Model model) {
        List<Todo> todoList =  todoService.findAll();
        model.addAttribute("todos", todoList);
        return "todo/list";
    }

    @PostMapping
    public String createTodo(@ModelAttribute TodoCreateDto todoCreateDto) {
        Date today = new Date();
        Todo newTodo = new Todo(todoCreateDto.getTitle(), todoCreateDto.getDescription(), todoCreateDto.getPriority(), todoCreateDto.getStatus(), today.toString(), today.toString());
        todoService.save(newTodo);
        return "redirect:/todos";
    }

    @PostMapping("/{id}")
    public String updateTodoById(@PathVariable("id") Long id, @ModelAttribute TodoUpdateDto todoUpdateDto) {
        Optional<Todo> optionalTodo = todoService.findById(id);
        if (optionalTodo.isEmpty()) {
            return "error";
        }
        
        Todo todo = optionalTodo.get();
        todo.setDescription(todoUpdateDto.getDescription());
        todo.setTitle(todoUpdateDto.getTitle());
        todo.setPriority(todoUpdateDto.getPriority());
        todo.setStatus(todoUpdateDto.getStatus());
        todo.setUpdatedAt(new Date().toString());

        todoService.save(todo);

        return "redirect:/todos";


    }


    @PostMapping("/delete/{id}")
    public String deleteTodoById(@PathVariable("id") Long id) {
        Optional<Todo> optionalTodo = todoService.findById(id);

        if (optionalTodo.isEmpty()) {
            return "error";
        }

        Todo todo = optionalTodo.get();

        todoService.delete(todo);
        return "redirect:/todos";
    }
}
