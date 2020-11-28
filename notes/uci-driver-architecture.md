# UCI Driver Architecture

## Components
- UCIEngine
    - start
        - start the process
        - SwitchToUCI -> IsReady -> StartANewGame
    - evaluate
        - GetBestMove
- UCICommand
    - Simple Commands
    - Commands with predefined parameters
- UCIResponse
- UCIProcedure
    - A procedure will have an `execute` method, with return type `T`
    - New Procedures can be created with Procedures

### UCIProcedures
1. SendCommand: Send a command
2. ReadResponse: Read until we get the response
3. SwitchToUCI: SendCommand `uci` -> ReadResponse `uciok` 
4. IsReady: SendCommand `isready` -> ReadResponse `readyok`
5. StartANewGame: SendCommand `ucinewgame` -> IsReady
6. SetPosition: SendCommand `position`
7. Go: SendCommand `go`
8. GetBestMove: SetPosition -> Go -> ReadResponse `bestmove`
9. Stop: SendCommand `stop`
10. PonderHit: SendCommand `ponderhit`
11. Quit: SendCommand `quit`
12. SetOption: Not supported #TODO