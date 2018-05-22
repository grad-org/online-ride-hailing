package com.gd.orh.mapper;

import com.gd.orh.entity.FareRule;

public interface FareRuleMapper {

    FareRule findRecentFareRule();

    FareRule findById(Long id);

    void insertFareRule(FareRule fareRule);
}