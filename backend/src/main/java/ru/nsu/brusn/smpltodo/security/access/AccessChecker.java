package ru.nsu.brusn.smpltodo.security.access;

import ru.nsu.brusn.smpltodo.model.entity.FolderEntity;
import ru.nsu.brusn.smpltodo.model.entity.UserEntity;

import java.util.Objects;

public class AccessChecker {
    public static boolean hasAccessToFolder(UserEntity user, FolderEntity folder) {
        return Objects.equals(folder.getUser().getId(), user.getId());
    }
}
