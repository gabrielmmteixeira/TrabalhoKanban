package com.example.kanban.service;

import com.example.kanban.model.Kanban;
import com.example.kanban.model.Status;
import com.example.kanban.repository.KanbanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class KanbanService {
teste commit
    private KanbanRepository kanbanRepository;

    @Autowired
    public KanbanService(KanbanRepository kanbanRepository) {
        this.kanbanRepository = kanbanRepository;
    }

    public Kanban criarKanban(Kanban kanban) {
        if (kanban.getDataLimite() != null) {
            validarDataLimite(kanban.getDataLimite());
        }
        kanban.setDataCriacao(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        return kanbanRepository.save(kanban);
    }

    public List<Kanban> listarKanbans() {
        return kanbanRepository.findAll();
    }

    public Kanban selecionarKanbanPorId(int id) {
        Optional<Kanban> kanbanOpt = kanbanRepository.findById(id);
        return kanbanOpt.orElseThrow(() -> new RuntimeException("Kanban não encontrado."));
    }

    public Kanban moverKanban(int id) {
        Kanban kanban = selecionarKanbanPorId(id);
        Status statusAtual = kanban.getStatus();
        switch (statusAtual) {
            case TODO:
                kanban.setStatus(Status.IN_PROGRESS);
                break;
            case IN_PROGRESS:
                kanban.setStatus(Status.DONE);
                break;
            case DONE:
                throw new RuntimeException("A tarefa já está concluída.");
        }
        return kanbanRepository.save(kanban);
    }

    public void deletarKanban(int id) {
        kanbanRepository.deleteById(id);
    }

    private void validarDataLimite(String dataLimite) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate.parse(dataLimite, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data limite inválida! Use o formato 'dd/MM/yyyy'.");
        }
    }
}