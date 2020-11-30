# Chess game architecture

## Models
- BoardState
    - Board - 8x8 matrix
    - Turn - black or white
- Piece
- Position
- Move

## Services
- ChessAPI
    - move(currentState: BoardState, move: Move): BoardState
