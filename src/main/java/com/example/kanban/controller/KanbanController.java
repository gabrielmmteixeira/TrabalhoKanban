package com.example.kanban.controller;

import com.example.kanban.model.Kanban;
import com.example.kanban.service.KanbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kanban")
public class KanbanController {

    private final KanbanService kanbanService;

    @Autowired
    public KanbanController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }

    @PostMapping
    public Kanban criarKanban(@RequestBody Kanban kanban) {
        return kanbanService.criarKanban(kanban);
    }

    @GetMapping
    public List<Kanban> listarKanbans() {
        return kanbanService.listarKanbans();
    }

    @GetMapping("/{id}")
    public Kanban selecionarKanbanPorId(@PathVariable int id) {
        return kanbanService.selecionarKanbanPorId(id);
    }

    @PutMapping("/{id}/mover")
    public Kanban moverKanban(@PathVariable int id) {
        return kanbanService.moverKanban(id);
    }

    @DeleteMapping("/{id}")
    public void deletarKanban(@PathVariable int id) {
        kanbanService.deletarKanban(id);
    }

    @PatchMapping("/{id}")
    public Kanban atualizarKanban(@PathVariable int id, @RequestBody Kanban kanbanAtualizado) {
        Kanban kanbanExistente = kanbanService.selecionarKanbanPorId(id);

        if (kanbanAtualizado.getTitulo() != null) {
            kanbanExistente.setTitulo(kanbanAtualizado.getTitulo());
        }
        if (kanbanAtualizado.getDescricao() != null) {
            kanbanExistente.setDescricao(kanbanAtualizado.getDescricao());
        }
        if (kanbanAtualizado.getPrioridade() != null) {
            kanbanExistente.setPrioridade(kanbanAtualizado.getPrioridade());
        }
        if (kanbanAtualizado.getDataLimite() != null) {
            kanbanExistente.setDataLimite(kanbanAtualizado.getDataLimite());
        }

        if (kanbanAtualizado.getStatus() != null) {
            kanbanExistente.setStatus(kanbanAtualizado.getStatus());
        }

        return kanbanService.criarKanban(kanbanExistente);
    }
}
