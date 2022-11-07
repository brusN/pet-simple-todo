package ru.nsu.brusn.smpltodo.model.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TaskEntity {
    @Id
    @SequenceGenerator(name = "tasks_sequence", sequenceName = "tasks_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "tasks_sequence")
    private Long id;

    @NotNull
    private String name;

    private Date startDate;
    private Date deadline;
    private String description;

    private boolean important;
    private boolean completed;

    @ManyToOne(fetch = FetchType.EAGER)
    private TaskFolderEntity folder;
}
