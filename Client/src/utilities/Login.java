
package utilities;

import java.io.Serializable;
import java.util.Objects;

public class Login implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;
    private int Login_ID;
    private String Login_pswd;
    boolean logged_In;

    public Login() {
        this.Login_ID = 0;
        this.Login_pswd = "";
        logged_In = false;
    }

    public Login(int Login_ID, String Login_pswd) {
        this.Login_ID = Login_ID;
        this.Login_pswd = Login_pswd;
        logged_In = false;
    }

    public boolean verifyAdmin(int Login_ID, String Login_pswd) {
        return (this.Login_ID == Login_ID && this.Login_pswd.equals(Login_pswd));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return Login_ID == login.Login_ID && Objects.equals(Login_pswd, login.Login_pswd);
    }



// Getter Methods

    public int getLogin_ID() {
        return Login_ID;
    }

    public String getLogin_pswd() {
        return Login_pswd;
    }

    public boolean getLogged_In() { return logged_In; }

    // Setter Methods

    public void setLogin_ID(int login_ID) {
        Login_ID = login_ID;
    }

    public void setLogin_pswd(String login_pswd) {
        Login_pswd = login_pswd;
    }

    public void setLogged_In(boolean logged_In) { this.logged_In = logged_In; }
}
