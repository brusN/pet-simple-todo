package ru.nsu.brusn.smpltodo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "Tasks")
public class TaskEntity {
    @Id
    @SequenceGenerator(name = "tasks_sequence", sequenceName = "tasks_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "tasks_sequence")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Date created;

    private Date startDate;
    private Date deadline;
    private String description;

    private boolean important;
    private boolean completed;

    @ManyToOne
    @JsonBackReference
    private FolderEntity folder;
}
