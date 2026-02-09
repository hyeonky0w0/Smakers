package com.example.smakersbe.quiz.init;

import com.example.smakersbe.asset.entity.Asset;
import com.example.smakersbe.asset.repository.AssetRepository;
import com.example.smakersbe.quiz.entity.QuizSet;
import com.example.smakersbe.quiz.entity.QuizSetItem;
import com.example.smakersbe.quiz.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j

public class QuizDatabaseSeeder implements CommandLineRunner {
    private final QuizSetRepository quizSetRepository;
    private final QuizSetItemRepository quizSetItemRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizResultRepository quizResultRepository;
    private final QuizUserAnswerRepository quizUserAnswerRepository;
    private final AssetRepository assetRepository;


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        log.info("=== 퀴즈 데이터 전면 초기화 및 시딩 시작 ===");

        // 1. 역순 삭제 (외래 키 제약 조건 방지)
        quizUserAnswerRepository.deleteAllInBatch();
        quizResultRepository.deleteAllInBatch();
        quizAttemptRepository.deleteAllInBatch();
        quizSetItemRepository.deleteAllInBatch();
        quizSetRepository.deleteAllInBatch();
        log.info("기존 퀴즈 관련 모든 테이블 데이터 삭제 완료");
        if (quizSetRepository.count() > 0) {
            log.info("이미 퀴즈 데이터가 존재하므로 시더를 실행하지 않습니다.");
            return;
        }

        log.info("퀴즈 데이터 시딩을 시작합니다 (선지 숫자 제거 버전)...");

        // 1. QuizSet 생성 (16개)
        long[] assetMapping = {1, 1, 1, 2, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7};
        List<QuizSet> quizSets = new ArrayList<>();
        for (long assetId : assetMapping) {
            Asset asset = assetRepository.findById(assetId)
                    .orElseThrow(() -> new RuntimeException("에셋 ID " + assetId + "가 없습니다."));
            quizSets.add(quizSetRepository.save(QuizSet.builder().asset(asset).build()));
        }

        List<QuizSetItem> items = new ArrayList<>();

        // --- Quiz Set 1 (Asset 1) ---
        items.add(createItem("Arm gear의 주요 역할로 가장 적절한 것은 무엇인가?", "[\"드론 기체 하부를 지지한다\", \"드론 암의 회전 구동을 담당한다\", \"비행 시 진동을 흡수한다\", \"양력을 직접 생성한다\"]", 1L, "Arm gear에 대한 설명입니다: 드론 암(Drone Arm)의 회전 구동을 담당하는 기어 부품입니다.", "이 부품은 드론의 ‘팔’ 부분에서 회전과 관련된 역할을 합니다.", quizSets.get(0)));
        items.add(createItem("다음 중 Main frame의 역할로 옳지 않은 것은?", "[\"모터와 암을 결합한다\", \"기체의 구조적 강성을 담당한다\", \"기체 구성 요소들을 지지한다\", \"회전 시 공기를 밀어 양력을 생성한다\"]", 3L, "메인 프레임은 많은 부품을 지지하고 결합하지만, 공기를 밀지는 않습니다.", "공기를 밀어내면서 드론을 위로 띄우는 역할을 하는 부품을 떠올려보세요.", quizSets.get(0)));
        items.add(createItem("드론 구동 시스템에서 회전력 전달 효율과 안정성에 기여하는 부품은 무엇인가?", "[\"Impeller Blade\", \"Leg\", \"Geearing\", \"Main frame\"]", 2L, "Geearing은 드론 구동 시스템 내부에서 모터의 회전력을 전달하는 핵심 기어 부품입니다.", "톱니바퀴처럼 서로 맞물려 돌아가며 에너지가 새나가지 않게 돕는 부품을 떠올려 보세요!", quizSets.get(0)));
        items.add(createItem("Main frame에 대한 설명으로 옳지 않은 것은?", "[\"드론의 주요 부품들을 결합한다\", \"기체의 구조적 안정성을 담당한다\", \"모터와 암을 지지한다\", \"양력을 직접 생성한다\"]", 3L, "Main frame은 드론의 모든 주요 부품을 지지하는 핵심 구조 부품입니다.", "드론의 '뼈대' 역할을 하는 부품을 떠올려 보세요.", quizSets.get(0)));

