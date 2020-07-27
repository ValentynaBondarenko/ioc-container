package com.bondarenko;

import com.bondarenko.bean.factory.BeanProcessor;
import test.SongService;

public class Main {
    public static void main(String[] args) {
        BeanProcessor beanProcessor = new BeanProcessor();
        beanProcessor.searchClass("test");
        SongService songService = (SongService) beanProcessor.getBean("songService");
        System.out.println(songService);

        beanProcessor.getBeanProperties();
    }
}
