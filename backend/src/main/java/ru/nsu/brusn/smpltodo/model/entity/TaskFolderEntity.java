package ru.nsu.brusn.smpltodo.model.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class TaskFolderEntity {
    @Id
    @SequenceGenerator(name = "folders_sequence", sequenceName = "folders_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "folders_sequence")
    private Long id;

    @NotNull
    private String name;
}
