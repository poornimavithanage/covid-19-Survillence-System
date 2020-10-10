package util;

public class QuarantineCenterTM {
    private String id;
    private String quarantineCenterName;
    private String city;
    private String district;
    private String headName;
    private String headContact;
    private String centerContact1;
    private String centerContact2;
    private String capacity;

    public QuarantineCenterTM() {
    }

    public QuarantineCenterTM(String id, String quarantineCenterName, String city, String district, String headName, String headContact, String centerContact1, String centerContact2, String capacity) {
        this.id = id;
        this.quarantineCenterName = quarantineCenterName;
        this.city = city;
        this.district = district;
        this.headName = headName;
        this.headContact = headContact;
        this.centerContact1 = centerContact1;
        this.centerContact2 = centerContact2;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuarantineCenterName() {
        return quarantineCenterName;
    }

    public void setQuarantineCenterName(String quarantineCenterName) {
        this.quarantineCenterName = quarantineCenterName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public String getHeadContact() {
        return headContact;
    }

    public void setHeadContact(String headContact) {
        this.headContact = headContact;
    }

    public String getCenterContact1() {
        return centerContact1;
    }

    public void setCenterContact1(String centerContact1) {
        this.centerContact1 = centerContact1;
    }

    public String getCenterContact2() {
        return centerContact2;
    }

    public void setCenterContact2(String centerContact2) {
        this.centerContact2 = centerContact2;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return quarantineCenterName;
    }
}
