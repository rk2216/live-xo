import { UPDATE_GAME } from "./Actions";
import { CURRENT_TURN, GAME_STATE, PLAYERS, WINNER } from "./Constants";

export const ticTacToeReducer = (state, action) => {
    switch (action.type) {
        case UPDATE_GAME: {
            const {
                gameName,
                board,
                players,
                currentTurn,
                winner
            } = action.payload;
            if (gameName === 'TIC_TAC_TOE') {
                return state.set(GAME_STATE, board)
                .set(PLAYERS, players)
                .set(CURRENT_TURN, currentTurn)
                .set(WINNER, winner);
            }
            return state;
        }
        default:
            return state
    }
};