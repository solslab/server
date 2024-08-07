package kr.sols.common;

import kr.sols.domain.company.entity.IndustryType;

import java.util.List;

public class TypeValidator {
    public static boolean isValidIndustryTypeList(List<String> industryTypes) {
        return industryTypes.stream().allMatch(IndustryType::isValid);
    }

//    public static boolean isValidSupportLanguagesTypeList(List<String> supportLanguages) {
//        return supportLanguages.stream().allMatch(Sup::isValid);
//    }

}
