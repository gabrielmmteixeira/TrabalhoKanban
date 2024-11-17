package com.example.kanban.model;

public enum Status {
    TODO("A fazer"),
    IN_PROGRESS("Em progresso"),
    DONE("Feito");

    private String kanbanStatus;

    Status(String kanbanStatus) {
        this.kanbanStatus = kanbanStatus;
    }

    public String getDescricao() {
        return kanbanStatus;
    }
}
