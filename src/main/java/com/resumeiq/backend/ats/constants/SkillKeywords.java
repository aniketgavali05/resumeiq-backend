package com.resumeiq.backend.ats.constants;

import java.util.HashMap;
import java.util.Map;

public final class SkillKeywords {

    private SkillKeywords() {}

    public static final Map<String, String> ALIASES = new HashMap<>();

    static {

        ALIASES.put("react.js", "React");
        ALIASES.put("reactjs", "React");

        ALIASES.put("nextjs", "Next.js");
        ALIASES.put("next.js", "Next.js");

        ALIASES.put("js", "JavaScript");

        ALIASES.put("ts", "TypeScript");

        ALIASES.put("postgres", "PostgreSQL");

        ALIASES.put("springboot", "Spring Boot");

        ALIASES.put("spring mvc", "Spring");

        ALIASES.put("node", "Node.js");

        ALIASES.put("rest", "REST API");

        ALIASES.put("gcp", "Google Cloud");

        ALIASES.put("ml", "Machine Learning");

        ALIASES.put("ai", "Artificial Intelligence");

        ALIASES.put("github.com", "GitHub");
    }
}