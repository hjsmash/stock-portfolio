package com.example.stockportfolio.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SymbolSearchResult {
    private String symbol;
    private String name;
}
