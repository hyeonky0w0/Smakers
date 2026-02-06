package com.example.smakersbe.report.service;

import com.example.smakersbe.ai.entity.AiChat;
import com.example.smakersbe.asset.repository.MemoRepository;
import com.example.smakersbe.asset.repository.UserAssetRepository;
import com.example.smakersbe.report.dto.request.ReportRequestDTO;
import com.example.smakersbe.report.dto.request.SelectedAiChatData;
import com.example.smakersbe.report.dto.request.SelectedMemoData;
import com.example.smakersbe.report.util.PdfGenerator;
import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final MemoRepository memoRepository;
    private final UserAssetRepository userAssetRepository;
    private final PdfGenerator pdfGenerator;

    // pdf 추출
    // 프론트에서 user정보 + aiChats + memos 에 대한 특정 Id값들 받기 -> 레파지토리로 각각의 데이터 추출 -> pdf 생성 -> url 반환
    @Override
    @Transactional(readOnly = true)
    public byte[] createReport (String uuid, Long assetId, ReportRequestDTO requestDTO, MultipartFile captureImage){

        boolean isOwner = userAssetRepository.existsByUserAndAsset_AssetId(uuid, assetId);
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 에셋에 대한 접근 권한이 없습니다.");
        }

        // 유저의 메모 가져오기
        List<SelectedMemoData> selectedMemos = memoRepository.findAllById(requestDTO.getMemoId()).stream()
                .map(memoData -> new SelectedMemoData(
                        memoData.getMemoTitle(),
                        memoData.getMemoContents()
                ))
                .toList();


        // 유저의 채팅 정보 가져오기
        List<SelectedAiChatData> selectedChats = memoRepository.findAllById(requestDTO.getAiChatId()).stream()
                .map(aiChatData -> new SelectedAiChatData(
                        aiChatData.getMemoTitle(),
                        aiChatData.getMemoContents()
                ))
                .toList();

        return pdfGenerator.generate(selectedMemos, selectedChats, captureImage);

    }








}
