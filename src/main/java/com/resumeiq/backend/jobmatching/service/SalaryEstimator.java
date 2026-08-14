package com.resumeiq.backend.jobmatching.service;

import org.springframework.stereotype.Service;

@Service
public class SalaryEstimator {

    public String estimateSalary(int matchScore) {

        if (matchScore >= 90)
            return "$120,000 - $150,000";

        if (matchScore >= 80)
            return "$90,000 - $120,000";

        if (matchScore >= 70)
            return "$70,000 - $90,000";

        if (matchScore >= 60)
            return "$50,000 - $70,000";

        return "$30,000 - $50,000";
    }

}