package vn.myclass.controller.admin;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vn.myclass.command.UserCommand;
import vn.myclass.core.common.WebConstant;
import vn.myclass.core.common.utils.UploadUtil;
import vn.myclass.core.dto.RoleDTO;
import vn.myclass.core.dto.UserDTO;
import vn.myclass.core.service.RoleService;
import vn.myclass.core.service.UserService;
import vn.myclass.core.service.impl.RoleServiceImpl;
import vn.myclass.core.service.impl.UserServiceImpl;
import vn.myclass.core.utils.FormUtil;
import vn.myclass.core.utils.SingletonServiceUtil;
import vn.myclass.core.utils.WebCommonUitl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@WebServlet(urlPatterns = {"/admin-user-list.html", "/ajax-admin-user-edit.html", "/admin-user-import-list.html"})
public class UserController extends HttpServlet {
    private final Logger log = Logger.getLogger(this.getClass());
    private final String SHOW_IMPORT_USER = "show_import_user";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserCommand command = FormUtil.populate(UserCommand.class, request);
        UserDTO pojo = command.getPojo();
        ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
        if (command.getUrlType() != null && command.getUrlType().equals(WebConstant.URL_LIST)) {
            Map<String, Object> mapProperty = new HashMap<String, Object>();
            Object[] objects = SingletonServiceUtil.getUserServiceInstance().findByProperty(mapProperty, command.getSortExpression(), command.getSortDirection(), command.getFirstItem(), command.getMaxPageItems());
            command.setListResult((List<UserDTO>) objects[1]);
            command.setTotalItems(Integer.parseInt(objects[0].toString()));
            request.setAttribute(WebConstant.LIST_ITEMS, command);
            if (command.getCrudaction() != null) {
                Map<String, String> mapMessage = buidMapRedirectMessage(bundle);
                WebCommonUitl.addRedirectMessage(request, command.getCrudaction(), mapMessage);
            }
            RequestDispatcher rd = request.getRequestDispatcher("/views/admin/user/list.jsp");
            rd.forward(request, response);
        } else if (command.getUrlType() != null && command.getUrlType().equals(WebConstant.URL_EDIT)) {
            if (pojo != null && pojo.getUserId() != null) {
                command.setPojo(SingletonServiceUtil.getUserServiceInstance().findById(pojo.getUserId()));
            }
            command.setRoles(SingletonServiceUtil.getRoleServiceInstance().findAll());
            request.setAttribute(WebConstant.FROM_ITEM, command);
            RequestDispatcher rd = request.getRequestDispatcher("/views/admin/user/edit.jsp");
            rd.forward(request, response);
        } else if (command.getUrlType() != null && command.getUrlType().equals(SHOW_IMPORT_USER)) {
            RequestDispatcher rd = request.getRequestDispatcher("/views/admin/user/importuser.jsp");
            rd.forward(request, response);
        }
    }

    private Map<String, String> buidMapRedirectMessage(ResourceBundle bundle) {
        Map<String, String> mapMessage = new HashMap<String, String>();
        mapMessage.put(WebConstant.REDIRECT_INSERT, bundle.getString("label.user.message.add.success"));
        mapMessage.put(WebConstant.REDIRECT_UPDATE, bundle.getString("label.user.message.update.success"));
        mapMessage.put(WebConstant.REDIRECT_DELETE, bundle.getString("label.user.message.delete.success"));
        mapMessage.put(WebConstant.REDIRECT_ERROR, bundle.getString("label.message.error"));
        return mapMessage;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UploadUtil uploadUtil = new UploadUtil();
        Object[] objects = uploadUtil.writeOrUpdateFile(request, null, null);
        try {
            UserCommand command = FormUtil.populate(UserCommand.class, request);
            UserDTO pojo = command.getPojo();
            if (command.getUrlType().equals(WebConstant.URL_EDIT)) {
                if (command.getCrudaction() != null && command.getCrudaction().equals(WebConstant.INSERT_UPDATE)) {
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setRoleId(command.getRoleId());
                    pojo.setRoleDTO(roleDTO);
                    if (pojo != null && pojo.getUserId() != null) {
                        SingletonServiceUtil.getUserServiceInstance().updateUser(pojo);
                        request.setAttribute(WebConstant.MESSAGE_RESPONSE, WebConstant.REDIRECT_UPDATE);
                    } else {
                        SingletonServiceUtil.getUserServiceInstance().saveUser(pojo);
                        request.setAttribute(WebConstant.MESSAGE_RESPONSE, WebConstant.REDIRECT_INSERT);
                    }
                }
            }
            if (objects != null) {
                String fileLocation = objects[1].toString();
                FileInputStream excelFile = new FileInputStream(new File(fileLocation));
                Workbook workbook = new XSSFWorkbook(excelFile);
                Sheet sheet = workbook.getSheetAt(0);
                for(int i = 1; i <= sheet.getLastRowNum(); i++) {
                    
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            request.setAttribute(WebConstant.MESSAGE_RESPONSE, WebConstant.REDIRECT_ERROR);
        }
        RequestDispatcher rd = request.getRequestDispatcher("/views/admin/user/edit.jsp");
        rd.forward(request, response);
    }
}
