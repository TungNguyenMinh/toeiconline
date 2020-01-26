package vn.myclass.command;

import vn.myclass.core.command.AbstractCommand;
import vn.myclass.core.dto.UserDTO;

public class UserCommand extends AbstractCommand<UserDTO> {
    private String confirmPassword;
    public UserCommand() {
        this.pojo = new UserDTO();
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
