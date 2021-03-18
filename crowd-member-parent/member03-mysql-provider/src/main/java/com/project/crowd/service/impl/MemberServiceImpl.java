package com.project.crowd.service.impl;

import com.project.crowd.entity.po.MemberPO;
import com.project.crowd.entity.po.MemberPOExample;
import com.project.crowd.mapper.MemberPOMapper;
import com.project.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 */
// 因为该类单独设置为查询API,在类上针对查询操作设置事务属性为只读，其他操作另行添加注解设置事务
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberPOMapper memberPOMapper;


    /**
     * 根据loginacct查询对应的MemberPO对象
     * @param loginacct
     * @return
     */
    @Override
    public MemberPO getMemberPOByLoginAcct(String loginacct) {

        // 创建Example对象
        MemberPOExample example = new MemberPOExample();
        // 通过Example创建Criteria对象，把查询条件封装进去
        example.createCriteria().andLoginacctEqualTo(loginacct);

        // 执行查询
        List<MemberPO> list = memberPOMapper.selectByExample(example);

        // 如果list为null，无法通过get来获取对象，所以直接返回null就可以了
        if (list == null || list.size() == 0) {
            return null;
        }
        // 不为null，就通过get来返回对象
        return list.get(0);
    }

    // 设置事务传播行为为REQUIRES_NEW，所有异常都进行回滚
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor= Exception.class)
    @Override
    public void saveMember(MemberPO memberPO) {

        memberPOMapper.insertSelective(memberPO);
    }
}
