package com.gd.orh.entity;

public enum OrderStatus {
    ACCEPTED, // 被受理
    CLOSED, // 已关闭
    PROCESSING, // 处理中
    PROCESSING_COMPLETED, // 处理完成
    PAID, // 已支付
    PAYMENT_COMPLETED // 支付完成
}
