package com.file;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    private static final String UPLOAD_DIR =
            System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "파일이 비어있습니다."));
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("message", "이미지 파일만 업로드 가능합니다."));
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.badRequest().body(Map.of("message", "파일 크기는 5MB 이하여야 합니다."));
        }

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String savedFilename = UUID.randomUUID() + ext;

        file.transferTo(new File(UPLOAD_DIR + savedFilename).getAbsoluteFile());

        return ResponseEntity.ok(Map.of("imageUrl", "/uploads/" + savedFilename));
    }
}
