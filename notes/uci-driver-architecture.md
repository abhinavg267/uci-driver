# UCI Driver Architecture

## Models
- UCIEngine
    - create
    - sendCommand
        - returns Unit
    - readResponse 
        - return UCIResponse
    - destroy
       
- UCICommand
    - Simple Commands
    - Commands with predefined parameters
    
- UCIResponse
    - ResponseType
    - Response
        - readResponse method in Engine will return this

- UCIProcedure
    - A procedure will have an `execute` method, with a generic return type `T`
    - Procedures can be clubbed together with `->` to create new Procedure, the return type of the new Procedure will 
        be the same as the return type of Procedure in RHS (Right-Hand-Side)
    - ReturnType of `ReadResponse` Procedure should be a specific Implementation of `UCIResponse` instead of generic `UCIResponse` #TODO

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