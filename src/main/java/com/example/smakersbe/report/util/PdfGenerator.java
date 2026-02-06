package com.example.smakersbe.report.util;

import com.example.smakersbe.report.dto.request.SelectedAiChatData;
import com.example.smakersbe.report.dto.request.SelectedMemoData;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.layout.font.FontProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
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
            // 1. 타임리프 컨텍스트 설정
            Context context = new Context();
            context.setVariable("memos", memos);
            context.setVariable("chats", chats);

            // 2. 이미지 Base64 변환
            if (image != null && !image.isEmpty()) {
                String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
                context.setVariable("captureImage", "data:" + image.getContentType() + ";base64," + base64Image);
            }

            // 3. HTML 템플릿 처리
            String htmlContent = templateEngine.process("report-template", context);

            // 4. ⭐ 폰트 설정 (이 부분이 핵심!)
            ConverterProperties properties = new ConverterProperties();
            FontProvider fontProvider = new DefaultFontProvider(false, false, false); // 기본 폰트 끄기

            // resources/static/fonts/NanumGothic.ttf 파일을 읽어옴
            byte[] fontBytes = new ClassPathResource("static/fonts/NanumGothic.ttf")
                    .getInputStream()
                    .readAllBytes();

            FontProgram fontProgram = FontProgramFactory.createFont(fontBytes);
            fontProvider.addFont(fontProgram);
            properties.setFontProvider(fontProvider);

            // 5. PDF 변환 (설정값 properties를 꼭 같이 넘겨줘야 함!)
            HtmlConverter.convertToPdf(htmlContent, baos, properties);

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("PDF 생성 중 에러 발생", e);
        }
    }
}