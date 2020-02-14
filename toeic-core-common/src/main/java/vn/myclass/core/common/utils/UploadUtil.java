package vn.myclass.core.common.utils;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;

public class UploadUtil {
    private final int maxMemorySize = 1024 * 1024 * 3; //3M
    private final int maxRequestSize = 1024 * 1024 * 50; //50M

    public void writeOrUpdateFile(HttpServletRequest request) {
        // Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
    }
}