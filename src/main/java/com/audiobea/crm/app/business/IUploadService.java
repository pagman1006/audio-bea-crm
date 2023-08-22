package com.audiobea.crm.app.business;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.audiobea.crm.app.commons.dto.DtoInFileResponse;
import com.audiobea.crm.app.core.exception.NoSuchFileException;

public interface IUploadService {

	public Resource load(String filename) throws MalformedURLException;

	public String copy(MultipartFile file) throws IOException;

	public boolean delete(String filename) throws NoSuchFileException;

	public void deleteAll();

	public void init() throws IOException;

	public DtoInFileResponse uploadExcelFile(MultipartFile file);

	public List<String> uploadFiles(MultipartFile[] files) throws IOException;

}
