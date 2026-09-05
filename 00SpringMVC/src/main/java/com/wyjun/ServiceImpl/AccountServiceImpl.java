package com.wyjun.ServiceImpl;

import com.wyjun.entity.Account;
import com.wyjun.mapper.AccountMapper;
import com.wyjun.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired //根据类型进行装配，也可以搭配@Qualifier根据名字进行装配。
    private AccountMapper accountMapper;

    @Override
    @Transactional //这是控制事务的的注解，这个方法必须保证事务。也可以直接写在这个类上面，针对所有方法。
    //有了这个@Transactional开关，不管你连接多少次数据库，都能保证是一个sqlSession对象，不用再考虑ThreadLocal。
    public void transfer(String fromActNo, String toActNo, BigDecimal amount) {

        //查询转出账户信息
        Account fromAccount = accountMapper.selectAccountByActNo(fromActNo);

        //判断余额是否充足
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("转出账户余额不足!");
        }

        //能到这里说明余额充足，继续查询转入账户信息
        Account toAccount = accountMapper.selectAccountByActNo(toActNo);

        //在内存中修改2个账户的余额信息
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        //更新数据库
        int affectRows = accountMapper.updateBalanceByAccount(fromAccount);
        affectRows += accountMapper.updateBalanceByAccount(toAccount);
        if (affectRows!=2) {
            throw new RuntimeException("转账失败，请联系管理员!");
        }
    }
}
