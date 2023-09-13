package kr.leedox.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Open {
    private int id;
    private String name;
    private int val;
}
