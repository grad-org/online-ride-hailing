package com.gd.orh.mapper;

import com.gd.orh.entity.FareRule;
import com.gd.orh.utils.MyMapper;

public interface FareRuleMapper extends MyMapper<FareRule> {

    FareRule findRecentFareRule();
}