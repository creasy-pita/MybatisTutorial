package com.creasypita.learning.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by creasypita on 9/9/2019.
 *
 * @ProjectName: MybatisTutorial
 */
@Service("innerService")
public class InnerServiceImpl implements InnerService {

    @Transactional(propagation = Propagation.REQUIRED)
    public void testRequired()
    {
        throw new RuntimeException("testRequired");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testRequiredNew()
    {
        throw new RuntimeException("testRequiredNew");
    }
}