        // --- Quiz Set 2 (Asset 1) ---
        items.add(createItem("Main frame_MIR에 대한 설명으로 가장 적절한 것은?", "[\"메인 프레임을 기반으로 설계된 확장형 프레임이다\", \"기본 메인 프레임을 대체하는 경량 부품이다\", \"메인 프레임과 무관한 독립 구조이다\", \"진동 흡수 전용 구조물이다\"]", 0L, "Main frame_MIR은 기존 구조를 기반으로 설계된 확장형 기체 프레임입니다.", "MIR은 확장형 모델임을 의미합니다.", quizSets.get(1)));
        items.add(createItem("Beater disc에 대한 설명으로 옳은 것은?", "[\"기체의 구조적 강성을 담당한다\", \"회전력을 저장하는 역할을 한다\", \"회전력 전달과 진동 흡수를 동시에 수행한다\", \"이착륙 시 충격을 흡수한다\"]", 2L, "Beater disc는 고속 회전과 충격 완화 기능을 동시에 수행하는 핵심 구동 부품입니다.", "모터의 힘을 전달하는 우체부이자 진동을 잡아주는 완충기 역할을 합니다.", quizSets.get(1)));
        items.add(createItem("다음 중 Arm gear에 대한 설명으로 옳지 않은 것은?", "[\"비행 시 양력을 직접 생성한다\", \"드론 암의 회전 구동을 담당한다\", \"회전력을 전달하는 기어 부품이다\", \"금속 소재로 제작된다\"]", 0L, "비행 시 양력을 직접 생성하는 부품은 임펠러 블레이드입니다.", "톱니바퀴가 맞물려 돌아가며 힘을 전달하는 부품입니다.", quizSets.get(1)));
        items.add(createItem("Impeller Blade의 역할로 가장 적절한 것은?", "[\"기체의 무게를 분산한다\", \"회전 시 공기를 밀어 추력을 발생시킨다\", \"회전력을 다른 기어로 전달한다\", \"기체의 구조를 고정한다\"]", 1L, "모터의 회전력을 받아 공기를 아래로 강력하게 밀어내어 양력을 만들어냅니다.", "드론이 하늘로 떠오르기 위해 필요한 '날개'의 역할을 떠올려 보세요.", quizSets.get(1)));

        // --- Quiz Set 3 (Asset 1) ---
        items.add(createItem("다음 중 Beater disc의 기능 설명으로 가장 부적절한 것은?", "[\"고속 회전을 수행한다\", \"회전력을 프로펠러로 전달한다\", \"비행 시 발생하는 미세 진동을 흡수한다\", \"기체 하부의 착륙 충격을 흡수한다\"]", 3L, "기체 하부에서 착륙 시 충격을 흡수하는 부품은 Leg입니다.", "땅에 닿을 때 충격을 몸으로 받아내는 부품(다리)은 따로 있습니다.", quizSets.get(2)));
        items.add(createItem("다음 중 회전력 전달 흐름이 가장 자연스러운 조합은?", "[\"Nut → Screw → Leg\", \"Motor → Beater disc → Impeller Blade\", \"Main frame → Leg → Nut\", \"Impeller Blade → Main frame → Arm gear\"]", 1L, "드론의 비행 동력은 Motor에서 Beater disc를 거쳐 Impeller Blade로 전달됩니다.", "에너지가 어디서 시작해서 어디로 전달되는지 힘의 통로를 따라가 보세요.", quizSets.get(2)));
        items.add(createItem("Impeller Blade가 정상적으로 기능하기 위해 직접적으로 필요한 부품 조합으로 가장 적절한 것은?", "[\"Beater disc + Geearing\", \"Main frame + Leg\", \"Nut + Screw\", \"xyz + Main frame\"]", 3L, "Main frame과 xyz는 구성 요소 간의 간격을 유지하고 정렬하는 역할을 합니다.", "기초 뼈대와 날개의 위치를 정확하게 잡아줄 정렬 부품이 필수적입니다.", quizSets.get(2)));
        items.add(createItem("다음 중 기체 하부 구조 형성과 가장 밀접한 부품 조합은?", "[\"Arm gear + Geearing\", \"Nut + Screw\", \"Leg + Main frame\", \"Impeller Blade + Beater disc\"]", 2L, "Leg와 Main frame이 결합하여 기체 전체의 강성과 구조적 안정성을 유지합니다.", "기체의 무게를 지탱하고 흔들리지 않게 고정해 주는 부품들을 찾아보세요.", quizSets.get(2)));

