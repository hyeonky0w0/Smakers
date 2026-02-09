package com.example.smakersbe.report.service;

import com.example.smakersbe.ai.entity.AiChat;
import com.example.smakersbe.ai.repository.AiChatRepository;
import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.entity.Memo;
import com.example.smakersbe.asset.repository.AssetRepository;
import com.example.smakersbe.asset.repository.MemoRepository;
import com.example.smakersbe.asset.repository.UserAssetRepository;
import com.example.smakersbe.report.dto.request.ReportRequestDTO;
import com.example.smakersbe.report.dto.request.SelectedAiChatData;
import com.example.smakersbe.report.dto.request.SelectedMemoData;
import com.example.smakersbe.report.util.PdfGenerator;
import com.example.smakersbe.user.entity.User;
import com.example.smakersbe.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final AiChatRepository aiChatRepository;
    private final AssetRepository assetRepository;

    // pdf 추출
    // 프론트에서 user정보 + aiChats + memos 에 대한 특정 Id값들 받기 -> 레파지토리로 각각의 데이터 추출 -> pdf 생성 -> url 반환
    // case1) isImportant 들만 모아서 추출, case2) 전체 추출
    @Override
    @Transactional(readOnly = true)
    public byte[] createReport (Long userId, Long assetId, boolean onlyImportant, MultipartFile captureImage){

        boolean isOwner = userAssetRepository.existsByUser_UserIdAndAsset_AssetId(userId, assetId);
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 에셋에 대한 접근 권한이 없습니다.");
        }

        // 에셋 이름 가져오기
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("에셋을 찾을 수 없습니다."));
        String assetName = asset.getAssetName();

        // 메모 가져오기
        List<Memo> targetMemos = onlyImportant
                ? memoRepository.findByUser_UserIdAndAsset_AssetIdAndIsImportantTrueAndDeletedAtIsNullOrderByCreatedAtAsc(userId, assetId)
                : memoRepository.findByUser_UserIdAndAsset_AssetIdAndDeletedAtIsNullOrderByCreatedAtAsc(userId, assetId);

        List<SelectedMemoData> selectedMemos = targetMemos.stream()
                .map(m -> new SelectedMemoData(m.getMemoTitle(), m.getMemoContents()))
                .toList();

        // 유저의 채팅 정보 가져오기
        List<AiChat> targetChats = onlyImportant
                ? aiChatRepository.findByUser_UserIdAndAsset_AssetIdAndIsImportantTrueOrderByCreatedAtAsc(userId, assetId)
                : aiChatRepository.findByUser_UserIdAndAsset_AssetIdOrderByCreatedAtAsc(userId, assetId);

        List<SelectedAiChatData> selectedChats = targetChats.stream()
                .map(aiChatData -> new SelectedAiChatData(
                        aiChatData.getQuestion().trim(),
                        aiChatData.getAnswer().trim()
                ))
                .toList();


        return pdfGenerator.generate(assetName,selectedMemos, selectedChats, captureImage);

    }








}
