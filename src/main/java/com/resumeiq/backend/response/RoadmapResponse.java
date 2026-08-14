package com.resumeiq.backend.response;

import java.util.List;

public class RoadmapResponse {

    private List<RoadmapItem> roadmap;

    public RoadmapResponse() {
    }

    public RoadmapResponse(
            List<RoadmapItem> roadmap
    ) {
        this.roadmap = roadmap;
    }

    public List<RoadmapItem> getRoadmap() {
        return roadmap;
    }

    public void setRoadmap(
            List<RoadmapItem> roadmap
    ) {
        this.roadmap = roadmap;
    }

    public static class RoadmapItem {

        private String id;
        private String title;
        private String description;
        private String status;
        private int week;
        private String category;

        public RoadmapItem() {
        }

        public RoadmapItem(
                String id,
                String title,
                String description,
                String status,
                int week,
                String category
        ) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.status = status;
            this.week = week;
            this.category = category;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getWeek() {
            return week;
        }

        public void setWeek(int week) {
            this.week = week;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }
}