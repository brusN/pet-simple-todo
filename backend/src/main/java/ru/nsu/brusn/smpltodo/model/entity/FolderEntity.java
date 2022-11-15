package ru.nsu.brusn.smpltodo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "Folders")
public class FolderEntity {
    @Id
    @SequenceGenerator(name = "folders_sequence", sequenceName = "folders_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "folders_sequence")
    private Long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "folder", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<TaskEntity> tasks = new ArrayList<>();

    @ManyToOne
    @JsonBackReference
    private UserEntity user;
}
