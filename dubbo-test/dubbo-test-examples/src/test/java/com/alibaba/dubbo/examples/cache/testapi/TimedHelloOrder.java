/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-10-16 10:32 创建
 *
 */
package com.alibaba.dubbo.examples.cache.testapi;

import java.util.Date;

import com.alibaba.dubbo.cache.keygenerator.selective.KeyComponent;
import com.alibaba.dubbo.examples.cache.api.HelloOrder;

/**
 * @author daidai@yiji.com
 */
public class TimedHelloOrder extends HelloOrder {
    private boolean important;
    private String orderNo;
    private Date time;

    public TimedHelloOrder(String userId, String message) {
        super(userId, message);
    }

    public TimedHelloOrder(boolean important, String userId, String message, String orderNo, Date time) {
        super(userId, message);
        this.important = important;
        this.orderNo = orderNo;
        this.time = time;
    }

    @KeyComponent
    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @KeyComponent
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}