package ru.nsu.brusn.smpltodo.model.dto.request;

import com.sun.istack.NotNull;
import lombok.Getter;

import java.util.Date;

@Getter
public class CreateNewTaskRequest {
    @NotNull
    private Long folderId;

    @NotNull
    private String name;

    private Date startDate;
    private Date deadline;
    private String description;
}
