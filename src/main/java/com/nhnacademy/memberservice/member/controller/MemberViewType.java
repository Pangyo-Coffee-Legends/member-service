package com.nhnacademy.memberservice.member.controller;

import lombok.Getter;

@Getter
public enum MemberViewType {
    SUMMARY("summary"), DETAILED("detailed");


    private final String name;

    MemberViewType(String name) {
        this.name = name;
    }

    public static MemberViewType from(String name) {
        for (MemberViewType type : values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid view type: " + name);
    }

}
