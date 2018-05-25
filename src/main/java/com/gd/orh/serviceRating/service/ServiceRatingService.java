package com.gd.orh.serviceRating.service;

import com.gd.orh.entity.ServiceRating;

public interface ServiceRatingService {
    ServiceRating save(ServiceRating serviceRating);

    ServiceRating findById(Long id);

    ServiceRating rateDriver(ServiceRating serviceRating);

    ServiceRating ratePassenger(ServiceRating serviceRating);
}
