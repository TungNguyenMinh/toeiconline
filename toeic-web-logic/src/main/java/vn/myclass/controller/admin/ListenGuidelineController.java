package vn.myclass.controller.admin;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import vn.myclass.command.ListenGuidelineCommand;
import vn.myclass.core.common.WebConstant;
import vn.myclass.core.common.utils.UploadUtil;
import vn.myclass.core.dto.ListenGuidelineDTO;
import vn.myclass.core.service.ListenGuidelineService;
import vn.myclass.core.service.impl.ListenGuidelineServiceImpl;
import vn.myclass.core.utils.FormUtil;
import vn.myclass.core.utils.RequestUtil;
import vn.myclass.core.utils.SingletonServiceUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/admin-guideline-listen-list.html", "/admin-guideline-listen-edit.html"})
public class ListenGuidelineController extends HttpServlet {
    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ListenGuidelineCommand command = FormUtil.populate(ListenGuidelineCommand.class, request);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources");
        /*HttpSession session = request.getSession();
        request.setAttribute(WebConstant.ALERT, WebConstant.TYPE_SUCCESS);
        request.setAttribute(WebConstant.MESSAGE_RESPONSE, resourceBundle.getString("label.guideline.listen.add.success"));
        if (session != null) {
            session.setAttribute(WebConstant.ALERT, session.getAttribute(WebConstant.ALERT));
            session.setAttribute(WebConstant.MESSAGE_RESPONSE, session.getAttribute(WebConstant.MESSAGE_RESPONSE));
        }*/
        if (command.getUrlType() != null && command.getUrlType().equals(WebConstant.URL_LIST)) {
            executeSearchListenGuideline(request, command);
            request.setAttribute(WebConstant.LIST_ITEMS, command);
            RequestDispatcher rd = request.getRequestDispatcher("/views/admin/listenguideline/list.jsp");
            rd.forward(request, response);
        } else if (command.getUrlType() != null && command.getUrlType().equals(WebConstant.URL_EDIT)) {
            if (command.getPojo() != null && command.getPojo().getListenGuidelineId() != null) {
                command.setPojo(SingletonServiceUtil.getListenGuidelineServiceInstance().findByListenGuidelineId("listenGuidelineId", command.getPojo().getListenGuidelineId()));
            }
            request.setAttribute(WebConstant.FROM_ITEM, command);
            RequestDispatcher rd = request.getRequestDispatcher("/views/admin/listenguideline/edit.jsp");
            rd.forward(request, response);
        }
        /*session.removeAttribute(WebConstant.ALERT);
        session.removeAttribute(WebConstant.MESSAGE_RESPONSE);*/
    }

    private void executeSearchListenGuideline(HttpServletRequest request, ListenGuidelineCommand command) {
        Map<String, Object> properties = buildMapProperties(command);
        RequestUtil.initSearchBean(request, command);
        Object[] objects = SingletonServiceUtil.getListenGuidelineServiceInstance().findListenGuidelineByProperties(properties, command.getSortDirection(), command.getSortExpression(), command.getFirstItem(), command.getMaxPageItems());
        command.setListResult((List<ListenGuidelineDTO>) objects[1]);
        command.setTotalItems(Integer.parseInt(objects[0].toString()));
    }

    private Map<String, Object> buildMapProperties(ListenGuidelineCommand command) {
        Map<String, Object> properties = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(command.getPojo().getTitle())) {
            properties.put("title", command.getPojo().getTitle());
        }
        return properties;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ListenGuidelineCommand command = new ListenGuidelineCommand();
        ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");
        UploadUtil uploadUtil = new UploadUtil();
        HttpSession session = request.getSession();
        //Set<String> valueTitle = buildSetValueListenguideline();
        /*try {
            Object[] object = uploadUtil.writeOrUpdateFile(request, valueTitle, WebConstant.LISTENGUIDELINE);
            Map<String, String> mapValue = (Map<String, String>) object[3];
            command = returnValueListenGuidelineCommand(valueTitle, command, mapValue);
            session.setAttribute(WebConstant.ALERT, WebConstant.TYPE_SUCCESS);
            session.setAttribute(WebConstant.MESSAGE_RESPONSE, bundle.getString("label.guideline.listen.add.success"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            session.setAttribute(WebConstant.ALERT, WebConstant.TYPE_ERROR);
            session.setAttribute(WebConstant.MESSAGE_RESPONSE, bundle.getString("label.error"));
        }*/
        response.sendRedirect("/admin-guideline-listen-list.html?urlType=url_list");
    }

    /*private ListenGuidelineCommand returnValueListenGuidelineCommand(Set<String> valueTitle, ListenGuidelineCommand command, Map<String, String> mapValue) {
        for (String item : valueTitle) {
            if (mapValue.containsKey(item)) {
                if (item.equals("pojo.title")) {
                    command.getPojo().setTitle(mapValue.get(item));
                } else if (item.equals("pojo.content")) {
                    command.getPojo().setContent(mapValue.get(item));
                }
            }
        }
        return command;
    }*/

    /*private Set<String> buildSetValueListenguideline() {
        Set<String> returnValue = new HashSet<String>();
        returnValue.add("pojo.title");
        returnValue.add("pojo.content");
        return returnValue;
    }*/
}