        // --- Quiz Set 4 (Asset 2) ---
        items.add(createItem("Clamp-Center의 주요 역할로 가장 적절한 것은?", "[\"리프 스프링의 탄성을 조절한다\", \"리프 스프링의 중앙부를 고정해 정렬을 유지한다\", \"주행 충격을 직접 흡수한다\", \"차체와 서스펜션을 연결한다\"]", 1L, "Clamp-Center는 리프 스프링의 중앙 부분을 견고하게 결합하여 정렬을 유지합니다.", "여러 겹으로 쌓인 리프 스프링을 하나로 묶어주는 집게 역할을 합니다.", quizSets.get(3)));
        items.add(createItem("다음 중 주행 시 발생하는 하중을 차체로 전달하는 역할과 가장 밀접한 부품은?", "[\"Support-Chassis\", \"Leaf-Layer\", \"Clamp-Secondary\", \"Support-Rubber\"]", 0L, "Support-Chassis는 차체와 서스펜션을 연결하여 충격을 차체로 전달합니다.", "자동차의 몸체인 '차체(Chassis)' 사이를 이어주는 다리 역할을 합니다.", quizSets.get(3)));
        items.add(createItem("Leaf-Layer에 대한 설명으로 옳지 않은 것은?", "[\"여러 장이 적층되어 사용된다\", \"하중을 지지한다\", \"금속 소재로 제작된다\", \"탄성 변형을 통해 충격을 흡수한다\"]", 2L, "Leaf-Layer의 소재는 금속이 아닌 플라스틱으로 명시되어 있습니다.", "하중을 견디고 탄성을 이용해 충격을 흡수하는 장치입니다.", quizSets.get(3)));
        items.add(createItem("Clamp-Primary의 역할로 가장 적절한 것은?", "[\"리프 스프링의 중앙을 고정한다\", \"리프 스프링과 차체를 연결한다\", \"금속 부품 사이의 진동을 흡수한다\", \"스프링 적층을 보조적으로 지지한다\"]", 1L, "Clamp-Primary는 리프 스프링을 프레임(차체)에 고정하는 체결 부품입니다.", "리프 스프링과 자동차의 뼈대를 단단하게 묶어주는 연결 고리입니다.", quizSets.get(3)));

        // --- Quiz Set 5 (Asset 2) ---
        items.add(createItem("다음 중 진동·충격 완화를 주된 목적으로 사용하는 부품은?", "[\"Support\", \"Clamp-Secondary\", \"Support-Rubber\", \"Support-Chassis Rigid\"]", 2L, "Support-Rubber는 고무 소재를 활용해 하중과 진동을 안정적으로 전달 및 완화합니다.", "탄성 소재가 사용되는 부품을 찾아보세요.", quizSets.get(4)));
        items.add(createItem("Support 부품의 주요 기능으로 가장 적절한 것은?", "[\"리프 스프링의 탄성을 직접 생성한다\", \"구조물을 지지하고 하중을 분산한다\", \"회전 운동을 전달한다\", \"진동을 흡수해 소음을 제거한다\"]", 1L, "Support 부품은 구조물을 지지하고 하중을 골고루 분산시킵니다.", "'Support(지지)'라는 단어의 의미를 떠올려보세요.", quizSets.get(4)));
        items.add(createItem("다음 중 프레임 결합부의 강성과 구조적 안정성을 직접적으로 확보하는 부품은?", "[\"Support-Chassis\", \"Support\", \"Support-Rubber\", \"Support-Chassis Rigid\"]", 3L, "Support-Chassis Rigid는 결합부의 강성을 높여 구조적 안정성을 확보합니다.", "이름에 ‘Rigid(강성이 강한)’가 포함된 이유를 생각해보세요.", quizSets.get(4)));
        items.add(createItem("Clamp-Secondary에 대한 설명으로 가장 부적절한 것은?", "[\"리프 스프링을 보조적으로 고정한다\", \"주 클램프와 함께 사용된다\", \"하중 분산을 보조한다\", \"리프 스프링의 탄성을 직접 생성한다\"]", 3L, "클램프는 스프링을 묶어주는 역할이며, 탄성은 Leaf-Layer가 담당합니다.", "보조적인(Secondary) 고정 역할을 수행하는 부품입니다.", quizSets.get(4)));

