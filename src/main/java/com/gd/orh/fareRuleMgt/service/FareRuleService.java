package com.gd.orh.fareRuleMgt.service;

import com.gd.orh.entity.FareRule;

public interface FareRuleService {

    FareRule findRecentFareRule();

    FareRule findById(Long id);

    FareRule save(FareRule fareRule);
}
