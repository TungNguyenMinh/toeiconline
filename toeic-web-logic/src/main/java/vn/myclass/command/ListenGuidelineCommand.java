package vn.myclass.command;

import vn.myclass.core.command.AbstractCommand;
import vn.myclass.core.dto.ListenGuidelineDTO;

public class ListenGuidelineCommand extends AbstractCommand<ListenGuidelineDTO> {
    public ListenGuidelineCommand() {
        this.pojo = new ListenGuidelineDTO();
    }
}
