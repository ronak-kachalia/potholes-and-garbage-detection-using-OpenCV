package com.examples.main;


public class Items {
    private String type, SRN, location,issue_date,completion_date,resolution_period,status,image1,image2;

    public Items() {
    }

    public Items(String type, String SRN, String location, String issue_date,
                 String completion_date, String resolution_period, String status,String image1, String image2) {
        this.type = type;
        this.SRN = SRN;
        this.location = location;
        this.issue_date = issue_date;
        this.resolution_period = resolution_period;
        this.completion_date = completion_date;
        this.status = status;
        this.image1=image1;
        this.image2=image2;
    }

    public String gettype() {
        return type;
    }

    public void settype(String name) {
        this.type = name;
    }


    public String getlocation() {
        return location;
    }

    public void setlocation(String SRN) {
        this.location = location;
    }


    public String getSRN() {
        return SRN;
    }

    public void setSRN(String SRN) {
        this.SRN = SRN;
    }


    public String getissue_date() {
        return issue_date;
    }

    public void setissue_date(String issue_date) {
        this.issue_date = issue_date;
    }


    public String getcompletion_date() {
        return completion_date;
    }

    public void setcompletion_date(String completion_date) {
        this.completion_date = completion_date;
    }

    public String getresolution_period() {
        return resolution_period;
    }

    public void setresolution_period(String issue_date) {
        this.resolution_period = resolution_period;
    }

    public String getstatus() {
        return status;
    }

    public void setstatus(String issue_date) {
        this.status = status;
    }

    public String getimage1() {
        return image1;
    }

    public String getimage2() {
        return image2;
    }
}
