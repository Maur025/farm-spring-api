package com.kernotec.farm.activity.rest.mapper.comment;

import com.kernotec.farm.activity.jpa.entity.Comment;
import com.kernotec.farm.activity.rest.dto.response.comment.CommentResponse;
import com.kernotec.farm.activity.rest.mapper.activity.ActivityResponseFlatMapper;
import com.kernotec.farm.parametric.rest.mapper.activity.type.ActivityTypeResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {ActivityResponseFlatMapper.class, ActivityTypeResponseFlatMapper.class})
public interface CommentResponseMapper {

    CommentResponse toResponse(Comment comment);

    CommentResponse toResponse(UUID id);

    List<CommentResponse> toResponse(List<Comment> commentList);

    Set<CommentResponse> toResponse(Set<Comment> commentSet);
}
