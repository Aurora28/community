package com.aurora.community.dao;

import org.springframework.stereotype.Repository;

@Repository("alphaHerbinate")
public class AlphaDaoHerbinate implements AlphaDao{
    @Override
    public String select() {
        return "Herbinate";
    }
}