package com.example.kanban.service;

import com.example.kanban.model.Kanban;
import com.example.kanban.model.Prioridade;
import com.example.kanban.model.Status;
import com.example.kanban.repository.KanbanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KanbanService {
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

    public Kanban moverKanban(int id) {
        Kanban kanban = selecionarKanbanPorId(id);
        if (kanban.getStatus() == Status.TODO) {
            kanban.setStatus(Status.IN_PROGRESS);
        } else if (kanban.getStatus() == Status.IN_PROGRESS) {
            kanban.setStatus(Status.DONE);
        }
        return kanbanRepository.save(kanban);
    }

    public List<Kanban> listarKanbans() {
        return kanbanRepository.findAll();
    }

    public Kanban selecionarKanbanPorId(int id) {
        Optional<Kanban> kanbanOpt = kanbanRepository.findById(id);
        return kanbanOpt.orElseThrow(() -> new RuntimeException("Kanban não encontrado."));
    }

    public Map<Status, List<Kanban>> listarKanbansPorColuna() {
        List<Kanban> kanbans = kanbanRepository.findAll();
        return kanbans.stream()
                .sorted(Comparator.comparing(Kanban::getPrioridade))
                .collect(Collectors.groupingBy(Kanban::getStatus));
    }

    public List<Kanban> filtrarKanbansPorPrioridade(Prioridade prioridade) {
        return kanbanRepository.findAll().stream()
                .filter(kanban -> kanban.getPrioridade() == prioridade)
                .collect(Collectors.toList());
    }

    public List<Kanban> filtrarKanbansPorDataLimite() {
        return kanbanRepository.findAll().stream()
                .filter(kanban -> LocalDate.parse(kanban.getDataLimite(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    public Map<Status, List<Kanban>> gerarRelatorio() {
        List<Kanban> kanbans = kanbanRepository.findAll();
        Map<Status, List<Kanban>> kanbansPorColuna = kanbans.stream()
                .sorted(Comparator.comparing(Kanban::getPrioridade))
                .collect(Collectors.groupingBy(Kanban::getStatus));

        kanbansPorColuna.forEach((status, listaKanbans) -> {
            listaKanbans.forEach(kanban -> {
                if (!kanban.getStatus().equals(Status.DONE) && LocalDate.parse(kanban.getDataLimite(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).isBefore(LocalDate.now())) {
                    kanban.setDescricao(kanban.getDescricao() + " (Atrasada)");
                }
            });
        });

        return kanbansPorColuna;
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