        // --- Quiz Set 6 (Asset 2) ---
        items.add(createItem("리프 스프링 시스템에서 여러 장이 적층되어 사용되는 부품은 무엇인가?", "[\"Clamp-Center\", \"Support\", \"Leaf-Layer\", \"Support-Rubber\"]", 2L, "판 형태의 스프링인 Leaf-Layer를 여러 겹으로 적층하여 하중을 지지합니다.", "여러 장이 겹겹이 쌓여 무게를 견디는 부품을 찾아보세요.", quizSets.get(5)));
        items.add(createItem("다음 중 금속 부품 사이에 삽입되어 소음을 저감하는 역할을 하는 부품은?", "[\"Clamp-Primary\", \"Support-Rubber\", \"Support\", \"Support-Chassis\"]", 1L, "Support-Rubber는 층층이 쌓인 구조 사이에서 진동을 흡수하고 승차감을 제공합니다.", "고무 소재가 사용되는 부품입니다.", quizSets.get(5)));
        items.add(createItem("Support-Chassis의 주요 역할로 가장 적절한 것은?", "[\"리프 스프링의 중앙을 고정한다\", \"주행 충격을 직접 흡수한다\", \"차체와 서스펜션 부품을 연결한다\", \"스프링 적층 위치를 유지한다\"]", 2L, "Support-Chassis는 자동차의 프레임과 서스펜션을 이어주는 연결부입니다.", "자동차의 몸체인 'Chassis'와 연결되는 다리 역할을 합니다.", quizSets.get(5)));
        items.add(createItem("다음 중 하중을 직접 지지하는 역할이 가장 강한 부품은?", "[\"Support-Chassis Rigid\", \"Support-Rubber\", \"Leaf-Layer\", \"Clamp-Secondary\"]", 0L, "Rigid 부품은 결합부의 강성을 높여 주요 하중을 직접 지탱합니다.", "강성 보강이라는 표현에 주목해보세요.", quizSets.get(5)));

        // --- Quiz Set 7 (Asset 3) ---
        items.add(createItem("TrapezSpindel의 주요 역할로 가장 적절한 것은?", "[\"이동 죠의 직선 운동을 안내한다\", \"회전 운동을 직선 운동으로 변환해 클램핑 힘을 생성한다\", \"공작물과 직접 접촉해 고정한다\", \"스핀들의 축방향 하중을 지지한다\"]", 1L, "회전 운동을 직선 운동으로 변환하여 이동 죠를 움직이고 클램핑 힘을 생성합니다.", "핸들을 돌리는 동작이 어떻게 물체를 조이는 힘으로 변하는지 생각해보세요.", quizSets.get(6)));
        items.add(createItem("이동 죠의 직선 운동을 안내하고 정렬을 유지하는 부품 조합으로 가장 적절한 것은?", "[\"Fuhrung + fuhrungschiene\", \"spindelsockel+Druckhulse\", \"Feste Backe +Spannbacke\", \"grundplatte +spindelsockel\"]", 0L, "Fuhrung(가이드)은 이동 죠의 직선 운동 통로 역할을 합니다.", "“가이드”라는 표현이 반복되는 부품을 떠올려보세요.", quizSets.get(6)));
        items.add(createItem("Feste Backe에 대한 설명으로 옳지 않은 것은?", "[\"머신 바이스에 고정된 죠이다\", \"이동 죠와 함께 공작물을 지지한다\", \"나사 구동에 따라 전후로 이동한다\", \"가공 중 위치 기준을 제공한다\"]", 2L, "Feste Backe는 고정된 부품이며, 전후로 이동하는 부품은 이동 죠입니다.", "'Feste'는 독일어로 '고정된'이라는 의미입니다.", quizSets.get(6)));
        items.add(createItem("공작물과 직접 접촉하여 미끄럼을 방지하고 고정력을 전달하는 부품은?", "[\"lose backe\", \"Spannbacke\", \"Fuhrung\", \"spindelsockel\"]", 1L, "Spannbacke는 공작물과 직접 접촉하여 미끄럼을 방지하는 소모성 부품입니다.", "물건을 잡을 때 미끄러지지 않게 해주는 지문 같은 역할을 합니다.", quizSets.get(6)));

        // --- Quiz Set 8 (Asset 3) ---
        items.add(createItem("머신 바이스 전체 구조의 강성과 안정성을 확보하는 하부 구성 부품은?", "[\"Druckhulse\", \"fuhrungschiene\", \"grundplatte\", \"Fuhrung\"]", 2L, "Grundplatte는 가장 하단에 위치하여 전체 구조물을 지지하는 뿌리 역할을 합니다.", "건물의 바닥에 다지는 기초 공사 같은 부품입니다.", quizSets.get(7)));
        items.add(createItem("Fuhrung의 주요 기능으로 가장 적절한 것은?", "[\"클램핑 힘을 직접 생성한다\", \"이동 죠의 직선 운동을 안내하고 정렬을 유지한다\", \"공작물과 직접 접촉한다\", \"축방향 하중을 흡수한다\"]", 3L, "Fuhrung은 가이드 역할과 동시에 강력한 축방향 하중을 흡수합니다.", "기차가 탈선하지 않게 잡아주는 레일 같은 역할을 떠올려 보세요.", quizSets.get(7)));
        items.add(createItem("스핀들의 회전 축을 고정하고 안정적인 동력 전달을 보조하는 부품은?", "[\"TrapezSpindel\", \"spindelsockel\", \"Druckhulse\", \"fuhrungschiene\"]", 1L, "spindelsockel은 회전하는 스핀들의 축을 물리적으로 고정하는 소켓 역할을 합니다.", "나사가 회전할 때 축이 흔들리지 않게 잡아주는 받침대입니다.", quizSets.get(7)));
        items.add(createItem("lose backe에 대한 설명으로 가장 부적절한 것은?", "[\"머신 바이스에서 이동하는 죠이다\", \"나사 구동에 따라 전후로 이동한다\", \"공작물을 고정하고 클램핑력을 전달한다\", \"머신 바이스에 고정되어 위치 기준을 제공한다\"]", 3L, "lose backe는 움직이는 죠이며, 고정되어 기준을 제공하는 것은 Feste Backe입니다.", "핸들을 돌릴 때마다 움직이는 '아래턱' 같은 부품입니다.", quizSets.get(7)));

