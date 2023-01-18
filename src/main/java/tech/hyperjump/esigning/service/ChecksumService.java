package tech.hyperjump.esigning.service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChecksumService {

    private final Logger log = LoggerFactory.getLogger(ChecksumService.class);

    public String createFileMd5Checksum(File doc) {
        String checksum = null;
        try {
            byte[] data = Files.readAllBytes(Paths.get(doc.toURI()));
            byte[] hash = MessageDigest.getInstance("MD5").digest(data);
            checksum = new BigInteger(1, hash).toString(16);
        } catch (IOException | NoSuchAlgorithmException e) {
            log.error("Error in creating checksum", e);
        }

        return checksum;
    }
}
