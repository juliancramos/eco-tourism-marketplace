package com.marketplace.service_review_questions.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.marketplace.service_review_questions.dto.ReviewDTOs.CreateReviewDTO;
import com.marketplace.service_review_questions.dto.ReviewDTOs.ReviewDTO;
import com.marketplace.service_review_questions.dto.ReviewDTOs.UpdateReviewDTO;
import com.marketplace.service_review_questions.model.Review;

public class ReviewMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static ReviewDTO toDto(Review e) {
        if (e == null) {
            return null;
        }
        return new ReviewDTO(
            e.getId(),
            e.getServiceId(),
            e.getUserId(),
            e.getStars(),
            e.getCommentText(),
            e.getCreationDate() != null ? e.getCreationDate().format(FORMATTER) : null
        );
    }

    public static Review toEntity(ReviewDTO dto) {
        if (dto == null) {
            return null;
        }
        Review e = new Review();
        e.setId(dto.id());
        e.setServiceId(dto.serviceId());
        e.setUserId(dto.userId());
        e.setStars(dto.stars());
        e.setCommentText(dto.commentText());
        e.setCreationDate(dto.creationDate() != null ? LocalDateTime.parse(dto.creationDate(), FORMATTER) : null);
        return e;
    }

    public static void applyCreate(Review e, CreateReviewDTO dto, LocalDateTime now){
        e.setServiceId(dto.serviceId());
        e.setUserId(dto.userId());
        e.setStars(dto.stars());
        e.setCommentText(dto.commentText());
        e.setCreationDate(now);
    }

    public static void applyUpdate(Review e, UpdateReviewDTO dto){
        e.setServiceId(dto.serviceId());
        e.setUserId(dto.userId());
        e.setStars(dto.stars());
        e.setCommentText(dto.commentText());
    }
}
