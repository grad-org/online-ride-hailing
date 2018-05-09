package com.gd.orh.serviceRating.service;

import com.gd.orh.entity.DriverComplaint;
import com.gd.orh.entity.DriverRating;
import com.gd.orh.entity.PassengerComplaint;
import com.gd.orh.entity.PassengerRating;
import com.gd.orh.mapper.DriverComplaintMapper;
import com.gd.orh.mapper.DriverRatingMapper;
import com.gd.orh.mapper.PassengerComplaintMapper;
import com.gd.orh.mapper.PassengerRatingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ServiceRatingServiceImpl implements ServiceRatingService {

    @Autowired
    private DriverRatingMapper driverRatingMapper;

    @Autowired
    private PassengerRatingMapper passengerRatingMapper;

    @Autowired
    private DriverComplaintMapper driverComplaintMapper;

    @Autowired
    private PassengerComplaintMapper passengerComplaintMapper;

    @Override
    public DriverRating rateDriver(DriverRating driverRating) {
        driverRatingMapper.insertDriverRating(driverRating);

        return driverRating;
    }

    @Override
    public PassengerRating ratePassenger(PassengerRating passengerRating) {
        passengerRatingMapper.insertPassengerRating(passengerRating);

        return passengerRating;
    }

    @Override
    @Transactional(readOnly = true)
    public DriverRating findDriverRatingById(Long id) {
        return driverRatingMapper.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PassengerRating findPassengerRatingById(Long id) {
        return passengerRatingMapper.findById(id);
    }

    @Override
    public DriverComplaint complainDriver(DriverComplaint driverComplaint) {
        driverComplaintMapper.insertDriverComplaint(driverComplaint);

        return driverComplaint;
    }

    @Override
    public PassengerComplaint complainPassenger(PassengerComplaint passengerComplaint) {
        passengerComplaintMapper.insertPassengerComplaint(passengerComplaint);

        return passengerComplaint;
    }

    @Override
    public DriverComplaint findDriverComplaintById(Long id) {
        return driverComplaintMapper.findById(id);
    }

    @Override
    public PassengerComplaint findPassengerComplaintById(Long id) {
        return passengerComplaintMapper.findById(id);
    }
}
