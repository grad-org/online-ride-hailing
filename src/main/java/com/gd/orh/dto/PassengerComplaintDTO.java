package com.gd.orh.dto;

import com.gd.orh.entity.Passenger;
import com.gd.orh.entity.PassengerComplaint;
import com.gd.orh.entity.User;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

@Data
public class PassengerComplaintDTO extends BaseDTO<PassengerComplaintDTO, PassengerComplaint> {
    private Long passengerComplaintId;

    private String complaintContent;

    private Long passengerId;

    private Long passengerUserId;

    private String nickname;

    private static class PassengerComplaintDTOConverter extends Converter<PassengerComplaintDTO, PassengerComplaint> {

        @Override
        protected PassengerComplaint doForward(PassengerComplaintDTO passengerComplaintDTO) {
            PassengerComplaint passengerComplaint = new PassengerComplaint();
            BeanUtils.copyProperties(passengerComplaintDTO, passengerComplaint);

            User user = new User();
            user.setId(passengerComplaintDTO.getPassengerUserId());
            user.setNickname(passengerComplaintDTO.getNickname());

            Passenger passenger = new Passenger();
            passenger.setId(passengerComplaintDTO.getPassengerId());
            passenger.setUser(user);

            passengerComplaint.setPassenger(passenger);

            return passengerComplaint;
        }

        @Override
        protected PassengerComplaintDTO doBackward(PassengerComplaint passengerComplaint) {
            PassengerComplaintDTO passengerComplaintDTO = new PassengerComplaintDTO();
            BeanUtils.copyProperties(passengerComplaint, passengerComplaintDTO);

            passengerComplaintDTO.setPassengerComplaintId(Optional
                    .ofNullable(passengerComplaint)
                    .map(PassengerComplaint::getId)
                    .orElse(null));

            passengerComplaintDTO.setPassengerId(Optional
                    .ofNullable(passengerComplaint)
                    .map(PassengerComplaint::getPassenger)
                    .map(Passenger::getId)
                    .orElse(null));

            passengerComplaintDTO.setPassengerUserId(Optional
                    .ofNullable(passengerComplaint)
                    .map(PassengerComplaint::getPassenger)
                    .map(Passenger::getUser)
                    .map(User::getId)
                    .orElse(null));

            passengerComplaintDTO.setNickname(Optional
                    .ofNullable(passengerComplaint)
                    .map(PassengerComplaint::getPassenger)
                    .map(Passenger::getUser)
                    .map(User::getNickname)
                    .orElse(null));

            return passengerComplaintDTO;
        }
    }

    @Override
    public Converter<PassengerComplaintDTO, PassengerComplaint> getConverter() {
        return new PassengerComplaintDTOConverter();
    }
}
