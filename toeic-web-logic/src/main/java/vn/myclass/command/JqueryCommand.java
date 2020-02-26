package vn.myclass.command;

import vn.myclass.core.command.AbstractCommand;
import vn.myclass.core.dto.JqueryDTO;

public class JqueryCommand extends AbstractCommand<JqueryDTO> {
    public JqueryCommand() {
        this.pojo = new JqueryDTO();
    }
    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    private String urlType;
}
