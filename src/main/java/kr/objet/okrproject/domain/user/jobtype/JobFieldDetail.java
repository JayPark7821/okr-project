package kr.objet.okrproject.domain.user.jobtype;

import java.util.Arrays;

import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import kr.service.okr.common.utils.EnumLookUpUtil;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JobFieldDetail implements JobType {
    UI_UX_PLANNER("UI/UX 기획자"),
    GAME_PLANNER("게임 기획자"),
    PROJECT_MANAGER("프로젝트 메니저"),
    HARDWARE_PLANNER("하드웨어(제품) 기획"),
    ETC_PLANNER("(기획) 기타"),

    GRAPHIC_DESIGNER("그래픽 디자이너"),
    UI_UX_DESIGNER("UI/UX 디자이너"),
    THREEDIMENTION_DESIGNER("3D 디자이너"),
    HARDWARE_DESIGNER("하드웨어(제품) 디자이너"),
    ETC_DESIGNER("(디자인) 기타"),

    IOS_DEVELOPER("IOS 개발자"),
    ANDROID_DEVELOPER("Android 개발자"),
    WEB_FRONT_END_DEVELOPER("웹 프론트엔드 개발자 "),
    WEB_PUBLISHER("웹 퍼블리셔"),
    CROSS_PLATFORM_DEVELOPER("크로스 플랫폼 개발자"),

    WEB_SERVER_DEVELOPER("서버 개발자"),
    BLOCK_CHAIN_DEVELOPER("블록체인 개발자"),
    AI_DEVELOPER("AI 개발자"),
    DB_BIG_DATA_DS("DB/빅데이터/DS"),
    GAME_SERVER("게임 서버 개발자"),

    BUSINESS_PLANNING("사업 기획"),
    MARKETING("마케팅"),
    FINANCE_ACCOUNTING("재무/회계"),
    SALES("영업"),
    STRATEGY_CONSULTING("전략/컨설팅"),
    INVESTMENT_ADVISOR("투자/고문"),
    ETC_BUSINESS("사업/기타"),

    WRITER_BLOGGER("작가/블로거"),
    INFLUENCER_STREAMER("인플루언서/스트리머"),
    LAW_LABOR("법률/노무"),
    MEDICAL_MEDICINE("의료/의학"),
    CATERING_CHEF("요식업/쉐프"),
    PRODUCER_CP("프로듀서/CP"),
    COMPOSING("작곡(사운드)"),
    VIDEO("영상"),
    OPERATE("운영"),
    QA("QA"),
    ETC("기타") ;

    private String title;

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getTitle() {
        return title;
    }

    public static JobFieldDetail of(String code) {
        return Arrays.stream(JobFieldDetail.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_JOB_DETAIL_FIELD));
    }

    public static JobFieldDetail lookup(String id) {
        return EnumLookUpUtil.lookup(JobFieldDetail.class, id);
    }
}
