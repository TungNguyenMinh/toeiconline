package vn.myclass.command;

import vn.myclass.core.command.AbstractCommand;
import vn.myclass.core.dto.UserDTO;

public class UserCommand extends AbstractCommand<UserDTO> {
    public UserCommand() {
        this.pojo = new UserDTO();
    }
}
