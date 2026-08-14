package com.resumeiq.backend.response;

import java.util.List;

public class ResumePageResponse {

    private List<ResumeResponse> resumes;

    private int currentPage;

    private int pageSize;

    private int totalPages;

    private long totalElements;

    private boolean last;

    public ResumePageResponse() {
    }

    public ResumePageResponse(
            List<ResumeResponse> resumes,
            int currentPage,
            int pageSize,
            int totalPages,
            long totalElements,
            boolean last) {

        this.resumes = resumes;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.last = last;
    }

    public List<ResumeResponse> getResumes() {
        return resumes;
    }

    public void setResumes(List<ResumeResponse> resumes) {
        this.resumes = resumes;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}