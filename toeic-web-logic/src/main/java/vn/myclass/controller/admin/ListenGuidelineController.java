package vn.myclass.controller.admin;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import vn.myclass.command.ListenGuidelineCommand;
import vn.myclass.core.common.WebConstant;
import vn.myclass.core.common.utils.UploadUtil;
import vn.myclass.core.dto.ListenGuidelineDTO;
import vn.myclass.core.service.utils.SingletonDaoUtil;
import vn.myclass.core.utils.FormUtil;
import vn.myclass.core.utils.RequestUtil;
import vn.myclass.core.utils.SingletonServiceUtil;
import vn.myclass.core.utils.WebCommonUitl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/admin-guideline-listen-list.html", "/admin-guideline-listen-edit.html"})
public class ListenGuidelineController extends HttpServlet {
    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ListenGuidelineCommand command = FormUtil.populate(ListenGuidelineCommand.class, request);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources");
        if (command.getUrlType() != null && command.getUrlType().equals(WebConstant.URL_LIST)) {
            if (command.getCrudaction() != null && command.getCrudaction().equals(WebConstant.REDIRECT_DELETE)) {
                List<Integer> ids = new ArrayList<Integer>();
                for (String item : command.getCheckList()) {
                    ids.add(Integer.parseInt(item));
                }
                Integer result = SingletonDaoUtil.getListenGuidelineDaoInstance().delete(ids);
                if (result != ids.size()) {
                    command.setCrudaction(WebConstant.REDIRECT_ERROR);
                }
            }
            executeSearchListenGuideline(request, command);
            if (command.getCrudaction() != null) {
                Map<String, String> mapMessage = buildMapRedirectMessage(resourceBundle);
                WebCommonUitl.addRedirectMessage(request, command.getCrudaction(), mapMessage);
            }
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
    }

    private Map<String, String> buildMapRedirectMessage(ResourceBundle resourceBundle) {
        Map<String, String> mapMessage = new HashMap<String, String>();
        mapMessage.put(WebConstant.REDIRECT_INSERT, resourceBundle.getString("label.listenguideline.add.succress"));
        mapMessage.put(WebConstant.REDIRECT_UPDATE, resourceBundle.getString("label.listenguideline.update.succress"));
        mapMessage.put(WebConstant.REDIRECT_DELETE, resourceBundle.getString("label.listenguideline.delete.succress"));
        mapMessage.put(WebConstant.REDIRECT_ERROR, resourceBundle.getString("label.message.error"));
        return mapMessage;
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
        Set<String> valueTitle = buildSetValueListenGuideline();
        Object[] object = uploadUtil.writeOrUpdateFile(request, valueTitle, WebConstant.LISTENGUIDELINE);
        boolean checkStatusUploadImage = (Boolean) object[0];
        if (!checkStatusUploadImage) {
            response.sendRedirect("/admin-guideline-listen-list.html?urlType=url_list&&crudaction=redirect_error");
        } else {
            ListenGuidelineDTO dto = command.getPojo();
            if (StringUtils.isNotBlank(object[2].toString())) {
                dto.setImage(object[2].toString());
            }
            Map<String, String> mapValue = (Map<String, String>) object[3];
            dto = returnValueOfDTO(dto, mapValue);
            if (dto != null) {
                if (dto.getListenGuidelineId() != null) {
                    ListenGuidelineDTO listenGuidelineDTO = SingletonServiceUtil.getListenGuidelineServiceInstance().findByListenGuidelineId("listenGuidelineId", dto.getListenGuidelineId());
                    if (dto.getImage() == null) {
                        dto.setImage(listenGuidelineDTO.getImage());
                    }
                    dto.setCreatedDate(listenGuidelineDTO.getCreatedDate());
                    ListenGuidelineDTO result = SingletonServiceUtil.getListenGuidelineServiceInstance().updateListenGuideline(dto);
                    if (result != null) {
                        response.sendRedirect("/admin-guideline-listen-list.html?urlType=url_list&&crudaction=redirect_update");
                    } else {
                        response.sendRedirect("/admin-guideline-listen-list.html?urlType=url_list&&crudaction=redirect_error");
                    }
                } else {
                    try {
                        SingletonServiceUtil.getListenGuidelineServiceInstance().saveListenGuideline(dto);
                        response.sendRedirect("/admin-guideline-listen-list.html?urlType=url_list&&crudaction=redirect_insert");
                    } catch (ConstraintViolationException e) {
                        log.error(e.getMessage(), e);
                        response.sendRedirect("/admin-guideline-listen-list.html?urlType=url_list&&crudaction=redirect_error");
                    }
                }
            }
        }
    }

    private ListenGuidelineDTO returnValueOfDTO(ListenGuidelineDTO dto, Map<String, String> mapValue) {
        for (Map.Entry<String, String> item : mapValue.entrySet()) {
            if (item.getKey().equals("pojo.title")) {
                dto.setTitle(item.getValue());
            } else if (item.getKey().equals("pojo.content")) {
                dto.setContent((item.getValue()));
            } else if (item.getKey().equals("pojo.listenGuidelineId")) {
                dto.setListenGuidelineId(Integer.parseInt(item.getValue().toString()));
            }
        }
        return dto;
    }

    private Set<String> buildSetValueListenGuideline() {
        Set<String> returnValue = new HashSet<String>();
        returnValue.add("pojo.title");
        returnValue.add("pojo.content");
        returnValue.add("pojo.listenGuidelineId");
        return returnValue;
    }
}