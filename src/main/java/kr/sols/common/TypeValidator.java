package kr.sols.common;

import kr.sols.domain.company.entity.IndustryType;

import java.util.List;

public class TypeValidator {
    public static boolean isValidIndustryTypeList(List<String> industryTypes) {
        return industryTypes.stream().allMatch(IndustryType::isValid);
    }

    public static void main(String[] args) {
        List<String> industryTypes = List.of("빅테크");
        boolean isValid = isValidIndustryTypeList(industryTypes);
        System.out.println("Is the industry type list valid? " + isValid);
    }


}
