package com.audiobea.crm.app.business;

import com.audiobea.crm.app.commons.dto.DtoInFileResponse;
import com.audiobea.crm.app.core.exception.NoSuchFileException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface IUploadService {

    Resource load(String filename) throws MalformedURLException;

    String copy(MultipartFile file) throws IOException;

    boolean delete(String filename) throws NoSuchFileException;

    void deleteAll();

    void init() throws IOException;

    DtoInFileResponse uploadExcelFile(MultipartFile file);

    List<String> uploadFiles(MultipartFile[] files) throws IOException;

}
