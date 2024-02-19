package com.reda.tp.metier;

import com.reda.tp.dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class MetierImp implements IMetier {
    @Autowired
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
