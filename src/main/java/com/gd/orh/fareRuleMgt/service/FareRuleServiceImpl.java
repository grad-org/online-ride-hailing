package com.gd.orh.fareRuleMgt.service;

import com.gd.orh.entity.FareRule;
import com.gd.orh.mapper.FareRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Service
public class FareRuleServiceImpl implements FareRuleService {

    @Autowired
    private FareRuleMapper fareRuleMapper;

    // 找最新的计费规则
    @Override
    @Transactional(readOnly = true)
    public FareRule findRecentFareRule() {
        return fareRuleMapper.findRecentFareRule();
    }

    @Override
    @Transactional(readOnly = true)
    public FareRule findById(Long id) {
        return fareRuleMapper.findById(id);
    }

    @Override
    public FareRule save(FareRule fareRule) {
        fareRule.setSetupTime(new Date());

        fareRuleMapper.insertFareRule(fareRule);

        return fareRule;
    }
}
