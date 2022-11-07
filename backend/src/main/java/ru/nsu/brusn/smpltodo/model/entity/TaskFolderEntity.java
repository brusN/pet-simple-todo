package ru.nsu.brusn.smpltodo.model.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class TaskFolderEntity {
    @Id
    @SequenceGenerator(name = "folders_sequence", sequenceName = "folders_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "folders_sequence")
    private Long id;

    @NotNull
    private String name;
}
