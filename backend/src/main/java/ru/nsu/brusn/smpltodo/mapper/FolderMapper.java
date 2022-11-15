package ru.nsu.brusn.smpltodo.mapper;

import org.springframework.stereotype.Component;
import ru.nsu.brusn.smpltodo.model.dto.response.UserFolderResponse;
import ru.nsu.brusn.smpltodo.model.entity.FolderEntity;

@Component
public class FolderMapper implements IMapper<FolderEntity, UserFolderResponse> {
    @Override
    public UserFolderResponse getMapped(FolderEntity object) {
        var userFolderResponse = new UserFolderResponse();
        userFolderResponse.setId(object.getId());
        userFolderResponse.setName(object.getName());
        return userFolderResponse;
    }
}
