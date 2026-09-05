package com.wyjun.mapper;

import com.wyjun.entity.Account;

public interface AccountMapper {
    /**
     * 根据账号查询账户信息
     *
     * @param actNo 账号
     * @return 账户信息
     */
    Account selectAccountByActNo(String actNo);


    /**
     * 根据账户的余额
     *
     * @param account 账户对象
     * @return 1表示更新成功，其他值表示失败
     */
    int updateBalanceByAccount(Account account);
}
