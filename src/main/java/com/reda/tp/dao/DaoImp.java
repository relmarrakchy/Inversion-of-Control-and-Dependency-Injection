package com.reda.tp.dao;

import java.util.Date;

public class DaoImp implements IDao {
    @Override
    public Date getDate() {
        return new Date();
    }
}
