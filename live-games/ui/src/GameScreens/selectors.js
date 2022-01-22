import { CURRENT_TURN, GAME_STATE, PLAYERS, USERNAME } from '../ReduxStore/Constants';
import { ROOT, TIC_TAC_TOE } from '../ReduxStore/CreateStore';

export const getGameState = state => state.getIn([TIC_TAC_TOE, GAME_STATE]) || ['', '', '', '', '', '', '', '', ''];
export const getCurrentTurn = state => state.getIn([TIC_TAC_TOE, CURRENT_TURN]) || 'X';
export const getPlayerPosition = state => {
    const userName = state.getIn([ROOT, USERNAME]) || '';
    const players = state.getIn([TIC_TAC_TOE, PLAYERS]) || {};
    return players[userName] || '';
};