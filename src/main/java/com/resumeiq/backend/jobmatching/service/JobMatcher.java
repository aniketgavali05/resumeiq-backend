package com.resumeiq.backend.jobmatching.service;

import org.springframework.stereotype.Service;

@Service
public class JobMatcher {

    public int calculateMatchScore(int skillScore, int keywordScore) {

        return (skillScore * 70 + keywordScore * 30) / 100;
    }

}