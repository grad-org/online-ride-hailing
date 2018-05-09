package com.gd.orh.serviceRating.service;

import com.gd.orh.entity.DriverComplaint;
import com.gd.orh.entity.DriverRating;
import com.gd.orh.entity.PassengerComplaint;
import com.gd.orh.entity.PassengerRating;

public interface ServiceRatingService {
    DriverRating rateDriver(DriverRating driverRating);

    PassengerRating ratePassenger(PassengerRating passengerRating);

    DriverRating findDriverRatingById(Long id);

    PassengerRating findPassengerRatingById(Long id);

    DriverComplaint complainDriver(DriverComplaint driverComplaint);

    PassengerComplaint complainPassenger(PassengerComplaint passengerComplaint);

    DriverComplaint findDriverComplaintById(Long id);

    PassengerComplaint findPassengerComplaintById(Long id);
}
