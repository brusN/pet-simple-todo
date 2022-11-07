package ru.nsu.brusn.smpltodo.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Roles")
@NoArgsConstructor
public class RoleEntity {
    @Id
    @SequenceGenerator(name = "roles_sequence", sequenceName = "roles_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "roles_sequence")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ERole role;

    public RoleEntity(ERole role) {
        this.role = role;
    }
}
