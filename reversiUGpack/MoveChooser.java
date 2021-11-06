import java.util.ArrayList;

public class MoveChooser {

    //initialise values of each position on board
    static int boardPosValues[][] =   {
        {120, -20, 20, 5, 5, 20, -20, 120},
        {-20, -40, -5, -5, -5, -5, -40, -20},
        {20, -5, 15, 3, 3, 15, -5, 20},
        {5, -5, 3, 3, 3, 3, -5, 5},
        {5, -5, 3, 3, 3, 3, -5, 5},
        {20, -5, 15, 3, 3, 15, -5, 20},
        {-20, -40, -5, -5, -5, -5, -40, -20},
        {120, -20, 20, 5, 5, 20, -20, 120}
    };

    public static int bestMove;
    public static int score;
    public static int whiteScore;
    public static int blackScore;
    public static int bestScore;
    public static BoardState boardStateCopy;
    public static int alpha;
    public static int beta;

    public static Move chooseMove(BoardState boardState){

    //retrieve depth of search tree
	int searchDepth= Othello.searchDepth;

        //retrieve array of legal moves for current board state
        ArrayList<Move> moves= boardState.getLegalMoves();

        //if there are no moves in the array return null
        if(moves.isEmpty()){
            return null;
	    }

        //else call the moveSelect function which returns the index of the best move in the legal moves list
        return moves.get(moveSelect(moves, boardState, searchDepth, true, alpha, beta));
    
    }

    public static int moveSelect(ArrayList<Move> movesListInput, BoardState boardStateInput, int searchDepthInput, boolean whiteTurn, int alpha, int beta){
        
        //if the board state is a game ending one return the current best move
        if(boardStateInput.gameOver()){
            return bestMove;
        }

        //if we reach the leaf node of the minimax search return the score of that node
        if(searchDepthInput == 0){
            return evaluate(boardStateInput);
        }

        //check if it's whites turn, the maximising player
        if(whiteTurn){
            bestScore = -1000000;
            //iterate through list of legal moves
            for (Move move : movesListInput){
                BoardState boardStateCopy = boardStateInput.deepCopy(); //creates copy of current board state
                boardStateCopy.makeLegalMove(move.x, move.y); //execute current move in list, explore child node
                score = moveSelect(boardStateCopy.getLegalMoves(), boardStateCopy, searchDepthInput - 1, false, alpha, beta); //recursively call function until depth is 0, then returning value of leaf node
                if(bestScore < score){
                    bestMove = movesListInput.indexOf(move); //if the score of the node is greater than its sibling, store the move leading to that node
                }
                bestScore = Math.max(score, bestScore); //store the new highest score after evaluation
                alpha = Math.max(alpha, score); //store the new greater alpha value
                if (beta <= alpha){ //alpha-beta pruning; ignore branches with beta values lower than alpha
                    break;
                }
            }
        } 
        
        //else it's blacks turn, the minimising player
        else {
            bestScore = 1000000;
            for (Move move : movesListInput){
                BoardState boardStateCopy = boardStateInput.deepCopy(); //creates copy of current board state
                boardStateCopy.makeLegalMove(move.x, move.y); //execute current move in list, explore child node
                score = moveSelect(boardStateCopy.getLegalMoves(), boardStateCopy, searchDepthInput - 1, true, alpha, beta);
                if(bestScore > score){
                    bestMove = movesListInput.indexOf(move); //if the score of the node is lesser than its sibling, store the move leading to that node
                }
                bestScore = Math.min(score, bestScore);//store the new lowest score after evaluation
                beta = Math.min(beta, score); //store the new lesser beta value
                if (beta <= alpha){ //alpha-beta pruning; ignore branches with beta values lower than alpha
                    break;
                }
            }
        }
        return bestMove;
    }

    //evaluates the score of the boardstate
    public static int evaluate(BoardState boardStateInput){
        //initialise board scores
        whiteScore = 0;
        blackScore = 0;

        //iterate over board squares
        for(int i= 0; i < 8; i++){
            for(int j= 0; j < 8; j++){
                if(boardStateInput.getContents(i, j) == 1){ //if the square contains a white piece, add to white score total
                    whiteScore += boardPosValues[i][j];

                } else if(boardStateInput.getContents(i, j) == -1){ //if the square contains a black piece, add to black score total
                    blackScore += boardPosValues[i][j];

                }
            }
        }
        return score =  whiteScore - blackScore; //get overall score of board state
    }
}