package com.example.kanban.controller;

import com.example.kanban.model.Kanban;
import com.example.kanban.model.Prioridade;
import com.example.kanban.model.Status;
import com.example.kanban.service.KanbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/status")
    public Map<Status, List<Kanban>> listarKanbansPorColuna() {
        return kanbanService.listarKanbansPorColuna();
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

    @GetMapping("/prioridade/{prioridade}")
    public List<Kanban> filtrarKanbansPorPrioridade(@PathVariable Prioridade prioridade) {
        return kanbanService.filtrarKanbansPorPrioridade(prioridade);
    }

    @GetMapping("/data-limite")
    public List<Kanban> filtrarKanbansPorDataLimite() {
        return kanbanService.filtrarKanbansPorDataLimite();
    }

    @GetMapping("/relatorio")
    public Map<Status, List<Kanban>> gerarRelatorio() {
        return kanbanService.gerarRelatorio();
    }
}