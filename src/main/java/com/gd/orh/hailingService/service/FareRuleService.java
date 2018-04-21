package com.gd.orh.hailingService.service;

import com.gd.orh.entity.FareRule;
import com.gd.orh.mapper.FareRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FareRuleService {

    @Autowired
    private FareRuleMapper fareRuleMapper;

    // 找最新的计费规则
    @Transactional(readOnly = true)
    public FareRule findRecentFareRule() {
        return fareRuleMapper.findRecentFareRule();
    }
}
