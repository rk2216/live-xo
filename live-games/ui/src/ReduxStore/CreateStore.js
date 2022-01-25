import Immutable from "immutable";
import { createStore } from 'redux';
import { combineReducers } from "redux-immutable";
import { rootReducer } from "./RootReducer";
import { ticTacToeReducer } from "./TicTacToeReducer";

export const ROOT = 'root';
export const TIC_TAC_TOE = 'ticTacToe';
export const store = createStore(combineReducers({
    [ROOT]: rootReducer,
    [TIC_TAC_TOE]: ticTacToeReducer
}),
    Immutable.fromJS({
        [ROOT]: {},
        [TIC_TAC_TOE]: {
            gameState: ['', '', '', '', '', '', '', '', ''],
            currentTurn: 'X'
        }
    }),
    window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__());