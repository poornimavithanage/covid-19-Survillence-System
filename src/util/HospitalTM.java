package util;

public class HospitalTM {
    private String id;
    private String hospitalName;
    private String city;
    private String district;
    private String capacity;
    private String directorName;
    private String directorContactNo;
    private String hospitalContact1;
    private String hospitalContact2;
    private String hospitalFax;
    private String hospitalEmail;

    public HospitalTM() {
    }

    public HospitalTM(String id, String hospitalName, String city,
                      String district, String capacity, String directorName, String directorContactNo,
                      String hospitalContact1, String hospitalContact2, String hospitalFax, String hospitalEmail) {
        this.id = id;
        this.hospitalName = hospitalName;
        this.city = city;
        this.district = district;
        this.capacity = capacity;
        this.directorName = directorName;
        this.directorContactNo = directorContactNo;
        this.hospitalContact1 = hospitalContact1;
        this.hospitalContact2 = hospitalContact2;
        this.hospitalFax = hospitalFax;
        this.hospitalEmail = hospitalEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
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

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getDirectorContactNo() {
        return directorContactNo;
    }

    public void setDirectorContactNo(String directorContactNo) {
        this.directorContactNo = directorContactNo;
    }

    public String getHospitalContact1() {
        return hospitalContact1;
    }

    public void setHospitalContact1(String hospitalContact1) {
        this.hospitalContact1 = hospitalContact1;
    }

    public String getHospitalContact2() {
        return hospitalContact2;
    }

    public void setHospitalContact2(String hospitalContact2) {
        this.hospitalContact2 = hospitalContact2;
    }

    public String getHospitalFax() {
        return hospitalFax;
    }

    public void setHospitalFax(String hospitalFax) {
        this.hospitalFax = hospitalFax;
    }

    public String getHospitalEmail() {
        return hospitalEmail;
    }

    public void setHospitalEmail(String hospitalEmail) {
        this.hospitalEmail = hospitalEmail;
    }

    @Override
    public String toString() {
        return hospitalName;
    }
}
