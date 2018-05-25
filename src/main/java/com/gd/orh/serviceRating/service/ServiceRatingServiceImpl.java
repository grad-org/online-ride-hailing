package com.gd.orh.serviceRating.service;

import com.gd.orh.entity.ServiceRating;
import com.gd.orh.mapper.ServiceRatingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ServiceRatingServiceImpl implements ServiceRatingService {

    @Autowired
    private ServiceRatingMapper serviceRatingMapper;

    @Override
    public ServiceRating save(ServiceRating serviceRating) {
        serviceRatingMapper.insertServiceRating(serviceRating);

        return serviceRating;
    }

    @Override
    public ServiceRating findById(Long id) {
        return serviceRatingMapper.findById(id);
    }

    @Override
    public ServiceRating rateDriver(ServiceRating serviceRating) {
        serviceRatingMapper.updatePassengerRating(serviceRating);

        return this.findById(serviceRating.getId());
    }

    @Override
    public ServiceRating ratePassenger(ServiceRating serviceRating) {
        serviceRatingMapper.updatePassengerRating(serviceRating);

        return this.findById(serviceRating.getId());
    }
}
