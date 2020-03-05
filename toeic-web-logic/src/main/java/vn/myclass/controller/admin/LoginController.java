package vn.myclass.controller.admin;

import org.apache.log4j.Logger;
import vn.myclass.command.UserCommand;
import vn.myclass.core.common.WebConstant;
import vn.myclass.core.dto.UserDTO;
import vn.myclass.core.service.UserService;
import vn.myclass.core.service.impl.UserServiceImpl;
import vn.myclass.core.utils.FormUtil;
import vn.myclass.core.utils.SingletonServiceUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

@WebServlet("/login.html")
public class LoginController extends HttpServlet {
    private final Logger log = Logger.getLogger(this.getClass());
    ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/views/web/login.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserCommand command = FormUtil.populate(UserCommand.class, request);
        UserDTO pojo = command.getPojo();
        /*try {
            if (SingletonServiceUtil.getUserServiceInstance().isUserExist(pojo) != null) {
                if (SingletonServiceUtil.getUserServiceInstance().findRoleByUser(pojo) != null && SingletonServiceUtil.getUserServiceInstance().findRoleByUser(pojo).getRoleDTO() != null) {
                    if (SingletonServiceUtil.getUserServiceInstance().findRoleByUser(pojo).getRoleDTO().getName().equals(WebConstant.ROLE_ADMIN)) {
                        response.sendRedirect("/admin-home.html");
                    } else if (SingletonServiceUtil.getUserServiceInstance().findRoleByUser(pojo).getRoleDTO().getName().equals(WebConstant.ROLE_USER)) {
                        response.sendRedirect("/home.html");
                    }
                }
            }
        } catch (NullPointerException e) {
            log.error(e.getMessage(), e);
            request.setAttribute(WebConstant.ALERT, WebConstant.TYPE_ERROR);
            request.setAttribute(WebConstant.MESSAGE_RESPONSE, "Tên hoặc mật khẩu sai");
            RequestDispatcher rd = request.getRequestDispatcher("/views/web/login.jsp");
            rd.forward(request, response);
        }*/
    }
}
