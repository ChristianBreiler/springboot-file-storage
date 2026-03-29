package com.example.springbootfilestorage.dto.mappers;

import com.example.springbootfilestorage.dao.Folder;
import com.example.springbootfilestorage.dto.folder.FolderDTO;
import com.example.springbootfilestorage.dto.folder.ParentFolderDTO;
import com.example.springbootfilestorage.dto.summary.FolderSummaryDTO;
import com.example.springbootfilestorage.security.usercontext.UserContext;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;

@Service
public class FolderDTOMapper implements Function<Folder, FolderDTO> {

    private final UserContext userContext;
    private final UploadFileDTOMapper uploadFileDTOMapper;

    public FolderDTOMapper(UserContext userContext, UploadFileDTOMapper uploadFileDTOMapper) {
        this.userContext = userContext;
        this.uploadFileDTOMapper = uploadFileDTOMapper;
    }

    @Override
    public FolderDTO apply(Folder folder) {
        if (folder == null) return null;
        return new FolderDTO(
                folder.getId(),
                folder.getName(),
                userContext.getAuthenticatedUser().getId(),
                // Do this so the breadcrumb order is correct and not reversed
                folder.allParents().stream().map(this::createParentFolderDTO).collect(Collector.of(LinkedList::new,
                        LinkedList::addFirst, (a, b) -> {
                            b.addAll(a);
                            return b;
                        })),
                folder.getSubfolders() != null ?
                        folder.getSubfolders().stream().map(this::createFolderSummaryDTO).toList() : List.of(),
                folder.getFiles() != null ?
                        folder.getFiles().stream().filter(f -> !f.isDeleted())
                                .map(uploadFileDTOMapper).toList() : List.of()
        );
    }

    private ParentFolderDTO createParentFolderDTO(Folder folder) {
        if (folder == null) return null;
        return new ParentFolderDTO(folder.getId(), folder.getName());
    }

    private FolderSummaryDTO createFolderSummaryDTO(Folder folder) {
        return new FolderSummaryDTO(folder.getId(), folder.getName());
    }
}
