package nb7232.muc_hw1.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

    public enum Sex {
        MALE,
        FEMALE
    }

    private String occupation;
    private Integer age;
    private String firstName;
    private String lastName;
    private String email;
    private Sex sex;
    private String password;
    private Integer samplingInterval;
    private boolean isValid;

    public User() {
        this.isValid = true;
    }

    public boolean isValid() {
        return isValid;
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

    public boolean setPassword(String password) {
        if (password.length() < 8 || password.length() > 16) {
            this.isValid = false;
            return false;
        }
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    /**
     * function md5 encryption for passwords
     *
     * @param password
     * @return passwordEncrypted
     * @link https://cmanios.wordpress.com/2012/03/19/android-md5-password-encryption/
     */
    private static final String md5(final String password) {
        try {

            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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
