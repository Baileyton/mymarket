package com.order.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@DiscriminatorValue("GENERAL")
@NoArgsConstructor
@SuperBuilder
public class GeneralItem extends Item {
    // 현재 추가할 특별한 필드가 없음
}