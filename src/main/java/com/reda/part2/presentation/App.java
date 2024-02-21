package com.reda.part2.presentation;

import com.reda.tp.dao.DaoImp;
import com.reda.tp.metier.IMetier;
import com.reda.tp.metier.MetierImp;

public class App {
    public static void main(String[] args) {
        IoCContainer container = new IoCContainer();

        //Saving the beans in a container
        container.registerBean("dao", new DaoImp());
        container.registerBean("metier", new MetierImp(new DaoImp()));

        // Injection of dependencies
        container.doConstructorInjection();
        container.doSetterInjection();
        container.doFieldInjection();

        //Using our beans
        IMetier metier = (IMetier) container.getBean("metier");
        metier.calculate();
    }
}