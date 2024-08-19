package kr.sols.common;

import kr.sols.domain.company.entity.IndustryType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TypeValidator {
    public static boolean isValidIndustryTypeList(List<String> industryTypes) {
        return industryTypes.stream().allMatch(IndustryType::isValid);
    }

    private static final List<String> SUPPORTED_LANGUAGES = Arrays.asList(
            "C", "C++", "C/C++", "C#", "Java", "JavaScript", "Kotlin",
            "Python", "Go", "Ruby", "Scala", "Swift", "SQL", "Oracle"
    );

    public static boolean isValidSupportLanguagesTypeList(List<String> supportLanguages) {
        if (supportLanguages == null || supportLanguages.isEmpty()) {
            return false;
        }

        return new HashSet<>(SUPPORTED_LANGUAGES).containsAll(supportLanguages);
    }
}
