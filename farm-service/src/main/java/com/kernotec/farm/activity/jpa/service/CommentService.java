package com.kernotec.farm.activity.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.activity.jpa.entity.Comment;
import com.kernotec.farm.activity.jpa.repository.CommentRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CommentService extends BaseServiceImpl<Comment, UUID> {

    private final CommentRepository repository;

    @Override
    protected String resourceName() {
        return "Comment";
    }

    @Override
    protected BaseRepository<Comment, UUID> repository() {
        return repository;
    }
}
