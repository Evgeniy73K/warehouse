package org.mediasoft.warehouse.service.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mediasoft.warehouse.db.entity.enums.StatusEnum;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusDto {
    StatusEnum status;
}
