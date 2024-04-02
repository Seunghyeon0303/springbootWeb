package com.kh.Final_Project.orderitem.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PocketForm {
    private List<String> productIds;
    private List<Integer> counts;
}
