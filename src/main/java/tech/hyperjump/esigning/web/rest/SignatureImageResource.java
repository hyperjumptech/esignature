package tech.hyperjump.esigning.web.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.hyperjump.esigning.service.FileStorageService;

/**
 * SignatureImageResource controller
 */
@RestController
@RequestMapping("/api/signature-image")
public class SignatureImageResource {

    private final Logger log = LoggerFactory.getLogger(SignatureImageResource.class);

    @Autowired
    FileStorageService storageService;

    private final String signatureFolder = "uploads/";

    /**
     * POST saveSignature
     */
    @PostMapping("/save-signature")
    public String saveSignature(@RequestBody String base64String) {
        Date currentime = new Date();
        String filename = String.valueOf(currentime.getTime()).concat(".png");
        String path = signatureFolder.concat(filename);
        try {
            String base64part = base64String.replace("data:image/png;base64,", "");
            byte[] decoder = Base64.getDecoder().decode(base64part);

            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(decoder);
            fos.close();
        } catch (IOException e) {
            log.error("Error in creating image from base64 string", e);
        }

        return filename;
    }

    @GetMapping(value = "/{signaturefile:.+}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getFile(@PathVariable String signaturefile) {
        Resource file = storageService.load(signaturefile);
        InputStream in;
        byte[] imageByte = null;

        try {
            in = file.getInputStream();
            imageByte = IOUtils.toByteArray(in);
        } catch (IOException e) {
            log.error(signaturefile, e);
        }
        return imageByte;
    }
}
