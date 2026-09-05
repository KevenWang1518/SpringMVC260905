package com.wyjun.service;

import java.math.BigDecimal;

public interface AccountService {

    /**
     * 转账的方法
     *
     * @param toActNo   转入账号
     * @param amount    转账金额
     * @param fromActNo 转出账号
     */

    void transfer(String fromActNo, String toActNo, BigDecimal amount);
}
