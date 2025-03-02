package kr.sols.domain.company.entity;

public enum IndustryType {
    IT_SERVICE("IT 서비스"),
    FINANCE("금융"),
    SOLUTION("솔루션"),
    GAME("게임"),
    SI("SI"),
    SM("SM"),
    BIG_TECH("빅테크"),
    START_UP("스타트업"),
    MAJOR("대기업"),
    MEDIUM("중견기업"),
    SMALL("중소기업"),
    PUBLIC("공기업"),
    EDUCATION("교육기관");

    private final String description;

    IndustryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static boolean isValid(String value) {
        for (IndustryType type : values()) {
            if (type.getDescription().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
