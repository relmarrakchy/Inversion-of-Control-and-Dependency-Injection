package com.reda.tp.metier;

import com.reda.tp.dao.IDao;

import java.util.Date;

public class MetierImp implements IMetier {
    private IDao dao;

    public MetierImp(IDao dao) {
        this.dao = dao;
    }

    @Override
    public void calculate() {
        Date date = dao.getDate();
        System.out.println("Date of calculation : " + date);
    }
}