        // --- Quiz Set 9 (Asset 4) ---
        items.add(createItem("base의 주요 역할로 가장 적절한 것은?", "[\"로봇 암 말단의 파지 동작을 수행한다\", \"로봇 암 전체를 지지하고 작업면에 고정한다\", \"관절 회전을 통해 자세를 제어한다\", \"그리퍼의 회전력을 직접 전달한다\"]", 1L, "베이스는 로봇 암 전체의 무게를 지탱하고 작업면에 고정시키는 기능을 합니다.", "건축물의 기초 같은 역할을 떠올려 보세요.", quizSets.get(8)));
        items.add(createItem("로봇 암의 초기 동작 범위와 방향 제어를 담당하는 부품은?", "[\"Upper Arm Link\", \"Shouler Joint Housing\", \"Lower Arm Link\", \"Gripper\"]", 1L, "Shoulder Joint Housing은 베이스와 팔을 연결하여 초기 방향을 제어합니다.", "사람의 몸에서 팔 전체의 방향을 결정하는 어깨 역할을 합니다.", quizSets.get(8)));
        items.add(createItem("Upper Arm Link에 대한 설명으로 옳지 않은 것은?", "[\"두 관절을 연결하는 링크 부품이다\", \"회전 운동에 따른 하중과 토크를 전달한다\", \"로봇 암 말단에서 대상물을 직접 파지한다\", \"동작 범위를 확장하는 역할을 한다\"]", 2L, "Upper Arm Link는 뼈대 역할이지 물체를 직접 파지하는 부위가 아닙니다.", "관절 사이를 이어주는 팔뚝 뼈 같은 역할입니다.", quizSets.get(8)));
        items.add(createItem("상부 링크와 하부 링크를 연결해 자세 변화와 위치 제어를 담당하는 부품은?", "[\"Elbow Joint Module\", \"Lower Arm Link\", \"Wrist Joint Unit\", \"End Effector Mount\"]", 2L, "Wrist Joint Unit(손목)은 그리퍼의 각도를 조절하여 정밀한 위치를 제어합니다.", "물체를 잡기 직전 각도를 조절하는 부위를 생각해보세요.", quizSets.get(8)));

        // --- Quiz Set 10 (Asset 4) ---
        items.add(createItem("관절 구동 시 발생하는 하중과 토크를 안정적으로 지지하는 부품은?", "[\"Gripper\", \"Upper Arm Link\", \"Lower Arm Link\", \"Shouler Joint Housing\"]", 2L, "Lower Arm Link는 관절들이 원활하게 움직이도록 뼈대를 형성하고 지지합니다.", "회전의 중심축을 만들고 물리적 스트레스를 견뎌내는 부품입니다.", quizSets.get(9)));
        items.add(createItem("로봇 암의 말단 관절로, 그리퍼의 각도 조절을 가능하게 하는 부품은?", "[\"Wrist Joint Unit\", \"Elbow Joint Module\", \"Shouler Joint Housing\", \"End Effector Mount\"]", 0L, "Wrist Joint Unit은 로봇 암의 말단에서 그리퍼의 각도를 세밀하게 조절합니다.", "손목 역할을 하는 관절 부위를 찾아보세요.", quizSets.get(9)));
        items.add(createItem("구동부의 회전력을 그리퍼로 전달하는 인터페이스 역할을 수행하는 부품은?", "[\"Gripper\", \"Wrist Joint Unit\", \"End Effector Mount\", \"Lower Arm Link\"]", 2L, "End Effector Mount는 본체와 그리퍼 사이를 잇는 연결 인터페이스입니다.", "팔의 끝단에서 그리퍼를 단단히 고정해주는 장착부입니다.", quizSets.get(9)));
        items.add(createItem("Gripper에 대한 설명으로 가장 부적절한 것은?", "[\"대상물을 직접 파지한다\", \"개폐 동작을 수행한다\", \"관절 회전을 통해 파지 각도를 조절한다\", \"로봇 암 전체의 하중을 분산한다\"]", 3L, "그리퍼는 물건을 잡는 손의 역할이지 하중 분산을 주된 목적으로 하지 않습니다.", "물건을 잡는 손의 역할과 바닥을 지지하는 발의 역할을 구분해 보세요.", quizSets.get(9)));

