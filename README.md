# chess-driver
An interface to play a game of chess, validate moves as per rules and interact with any uci compatible chess engines 
to play against a computer. The example code provided herewith uses Stockfish engine.

### Feature Implementation plans
- Define chess pieces, eg. moves, weights etc
- Define chess board, eg. valid positions, valid moves
- Console based game
- Interface for parsing uci commands and response
- Game against a computer using uci-engine

### TODOs
- Enable en-passant
- Enable castling
- Add support for fen notation
- Add support for pgn notation

### Resources
- [FIDE: Laws of Chess](https://www.fide.com/FIDE/handbook/LawsOfChess.pdf)
- [Description of the universal chess interface (UCI)](http://download.shredderchess.com/div/uci.zip)
- [Stockfish: A UCI Compatible chess engine](https://stockfishchess.org)
- [Forsyth–Edwards Notation (FEN)](https://en.wikipedia.org/wiki/Forsyth–Edwards_Notation)