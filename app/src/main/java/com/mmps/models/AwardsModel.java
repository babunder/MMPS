package com.mmps.models;

import java.io.Serializable;

/**
 * @author Aditya Kulkarni
 */

public class AwardsModel implements Serializable{

    private int SrNo,OrganizationId, AwardId;
    private String AwardWinner, PhotoImage, AwardYear, Organization, AwardTitle, AwardPrice, AwardCriteria, Description, CreatedBy,
            CreatedDate, LastModifiedBy, LastModifiedDate;
    private boolean IsActive;

    public int getSrNo() {
        return SrNo;
    }

    public int getAwardId() {
        return AwardId;
    }

    public void setAwardId(int awardId) {
        AwardId = awardId;
    }

    public String getAwardWinner() {
        return AwardWinner;
    }

    public void setAwardWinner(String awardWinner) {
        AwardWinner = awardWinner;
    }

    public String getPhotoImage() {
        return PhotoImage;
    }

    public void setPhotoImage(String photoImage) {
        PhotoImage = photoImage;
    }

    public String getAwardYear() {
        return AwardYear;
    }

    public void setAwardYear(String awardYear) {
        AwardYear = awardYear;
    }

    public void setSrNo(int srNo) {
        SrNo = srNo;
    }

    public int getOrganizationId() {
        return OrganizationId;
    }

    public void setOrganizationId(int organizationId) {
        OrganizationId = organizationId;
    }

    public String getOrganization() {
        return Organization;
    }

    public void setOrganization(String organization) {
        Organization = organization;
    }

    public String getAwardTitle() {
        return AwardTitle;
    }

    public void setAwardTitle(String awardTitle) {
        AwardTitle = awardTitle;
    }

    public String getAwardPrice() {
        return AwardPrice;
    }

    public void setAwardPrice(String awardPrice) {
        AwardPrice = awardPrice;
    }

    public String getAwardCriteria() {
        return AwardCriteria;
    }

    public void setAwardCriteria(String awardCriteria) {
        AwardCriteria = awardCriteria;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getLastModifiedBy() {
        return LastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        LastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return LastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        LastModifiedDate = lastModifiedDate;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }
}
