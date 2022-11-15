package ru.nsu.brusn.smpltodo.model.dto.request;

import com.sun.istack.NotNull;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.Date;

@Getter
public class CreateNewTaskRequest {
    @NotNull
    private Long folderId;

    @NotNull
    private String name;

    private ZonedDateTime startDate;
    private ZonedDateTime deadline;
    private String description;
}
