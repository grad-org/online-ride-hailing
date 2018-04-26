package com.gd.orh.mapper;

import com.gd.orh.entity.PassengerComplaint;
import com.gd.orh.utils.MyMapper;

public interface PassengerComplaintMapper extends MyMapper<PassengerComplaint> {
    void insertPassengerComplaint(PassengerComplaint passengerComplaint);

    PassengerComplaint findById(Long id);
}