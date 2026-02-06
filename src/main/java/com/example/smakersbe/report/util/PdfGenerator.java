package com.example.smakersbe.report.util;

import com.example.smakersbe.report.dto.request.SelectedAiChatData;
import com.example.smakersbe.report.dto.request.SelectedMemoData;
import com.itextpdf.html2pdf.HtmlConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;


@Component
@RequiredArgsConstructor
public class PdfGenerator {

    private final TemplateEngine templateEngine;

    public byte[] generate(List<SelectedMemoData> memos, List<SelectedAiChatData> chats, MultipartFile image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 1. 타임리프 컨텍스트 설정 (데이터 바인딩)
            Context context = new Context();
            context.setVariable("memos", memos);
            context.setVariable("chats", chats);

            // 2. 이미지를 Base64 문자열로 변환 (S3 안 쓰니까 필수!)
            if (image != null && !image.isEmpty()) {
                String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
                context.setVariable("captureImage", "data:" + image.getContentType() + ";base64," + base64Image);
            }

            // 3. Thymeleaf 템플릿(HTML)을 문자열로 변환
            // src/main/resources/templates/report-template.html 파일을 찾습니다.
            String htmlContent = templateEngine.process("report-template", context);

            // 4. iText html2pdf를 사용하여 PDF 생성 (가장 핵심 한 줄!)
            HtmlConverter.convertToPdf(htmlContent, baos);

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("PDF 생성 중 입출력 에러 발생", e);
        }
    }

}