        // --- Quiz Set 11 (Asset 5) ---
        items.add(createItem("Base Gear의 주요 역할로 가장 적절한 것은?", "[\"구동 모터의 회전력을 전달해 그리퍼 개폐를 제어한다\", \"그리퍼를 프레임에 고정한다\", \"내부 부품의 위치를 정렬한다\", \"링크와 기어를 회전 축으로 연결한다\"]", 0L, "Base Gear는 모터에서 발생한 회전력을 전달받아 개폐 동작이 가능하게 합니다.", "모터와 집게 사이를 이어주는 동력 전달의 시작점입니다.", quizSets.get(10)));
        items.add(createItem("Base Mounting Bracket의 기능으로 가장 적절한 것은?", "[\"그리퍼 암의 동기화를 보조한다\", \"그리퍼 베이스를 프레임에 고정하고 하중·진동을 지지한다\", \"회전 운동을 링크로 전달한다\", \"물체를 직접 파지한다\"]", 1L, "그리퍼 본체를 로봇 팔에 고정하고 작업 시 발생하는 하중을 분산시킵니다.", "그리퍼가 흔들리지 않게 꽉 잡아주는 기초 설치 부품입니다.", quizSets.get(10)));
        items.add(createItem("Base Plate에 대한 설명으로 옳지 않은 것은?", "[\"내부 부품을 지지하고 정렬한다\", \"기어와 샤프트 위치를 정확히 고정한다\", \"전체 구조의 강성과 안정성을 확보한다\", \"그리퍼 개폐를 직접 수행한다\"]", 3L, "Base Plate는 조립되는 기초 판이며, 직접 개폐를 수행하지는 않습니다.", "내부 부품들이 제자리에 있도록 도와주는 무대 같은 역할입니다.", quizSets.get(10)));
        items.add(createItem("기어의 회전 운동을 링크 메커니즘으로 전달하는 부품은?", "[\"Link\", \"Gear link\", \"Pin\", \"그리퍼\"]", 1L, "Gear link는 회전 운동을 그리퍼가 벌어지거나 다무는 운동으로 변환합니다.", "회전력을 링크로 전달하는 연결 고리입니다.", quizSets.get(10)));

        // --- Quiz Set 12 (Asset 5) ---
        items.add(createItem("물체를 직접 파지하며 미끄러짐을 방지하는 핵심 부품은?", "[\"Gripper\", \"Pin\", \"Base Plate\", \"Gear link\"]", 0L, "그리퍼는 물체에 직접 닿아 정밀한 파지력으로 대상물을 고정합니다.", "물체와 직접 접촉하여 악력을 전달하는 부품입니다.", quizSets.get(11)));
        items.add(createItem("Gear link 2의 역할로 가장 적절한 것은?", "[\"회전 축을 형성해 정렬을 유지한다\", \"보조 기어와 연결되어 양쪽 암의 동기화를 보조한다\", \"내부 부품을 고정·정렬한다\", \"그리퍼를 프레임에 고정한다\"]", 1L, "그리퍼의 양쪽 암이 똑같이 움직이도록 동기화하는 것을 보조합니다.", "양쪽 집게가 어긋나지 않게 도와주는 역할에 집중해 보세요.", quizSets.get(11)));
        items.add(createItem("Link 부품의 기능으로 가장 적절한 것은?", "[\"회전력을 저장한다\", \"내부 구조의 강성을 확보한다\", \"회전 운동을 직선 또는 각도 변화로 전달한다\", \"물체를 직접 파지한다\"]", 2L, "Link는 기어의 회전 운동을 그리퍼 입을 벌리거나 다무는 운동으로 변환합니다.", "기어의 힘을 받아 실제로 움직이게 하는 다리 역할을 합니다.", quizSets.get(11)));
        items.add(createItem("Pin에 대한 설명으로 가장 부적절한 것은?", "[\"회전 축 역할을 수행한다\", \"부품 간 정확한 위치 정렬을 유지한다\", \"동작 중 발생하는 하중을 지지한다\", \"그리퍼 개폐 각도를 직접 제어한다\"]", 3L, "Pin은 연결 중심축일 뿐 개폐 각도를 직접 제어하는 핵심 부품은 아닙니다.", "뼈마디를 이어주어 회전할 수 있게 돕는 관절 중심 못 같은 존재입니다.", quizSets.get(11)));

