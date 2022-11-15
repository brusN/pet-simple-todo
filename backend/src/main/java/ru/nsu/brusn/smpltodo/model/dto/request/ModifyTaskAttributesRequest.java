package ru.nsu.brusn.smpltodo.model.dto.request;

import com.sun.istack.NotNull;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.Date;

@Getter
public class ModifyTaskAttributesRequest {
    private String name;
    private ZonedDateTime startDate;
    private ZonedDateTime deadline;
    private String description;
    private Boolean important;
    private Boolean completed;
    private Long newFolderId;
}
