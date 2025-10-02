package com.kernotec.farm.activity.jpa.dto.mapper;

import com.kernotec.farm.activity.jpa.dto.entity.CommentDto;
import com.kernotec.farm.activity.jpa.entity.Comment;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentDtoActivityMapper {

    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    CommentDto toDto(Comment comment);

    List<CommentDto> toDto(List<Comment> commentList);

    Set<CommentDto> toDto(Set<Comment> commentSet);
}
