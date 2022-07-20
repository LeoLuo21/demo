package kafka.message;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author leo
 * @date 20220701 18:06:17
 */
public class OrderMessage implements Serializable {
    private static final long serialVersionUID = 434767243435453L;
    private String orderNumber;
    private String customer;


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderMessage)) return false;
        OrderMessage that = (OrderMessage) o;
        return orderNumber.equals(that.orderNumber) && customer.equals(that.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, customer);
    }

    @Override
    public String toString() {
        return "OrderMessage{" +
                "orderNumber='" + orderNumber + '\'' +
                ", customer='" + customer + '\'' +
                '}';
    }
}