        // --- Quiz Set 13 (Asset 6) ---
        items.add(createItem("BASE의 주요 역할로 가장 적절한 것은?", "[\"스프링의 복원력을 직접 생성한다\", \"서스펜션 하부를 지지하고 전체 하중을 분산한다\", \"상·하 움직임을 직접 전달한다\", \"스프링과 샤프트를 체결한다\"]", 1L, "BASE는 전체 무게를 안정적으로 전달하고 구조적 안정성을 유지합니다.", "전체 무게를 든든하게 받쳐주는 기초 부품입니다.", quizSets.get(12)));
        items.add(createItem("서스펜션 구성 부품을 축 방향으로 고정해 풀림을 방지하는 부품은?", "[\"NUT\", \"ROD\", \"SPRING\", \"NIT\"]", 3L, "NIT는 진동 속에서도 부품이 풀리지 않도록 축 방향으로 고정합니다.", "반복 진동 환경에서도 풀리지 않게 막는 역할을 찾으세요.", quizSets.get(12)));
        items.add(createItem("ROD에 대한 설명으로 옳지 않은 것은?", "[\"상·하 움직임을 전달한다\", \"스프링의 압축·복원 동작을 가이드한다\", \"하중과 진동을 안정적으로 전달한다\", \"외부 충격을 흡수하는 탄성 부품이다\"]", 3L, "ROD는 방향을 잡아주는 길잡이이며, 탄성 부품은 스프링입니다.", "스프링이 올바른 방향으로 움직이도록 돕는 기둥 역할을 합니다.", quizSets.get(12)));
        items.add(createItem("서스펜션 시스템에서 복원력과 승차감을 담당하는 부품은?", "[\"BASE\", \"NUT\", \"SPRING\", \"ROD\"]", 2L, "SPRING은 탄성을 이용해 충격을 흡수하고 수평을 유지해 승차감을 높입니다.", "충격이 왔을 때 흡수하고 원래 모양으로 돌아가려는 성질을 가진 부품입니다.", quizSets.get(12)));

        // --- Quiz Set 14 (Asset 6) ---
        items.add(createItem("BASE와 상부 샤프트, 완충 부품을 함께 고정해 구조 안정성을 확보하는 목적은?", "[\"조립 편의성 향상\", \"외관 개선\", \"서스펜션 동작 시 힘 분산\", \"마찰 증가\"]", 2L, "강한 물리적 에너지를 골고루 분산시켜 구조가 뒤틀리지 않게 하기 위함입니다.", "압력을 한 곳에 쏠리지 않게 나누는 것이 중요합니다.", quizSets.get(13)));
        items.add(createItem("NUT의 기능으로 가장 적절한 것은?", "[\"스프링의 탄성을 생성한다\", \"스프링과 내부 부품을 고정하고 하중을 분산한다\", \"상·하 운동을 가이드한다\", \"하부 구조를 지지한다\"]", 1L, "NUT는 샤프트에 체결되어 부품들을 고정하고 하중을 분산시킵니다.", "체결과 하중 분산이 핵심 키워드입니다.", quizSets.get(13)));
        items.add(createItem("SPRING이 정상적으로 작동하기 위해 직접적으로 연동되는 부품 조합으로 가장 적절한 것은?", "[\"BASE + NUT\", \"ROD + NUT\", \"ROD + BASE\", \"NIT + BASE\"]", 1L, "ROD가 동작을 가이드하고 NUT가 고정해주어야 스프링이 정상 작동합니다.", "스프링을 안내하고 단단히 잡아주는 조합을 찾아보세요.", quizSets.get(13)));
        items.add(createItem("NIT에 대한 설명으로 가장 부적절한 것은?", "[\"축 방향으로 부품을 고정한다\", \"반복 진동 환경에서도 고정력을 유지한다\", \"외부 충격을 흡수하는 탄성 부품이다\", \"샤프트와 스프링과 결합된다\"]", 2L, "NIT는 고정력을 유지하는 부품이지 충격 흡수용 탄성 부품은 아닙니다.", "풀리지 않게 꽉 잡아주는 역할에 집중하세요.", quizSets.get(13)));

