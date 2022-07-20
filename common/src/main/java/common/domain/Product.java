package common.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author leo
 * @date 20220627 15:44:24
 */
@Data
public class Product implements Serializable {
    private final static long serialVersionUID = 32442355465462L;
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private String shop;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
