package com.gd.orh.fareRuleMgt.service;

import com.gd.orh.entity.FareRule;
import com.gd.orh.mapper.FareRuleMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
        PageHelper.offsetPage(0, 1);

        Example example = new Example(FareRule.class);
        example.setOrderByClause("SETUP_TIME DESC");

        return fareRuleMapper.selectOneByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public FareRule findById(Long id) {
        return fareRuleMapper.selectByPrimaryKey(id);
    }

    @Override
    public FareRule save(FareRule fareRule) {
        fareRule.setSetupTime(new Date());

        fareRuleMapper.insertUseGeneratedKeys(fareRule);

        return fareRule;
    }
}
