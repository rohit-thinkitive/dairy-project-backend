package com.dairy.milkcollection.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MilkCollectionRequest {
    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotNull(message = "Milk liter is required")
    @DecimalMin(value = "0.1", message = "Milk must be at least 0.1 liters")
    private BigDecimal milkLiter;

    private BigDecimal fat;
    private BigDecimal snf;

    @NotNull(message = "Degree is required")
    @DecimalMin(value = "1", message = "Degree must be at least 1")
    private BigDecimal degree;
}
