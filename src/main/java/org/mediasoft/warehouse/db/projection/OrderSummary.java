package org.mediasoft.warehouse.db.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface OrderSummary {
    UUID getId();
    String getName();
    BigDecimal getQty();
    BigDecimal getPrice();
}
