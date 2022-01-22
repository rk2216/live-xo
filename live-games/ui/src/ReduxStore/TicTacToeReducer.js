import { UPDATE_GAME } from "./Actions";
import { CURRENT_TURN, GAME_STATE, PLAYERS } from "./Constants";

export const ticTacToeReducer = (state, action) => {
    switch (action.type) {
        case UPDATE_GAME: {
            const {
                gameName,
                board,
                players,
                currentTurn
            } = action.payload;
            if (gameName === 'TIC_TAC_TOE') {
                return state.set(GAME_STATE, board).set(PLAYERS, players).set(CURRENT_TURN, currentTurn);
            }
            return state;
        }
        default:
            return state
    }
};