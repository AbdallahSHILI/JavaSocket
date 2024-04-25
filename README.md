# Java Socket Game: Number Guessing

This project implements a simple number guessing game using Java sockets where two players compete to guess a randomly generated number between 0 and 20. The server manages the game logic and communication between the two clients.

## Features

- **Multiplayer Game**: Two players can join the game and guess numbers until one of them wins by guessing the correct number.
- **Server-Client Communication**: Utilizes TCP sockets for real-time communication between the server and each client.
- **Dynamic Turn Management**: The server controls whose turn it is to guess and informs the other player to wait.
- **Game Replay Option**: After a game ends, players can choose to play again or exit.
- **Server IP Display**: The server displays its IP address on startup to facilitate client connections on dynamic IP setups.

## How to Run

### Requirements

- Java Development Kit (JDK) installed on all machines participating in the game.

### Server Setup

1. **Start the Server**:
   - Navigate to the directory containing `ServerSide.java`.
   - Compile the Java file:
     ```
     javac ServerSide.java
     ```
   - Run the compiled class:
     ```
     java ServerSide
     ```
   - Note the IP address displayed by the server if you are running clients on different machines.

### Client Setup

1. **Start Each Client**:
   - Navigate to the directory containing `ClientSide.java`.
   - Compile the Java file:
     ```
     javac ClientSide.java
     ```
   - Run the compiled class, providing the server's IP address when prompted:
     ```
     java ClientSide
     ```
   - If running on the same machine as the server, you can just press Enter to use `localhost`.

### Gameplay

1. **Game Start**:

   - Once both clients are connected, the game starts automatically.
   - The server randomly selects one of the two players to make the first guess.

2. **Guessing**:

   - Players take turns guessing numbers between 0 and 20.
   - The server informs each player whether their guess was too high, too low, or correct.

3. **Winning the Game**:

   - The player who guesses the number correctly first wins.
   - Both players receive a message indicating the game outcome and are asked if they want to play again.

4. **Replay or Exit**:
   - If both players agree to replay, the game restarts with a new number.
   - If any player chooses not to replay, the game ends, and the server closes the connections.

## Conclusion

This simple Java socket application demonstrates basic network programming concepts, including handling multiple client connections, synchronous I/O, and simple game logic integration. It can be extended with features such as better error handling, GUI integration, and more complex game rules.

# Made with ❤️ by

- Ahmed Elj, @lorffine

- Abdallah Shilli, @AbdallahSHILI

- Zied Jarboui,
