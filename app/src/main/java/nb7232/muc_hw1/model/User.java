package nb7232.muc_hw1.model;

import java.util.UUID;

public class User {

    public enum Sex {
        MALE,
        FEMALE
    }

    private String uuid;
    private String occupation;
    private Integer age;
    private String firstName;
    private String lastName;
    private String email;
    private Sex sex;
    private String rawPassword;
    private String hashedPassword;
    private Integer samplingInterval;
    private boolean isValid;

    public User() {
        this.isValid = true;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean setUuid(String uuid) {
        try {
            UUID.fromString(uuid);
            this.uuid = uuid;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean setOccupation(String occupation) {

        if (occupation.length() < 1) {
            this.isValid = false;
            return false;

        }
        this.occupation = occupation;
        return true;
    }

    public boolean setAge(int age) {
        if (age < 5) {
            this.isValid = false;
            return false;
        }
        this.age = age;
        return true;
    }

    public boolean setFirstName(String firstName) {
        if (firstName.length() < 2) {
            this.isValid = false;
            return false;
        }
        this.firstName = firstName;
        return true;
    }

    public boolean setLastName(String lastName) {
        if (lastName.length() < 2) {
            this.isValid = false;
            return false;
        }
        this.lastName = lastName;
        return true;
    }

    public boolean setEmail(String email) {
        if (email.length() < 4) {
            this.isValid = false;
            return false;
        } else if (!Validator.isValidEmail(email)) {
            this.isValid = false;
            return false;
        }
        this.email = email;
        return true;
    }

    public boolean setSex(Sex sex) {
        this.sex = sex;
        return true;
    }

    public boolean setRawPassword(String password) {
        if (password.length() < 8 || password.length() > 16) {
            this.isValid = false;
            return false;
        }
        this.rawPassword = password;
        return true;
    }

    public boolean setHashedPassword(String password) {
        if (password.length() != 32) {
            this.isValid = false;
            return false;
        }
        this.hashedPassword = password;
        return true;
    }

    public boolean setSamplingInterval(Integer samplingInterval) {
        if (samplingInterval < 0) {
            this.isValid = false;
            return false;
        }
        this.samplingInterval = samplingInterval;
        return true;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getUuid() {
        return uuid;
    }

    public String getOccupation() {
        return occupation;
    }

    public Integer getAge() {
        return age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Sex getSex() {
        return sex;
    }

    public Integer getSamplingInterval() {
        return samplingInterval;
    }
}
