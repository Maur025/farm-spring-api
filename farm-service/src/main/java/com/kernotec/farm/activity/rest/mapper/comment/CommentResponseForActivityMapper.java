package com.kernotec.farm.activity.rest.mapper.comment;

import com.kernotec.farm.activity.jpa.entity.Comment;
import com.kernotec.farm.activity.rest.dto.response.comment.CommentResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentResponseForActivityMapper {

    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    CommentResponse toResponse(Comment comment);

    CommentResponse toResponse(UUID id);

    List<CommentResponse> toResponse(List<Comment> commentList);

    Set<CommentResponse> toResponse(Set<Comment> commentSet);
}
