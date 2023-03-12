package com.josephredmond.models;

import com.josephredmond.enums.GamePiece;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Board {
	private GamePiece[][] board;

}
