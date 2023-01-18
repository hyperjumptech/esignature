package tech.hyperjump.esigning.web.rest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.hyperjump.esigning.message.ResponseMessage;
import tech.hyperjump.esigning.service.FileStorageService;

/**
 * FileUploadResource controller
 */
@RestController
@RequestMapping("/api/files")
public class FileUploadResource {

    private final Logger log = LoggerFactory.getLogger(FileUploadResource.class);

    @Autowired
    FileStorageService storageService;

    /**
     * POST upload
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> upload(@RequestParam MultipartFile file) {
        String message = "";
        try {
            storageService.save(file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            log.info(message);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (IllegalStateException e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            log.error(message, e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    // @GetMapping("/files")
    // public ResponseEntity<List<FileInfo>> getListFiles() {
    //     List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
    //         String filename = path.getFileName().toString();
    //         String url = MvcUriComponentsBuilder
    //                 .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

    //         return new FileInfo(filename, url);
    //     }).collect(Collectors.toList());

    //     return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    // }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
    }
}
