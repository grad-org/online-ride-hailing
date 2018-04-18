package entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "trip_order")
public class TripOrder {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ACCEPTED_TIME")
    private Date acceptedTime;

    @Column(name = "COMPLETED_TIME")
    private Date completedTime;

    @Column(name = "ORDER_STATUS")
    private String orderStatus;

    @Column(name = "TRIP_ID")
    private Long tripId;

    @Column(name = "DRIVER_ID")
    private Long driverId;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return ACCEPTED_TIME
     */
    public Date getAcceptedTime() {
        return acceptedTime;
    }

    /**
     * @param acceptedTime
     */
    public void setAcceptedTime(Date acceptedTime) {
        this.acceptedTime = acceptedTime;
    }

    /**
     * @return COMPLETED_TIME
     */
    public Date getCompletedTime() {
        return completedTime;
    }

    /**
     * @param completedTime
     */
    public void setCompletedTime(Date completedTime) {
        this.completedTime = completedTime;
    }

    /**
     * @return ORDER_STATUS
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * @param orderStatus
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * @return TRIP_ID
     */
    public Long getTripId() {
        return tripId;
    }

    /**
     * @param tripId
     */
    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    /**
     * @return DRIVER_ID
     */
    public Long getDriverId() {
        return driverId;
    }

    /**
     * @param driverId
     */
    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
}