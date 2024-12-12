package accountInfo;

public class Credentials {
    private String email;
    private String password;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /** For future use cases, maybe add an encryption algorithm
     *  to check if a password is correct
     */
    public void changePassword(String oldPassword, String newPassword) {
        if (newPassword.equals(oldPassword)) {
            this.password = newPassword;
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Invalid password\nTry again later!");
        }
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
