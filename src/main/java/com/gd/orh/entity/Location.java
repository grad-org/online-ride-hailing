package com.gd.orh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private String lng; // 经度
    private String lat; // 纬度
}
