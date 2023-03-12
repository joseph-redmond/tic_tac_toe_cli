package com.josephredmond.models;

import com.josephredmond.enums.GamePiece;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Piece {
	private GamePiece player;
	private GamePiece ai;
}
