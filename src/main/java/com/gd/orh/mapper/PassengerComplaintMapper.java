package com.gd.orh.mapper;

import com.gd.orh.entity.PassengerComplaint;

public interface PassengerComplaintMapper {
    void insertPassengerComplaint(PassengerComplaint passengerComplaint);

    PassengerComplaint findById(Long id);
}