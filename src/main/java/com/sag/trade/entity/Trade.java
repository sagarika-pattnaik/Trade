package com.sag.trade.entity;

import jdk.jfr.DataAmount;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Trade
{

   // private Integer id;
    private String tradeid;
    private String version;
    private String couterpartyid;
    private String bookid;
    private String maturityDate;
    private LocalDate createDate;
    private String expired;
    private Boolean isValid;
    private Boolean shouldOverride;

    public Trade() {
    }

    public String getTradeid() {
        return tradeid;
    }

    public void setTradeid(String tradeid) {
        this.tradeid = tradeid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCouterpartyid() {
        return couterpartyid;
    }

    public void setCouterpartyid(String couterpartyid) {
        this.couterpartyid = couterpartyid;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public Boolean getShouldOverride() {
        return shouldOverride;
    }

    public void setShouldOverride(Boolean shouldOverride) {
        this.shouldOverride = shouldOverride;
    }
}