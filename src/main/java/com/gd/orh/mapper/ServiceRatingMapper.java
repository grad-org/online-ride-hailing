package com.gd.orh.mapper;

import com.gd.orh.entity.ServiceRating;

public interface ServiceRatingMapper {
    void insertServiceRating(ServiceRating serviceRating);

    ServiceRating findById(Long id);

    void updatePassengerRating(ServiceRating serviceRating);

    void updateDriverRating(ServiceRating serviceRating);
}