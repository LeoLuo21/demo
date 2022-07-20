package kafka.message;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author leo
 * @date 20220701 17:42:03
 */
public class LogMessage implements Serializable {

    private static final long serialVersionUID= 43243424234L;
    private String message;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogMessage)) return false;
        LogMessage that = (LogMessage) o;
        return message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public String toString() {
        return "LogMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
