package com.gd.orh.hailingService.service;

import com.gd.orh.entity.Fare;
import com.gd.orh.mapper.FareMapper;
import com.gd.orh.mapper.FareRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FareService {

    @Autowired
    private FareMapper fareMapper;

    @Autowired
    private FareRuleMapper fareRuleMapper;

    @Transactional(readOnly = true)
    public Fare findById(Long id) {
        return fareMapper.findById(id);
    }

    @Transactional(readOnly = true)
    public Fare predictFare(Fare fare) {
        Fare predictedFare = new Fare();

        predictedFare.setLengthOfMileage(fare.getLengthOfMileage());
        predictedFare.setLengthOfTime(fare.getLengthOfTime());
        predictedFare.setFareRule(fareRuleMapper.findRecentFareRule());

        return predictedFare;
    }
}
