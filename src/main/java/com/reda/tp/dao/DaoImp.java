package com.reda.tp.dao;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DaoImp implements IDao {
    @Override
    public Date getDate() {
        return new Date();
    }
}
