package ru.nsu.brusn.smpltodo.model.dto.request;

import com.sun.istack.NotNull;
import lombok.Getter;

@Getter
public class CreateNewFolderRequest {
    @NotNull
    private String name;
}