        // --- Quiz Set 15 (Asset 7) ---
        items.add(createItem("Piston의 주요 역할로 가장 적절한 것은?", "[\"크랭크샤프트를 회전시킨다\", \"연소 압력을 받아 상하 운동하며 동력을 생성한다\", \"실린더와의 기밀을 유지하는 역할만 수행한다\", \"커넥팅 로드를 고정한다\"]", 1L, "피스톤은 연소 압력을 받아 상하 왕복 운동을 하며 기초 동력을 만듭니다.", "실린더 안에서 위아래로 움직이며 힘을 만드는 주인공입니다.", quizSets.get(14)));
        items.add(createItem("Piston Ring의 기능으로 가장 적절한 것은?", "[\"피스톤과 커넥팅 로드를 연결한다\", \"피스톤의 왕복 운동을 회전 운동으로 바꾼다\", \"실린더와의 기밀을 유지하고 압축 손실을 방지한다\", \"크랭크샤프트의 회전 축을 형성한다\"]", 2L, "피스톤 링은 실린더 벽과의 기밀을 유지하여 압축 손실을 방지합니다.", "가스가 새나가지 않게 막아주는 봉인 역할을 떠올려 보세요.", quizSets.get(14)));
        items.add(createItem("Connecting Rod에 대한 설명으로 옳지 않은 것은?", "[\"피스톤과 크랭크샤프트를 연결한다\", \"직선 운동을 회전 운동으로 전달한다\", \"엔진 출력 축 역할을 수행한다\", \"동력 전달의 핵심 부품이다\"]", 2L, "Connecting Rod는 힘을 전달하는 막대이며, 출력 축은 크랭크샤프트입니다.", "피스톤과 축 사이를 잇는 연결 고리 역할을 합니다.", quizSets.get(14)));
        items.add(createItem("피스톤의 왕복 운동을 받아 엔진 출력으로 회전 운동을 생성하는 부품은?", "[\"Piston Pin\", \"Connecting Rod\", \"Crankshaft\", \"Connecting Rod Cap\"]", 2L, "Crankshaft는 왕복 운동 에너지를 회전 운동으로 최종 변환합니다.", "자동차를 나가게 만드는 엔진의 회전 중심축입니다.", quizSets.get(14)));

        // --- Quiz Set 16 (Asset 7) ---
        items.add(createItem("Connecting Rod Cap의 주요 역할로 가장 적절한 것은?", "[\"피스톤과 로드를 연결한다\", \"로드 하부에서 크랭크샤프트를 감싸 고정한다\", \"로드와 실린더를 연결한다\", \"엔진 회전을 직접 생성한다\"]", 1L, "로드 캡은 커넥팅 로드 하부에서 크랭크샤프트를 감싸 고정하여 내구성을 높입니다.", "캡(덮개) 역할을 하는 부품의 위치를 생각해보세요.", quizSets.get(15)));
        items.add(createItem("Piston Pin의 역할로 가장 적절한 것은?", "[\"피스톤의 기밀을 유지한다\", \"피스톤과 커넥팅 로드를 연결한다\", \"크랭크샤프트를 고정한다\", \"로드와 로드 캡을 체결한다\"]", 1L, "피스톤 핀은 피스톤과 커넥팅 로드 사이를 연결해 힘을 전달합니다.", "엔진의 머리와 팔을 이어주는 축 역할을 생각해보세요.", quizSets.get(15)));
        items.add(createItem("Conrod Bolt에 대한 설명으로 가장 적절한 것은?", "[\"엔진 회전을 출력으로 전달한다\", \"피스톤과 로드를 체결한다\", \"로드와 로드 캡을 체결해 반복 하중을 견든다\", \"크랭크샤프트의 축을 형성한다\"]", 2L, "이 볼트는 로드와 로드 캡을 하나로 묶어 고속 회전 시의 하중을 견딥니다.", "본체와 캡이 떨어지지 않게 꽉 조여주는 부품입니다.", quizSets.get(15)));
        items.add(createItem("Crankshaft에 대한 설명으로 가장 부적절한 것은?", "[\"피스톤의 직선 운동을 회전 운동으로 변환한다\", \"커넥팅 로드를 통해 동력을 전달받는다\", \"엔진 출력으로 회전력을 전달한다\", \"실린더 내부에서 상하 운동한다\"]", 3L, "실린더 내부에서 상하 운동하는 것은 피스톤이며, 크랭크샤프트는 회전합니다.", "제자리에서 빙글빙글 도는 회전 축이라는 점을 기억하세요.", quizSets.get(15)));

        quizSetItemRepository.saveAll(items);
        log.info("총 64개의 문항 삽입 완료.");
    }

    private QuizSetItem createItem(String question, String options, Long answer, String explanation, String hint, QuizSet quizSet) {
        return QuizSetItem.builder()
                .question(question)
                .options(options)
                .answer(answer)
                .explanation(explanation)
                .hint(hint)
                .quizSet(quizSet)
                .build();
    }
}
